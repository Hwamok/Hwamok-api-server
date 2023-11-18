package com.hwamok.product.domain;

import com.hwamok.category.domain.Category;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.notice.domain.NoticeStatus;
import com.hwamok.support.BaseEntity;
import com.hwamok.utils.PreConditions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
  @Column(length = 20, nullable = false)
  private String name;

  //todo 자동생성으로 바꿔보기
  @Column(length = 6, nullable = false)
  private String code;

  @Column(nullable = false)
  private Integer price;

  @ManyToOne(fetch = FetchType.LAZY)
  @Column(nullable = false)
  private Category category;

  @Column(nullable = false)
  private ProductStatus productStatus = ProductStatus.ACTIVATED;

  public Product(String name, String code, Integer price, Category category) {
    PreConditions.require(Strings.isNotBlank(name));
    PreConditions.require(Strings.isNotBlank(code));
    PreConditions.require(price != null);
    PreConditions.require(category != null);
    PreConditions.require(productStatus != null);

    PreConditions.validate(name.length() < 21, ExceptionCode.NOT_NAME_FORM);
    PreConditions.validate(price > -1, ExceptionCode.NOT_PRICE_FORM);

    this.name = name;
    this.code = code;
    this.price = price;
    this.category = category;
  }

  public void updateProduct(String name, String code, Integer price, Category category){
    PreConditions.require(Strings.isNotBlank(name));
    PreConditions.require(Strings.isNotBlank(code));
    PreConditions.require(price != null);
    PreConditions.require(category != null);

    PreConditions.validate(name.length() < 21, ExceptionCode.NOT_NAME_FORM);
    PreConditions.validate(price > -1, ExceptionCode.NOT_PRICE_FORM);

    this.name = name;
    this.code = code;
    this.price = price;
    this.category = category;
  }

  public void deleteProduct(){
    this.productStatus = ProductStatus.INACTIVATED;
  }
}
