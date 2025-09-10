package com.iherbyou.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Cart")   //
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User userId;

    @Column(name = "sub_total")
    private Integer subTotal;

    @Column(name = "discount_total")
    private Integer discountTotal;

    @Column(name = "grand_total")
    private Integer grandTotal;

    public Cart() {
    }
}
