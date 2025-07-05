package com.zenzap.zenzap.controller;

import com.zenzap.zenzap.entity.Category;
import com.zenzap.zenzap.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para exponer categorías.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepo;

    @Autowired
    public CategoryController(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    /**
     * GET /api/categories
     * Devuelve todas las categorías.
     */
    @GetMapping
    public List<Category> listAll() {
        return categoryRepo.findAll();
    }
}

