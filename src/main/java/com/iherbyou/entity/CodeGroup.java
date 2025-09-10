package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class CodeGroup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String groupName;

    @Column(length = 50)
    private String groupKey;

    @OneToMany(mappedBy = "code")
    private List<Code> codes;
}
