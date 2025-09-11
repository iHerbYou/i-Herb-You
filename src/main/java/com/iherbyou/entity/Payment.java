package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "paymentStatusId", nullable = false)
    private Code paymentStatus;

    @ManyToOne
    @JoinColumn(name = "paymentMethodId", nullable = false)
    private Code paymentMethod;

    @Column
    private LocalDateTime paymentDate;

    @Column
    private Integer paymentAmount;

    //TODO 질문있습니다
    public Payment(Order order, Code paymentStatus, Code paymentMethod, LocalDateTime paymentDate, Integer paymentAmount) {
        this.order = order;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
    }

}
