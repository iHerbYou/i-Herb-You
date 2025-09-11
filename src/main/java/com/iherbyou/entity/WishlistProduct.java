package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Entity
public class WishlistProduct {

    @Id
    private Long id; //TODO 이거는 GeneratedValue 필요없나요?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlistId", nullable = false)
    private Wishlist wishlist;

}
