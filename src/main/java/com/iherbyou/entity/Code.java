package com.iherbyou.entity;

import jakarta.persistence.*;

@Entity
public class Code {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_group_id")
    private CodeGroup codeGroup;

    @Column(length = 50)
    private String codeName;

    private int sortIdx; //정렬순서
}
