package com.inventory.services;

import com.inventory.data.models.Customer;
import com.inventory.data.models.Item;
import com.inventory.data.models.Order;
import com.inventory.data.models.Product;
import com.inventory.data.repositories.OrderRepository;
import com.inventory.data.repositories.CustomerRepository;
import com.inventory.data.repositories.ProductRepository;
import com.inventory.dtos.QuantityOperation;
import com.inventory.dtos.requests.OrderRequestDto;
import com.inventory.dtos.requests.OrderUpdateDto;
import com.inventory.dtos.responses.OrderResponseDto;
import com.inventory.exceptions.InventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final String topic = "order_details";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderDetails(String message){
        this.kafkaTemplate.send(topic,message );
    }
    private boolean quantityIsNotValid(Product product, int quantity){
        return product.getQuantity() <= quantity;
    }

    private OrderResponseDto buildCartResponse(Order order){
        return OrderResponseDto.builder()
                .itemList(order.getItemList())
                .totalPrice(order.getTotalPrice())
                .build();
    }


    private Double calculateItemPrice(Item item){
        return item.getProduct().getPrice() * item.getQuantityAdded();
    }


    private Customer getUserFromRequestDto(OrderRequestDto orderRequestDto) throws InventoryException {
        Optional<Customer> query = customerRepository.findById(orderRequestDto.getUserId());

        if ( query.isEmpty() ){
            throw new InventoryException("User with ID "+ orderRequestDto.getUserId()+ " not found");
        }
        return query.get();
    }



    private Product getProductFromRequestDto(OrderRequestDto orderRequestDto)
            throws InventoryException {

        Product product = productRepository.findById(13L).orElse(null);
        if ( product ==null ){
            throw new InventoryException("Product with ID "+
                    orderRequestDto.getProductId()+" does not exist");
        }

        if ( quantityIsNotValid(product, orderRequestDto.getQuantity()) )
            throw new InventoryException("Quantity too large");
        return product;
    }


    private void calculateNewTotalPriceOfCart(OrderRequestDto orderRequestDto, Order order, Product product) {
        Item cartItem = new Item(product, orderRequestDto.getQuantity());
        order.addItem(cartItem);
        order.setTotalPrice(order.getTotalPrice()+ calculateItemPrice(cartItem));
    }


    private Optional<Item> findCartItem(Long itemId, Order order){
        Predicate<Item> itemPredicate = i -> i.getId().equals(itemId);
        return order.getItemList().stream().filter(itemPredicate).findFirst();
    }


    @Override
    public OrderResponseDto orderItem(OrderRequestDto orderRequestDto) throws InventoryException {

        Customer customer = getUserFromRequestDto(orderRequestDto);
        Product product = getProductFromRequestDto(orderRequestDto);
        Order order = customer.getOrder();

        calculateNewTotalPriceOfCart(orderRequestDto, order, product);
        orderRepository.save(order);
        return buildCartResponse(order);
    }

    @Override
    public OrderResponseDto viewOrder(Long userId) throws InventoryException {
        Customer customer = customerRepository.findById(userId).orElse(null);
        if ( customer == null ) throw new InventoryException("User with id "+userId +" not found");

        Order order = customer.getOrder();
        return buildCartResponse(order);
    }

    @Override
    public OrderResponseDto updateOrderItem(OrderUpdateDto orderUpdateDto) throws InventoryException {
        //get user by id
        Customer customer = customerRepository.findById(orderUpdateDto.getUserId()).orElse(null);
        if ( customer == null )
            throw new InventoryException("User with id "
                    + orderUpdateDto.getUserId() +" does not exist");
        //get a order by userId
        Order order = customer.getOrder();
        //Find an item within order within itemId
        Item item = findCartItem(orderUpdateDto.getItemId(), order).orElse(null);
        if ( item == null ) throw new InventoryException("Item not in order");

        //Perform update to item

        if ( orderUpdateDto.getQuantityOperation() == QuantityOperation.INCREASE ) {
            item.setQuantityAdded(item.getQuantityAdded() + 1);
            order.setTotalPrice(order.getTotalPrice() + item.getProduct().getPrice());
        }
        else if ( orderUpdateDto.getQuantityOperation() == QuantityOperation.DECREASE ) {
            item.setQuantityAdded(item.getQuantityAdded() - 1);
            order.setTotalPrice(order.getTotalPrice() - item.getProduct().getPrice());

        }

        orderRepository.save(order);

        return buildCartResponse(order);
    }

    @Override
    public void sendMessage(String message) {

    }
}
