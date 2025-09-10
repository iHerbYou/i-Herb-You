package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.query.Order;
import org.springframework.web.servlet.view.RedirectView;

import javax.smartcardio.CardTerminal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column
    private Date createdDate;

    @Column
    private Date updatedDate;

    @Column
    private boolean isTermsAgreed;

    @Column
    private boolean isVerified;

    // 1:N 관계 (회원 한 명에 여러 주소)
    @OneToMany(mappedBy = "user_address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>();

    // 1:1 관계 (회원 - 포인트)
    @OneToOne(mappedBy = "point", cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    // 1:1 (회원 - 위시리스트)
    @OneToOne(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wishlist wishlist;

    // 1:N (회원 한명에 여러 리뷰)
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews = new ArrayList<>();

    // 1:N (회원 한명에 여러 쿠폰)
//    @OneToMany(mappedBy = "user_coupon", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<UserCoupon> coupons = new ArrayList<>();

    // 1:N (회원 한명에 여러 주문)
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Order> orders = new ArrayList<>();

    // 1:1 (회원 - 장바구니)
//    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Cart cart;


    public User() {
    }

}
