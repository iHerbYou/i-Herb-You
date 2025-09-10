package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.apache.bcel.classfile.Code;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;

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

    protected Refund() {}

    public Refund(Payment payment, Code code, LocalDateTime refundDate, String refundReason, Integer refundAmount) {
        this.payment = payment;
        this.code = code;
        this.refundDate = refundDate;
        this.refundReason = refundReason;
        this.refundAmount = refundAmount;

    }
}
