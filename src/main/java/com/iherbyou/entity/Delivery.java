package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id")
    private Code code;

    private int deliveryNumber; //송장번호

    private String deliveryCompany; //택배사

    private String deliveryMemo; //배송 메모

    private Date deliveryStartDate; //배송 시작일

    private Date deliveryCompleteDate; //배송 완료일

}
