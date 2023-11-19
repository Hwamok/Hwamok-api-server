package com.hwamok.category.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.product.domain.Product;
import com.hwamok.support.BaseEntity;
import com.hwamok.utils.PreConditions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
  @Column(length = 30, nullable = false)
  private String branch;

  // TODO: 2023-11-18 코드 자동으로 올라가게 만들기, 유일값
  @Column(length = 5, nullable = false)
  private String code;

  private String codeType= code.substring(0,2);

  @Column(length = 30, nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
  private List<Category> subCategory = new ArrayList<>();

  @Column(nullable = false)
  private Integer level;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Product> products = new ArrayList<>();

  private CategoryStatus status = CategoryStatus.ACTIVATE;

  @Builder
  public Category(String branch, String code, String name, Integer level, Category parentCategory){
    PreConditions.require(Strings.isNotBlank(branch));
    PreConditions.require(Strings.isNotBlank(code));
    PreConditions.require(Strings.isNotBlank(name));
    PreConditions.require(level != null);

    PreConditions.validate(branch.length() < 31, ExceptionCode.NOT_BRANCH_FORM);
    PreConditions.validate(code.length() < 6, ExceptionCode.NOT_CODE_FORM);
    PreConditions.validate(name.length() < 31, ExceptionCode.NOT_NAME_FORM);
    PreConditions.validate(level > -1, ExceptionCode.NOT_LEVEL_FORM);

    this.branch = branch;
    this.code = code;
    this.name = name;
    this.level = level;
    this.parentCategory = parentCategory;
  }

  public void registerParentCategory(Category parentCategory){
    this.parentCategory = parentCategory;
  }

  public void registerLevel(Integer level){
    this.level = level;
  }

  public void updateCategory(String branch, String code, String name, Integer level, Category parentCategory){
    PreConditions.require(Strings.isNotBlank(branch));
    PreConditions.require(Strings.isNotBlank(code));
    PreConditions.require(Strings.isNotBlank(name));
    PreConditions.require(level != null);

    PreConditions.validate(branch.length() < 31, ExceptionCode.NOT_BRANCH_FORM);
    PreConditions.validate(code.length() < 6, ExceptionCode.NOT_CODE_FORM);
    PreConditions.validate(name.length() < 31, ExceptionCode.NOT_NAME_FORM);
    PreConditions.validate(level > -1, ExceptionCode.NOT_LEVEL_FORM);

    this.branch = branch;
    this.code = code;
    this.name = name;
    this.level = level;
    this.parentCategory = parentCategory;
  }

  public void deleteCategory(){
    status = CategoryStatus.INACTIVATE;
  }
}
