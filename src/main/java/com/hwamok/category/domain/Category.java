package com.hwamok.category.domain;

import com.hwamok.product.domain.Product;
import com.hwamok.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
  private String branch;
  private String code;
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
  private List<Category> subCategory = new ArrayList<>();

  private Integer level;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Product> products = new ArrayList<>();
}
