package com.hwamok.product.domain;

import com.hwamok.category.domain.Category;
import com.hwamok.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
  private String name;
  private String code;
  private int price;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;
}
