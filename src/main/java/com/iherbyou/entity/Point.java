package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Point {

    @Id
    private Long userId;

    // userId가 PK이자 FK
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int balance;

    public Point() {
    }

    public Point(Long userId, User user, int balance) {
        this.userId = userId;
        this.user = user;
        this.balance = balance;
    }
}
