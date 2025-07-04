package com.zenzap.zenzap.repository;

import com.zenzap.zenzap.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para categorías.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

