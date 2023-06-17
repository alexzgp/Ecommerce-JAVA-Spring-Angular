package com.luv2code.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "product_category")
// @Data -- known bug.
@Getter @Setter
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Product> products;


}
