package com.hwamok.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByBranchAndName (String branch, String name);

    Boolean existsByBranchAndName (String branch, String name);

    List<Category> findAllByBranchAndStatus (String branch, CategoryStatus status);

    // TODO: 2023-11-19 도메인 수정 후 추가 예정
//    List<Category> findAllByCodeTypeAndStatus (String codeType, CategoryStatus status);

    Optional<Category> findByIdAndStatus (Long id, CategoryStatus status);

    Optional<Category> findByCodeAndStatus (String code, CategoryStatus status);

    Optional<Category> findByNameAndStatus (String name, CategoryStatus status);

}
