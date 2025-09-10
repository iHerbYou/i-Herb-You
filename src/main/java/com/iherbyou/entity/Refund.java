package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentId", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codeId", nullable = false)
    private Code code;

    @Column
    private LocalDateTime refundDate;

    @Column(length = 255)
    private String refundReason;

    @Column
    private Integer refundAmount;

    //TODO 질문
    public Refund(Payment payment, Code code, LocalDateTime refundDate, String refundReason, Integer refundAmount) {
        this.payment = payment;
        this.code = code;
        this.refundDate = refundDate;
        this.refundReason = refundReason;
        this.refundAmount = refundAmount;

    }
}
