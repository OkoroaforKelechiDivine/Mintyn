package com.inventory.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    private int quantityAdded;

    public Item(Product product, int quantityAdded){
        if ( quantityAdded <= product.getQuantity() )
            this.quantityAdded = quantityAdded;
        else {
            this.quantityAdded = 0;
        }
        this.product = product;
    }

}
