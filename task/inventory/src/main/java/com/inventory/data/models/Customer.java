package com.inventory.data.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.PERSIST)
    @Getter
    private final Order order;

    public Customer(){
        this.order = new Order();
    }

}
