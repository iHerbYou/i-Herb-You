package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.apache.bcel.classfile.Code;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

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

    protected Payment() {}

    public Payment(Order order, Code paymentStatus, Code paymentMethod, LocalDateTime paymentDate, Integer paymentAmount) {
        this.order = order;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
    }

}
