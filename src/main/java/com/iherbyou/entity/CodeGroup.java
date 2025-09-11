package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
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
    private List<Code> codes; //TODO 이 부분 정확히 이해 안된다
}
