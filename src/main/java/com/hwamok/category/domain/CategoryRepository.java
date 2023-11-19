package com.hwamok.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByBranchAndName (String branch, String name);

    Boolean existsByBranchAndName (String branch, String name);

    List<Category> findAllByBranchaAndStatus (String branch, CategoryStatus status);

    List<Category> findAllByCodeTypeAndStatus (String codeType, CategoryStatus status);

    Optional<Category> findByCodeAndStatus (String code, CategoryStatus status);

    List<Category> findAllByNameAndStatus (String name, CategoryStatus status);

}
