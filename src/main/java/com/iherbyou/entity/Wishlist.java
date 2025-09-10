package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Wishlist {

    @Id
    @GeneratedValue
    private Long wishlistId;

    // 1:1 (회원 - 위시리스트)
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 1:N (위시리스트 한개에 여러 위시리스트항목)
    @OneToMany(mappedBy = "wishlistProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists = new ArrayList<>();


    public Wishlist() {}

}
