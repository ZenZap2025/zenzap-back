package com.zenzap.zenzap.service;

import com.zenzap.zenzap.entity.Product;
import com.zenzap.zenzap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> buyProduct(Long productId, int quantity) {
        return productRepository.findById(productId).map(product -> {
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("No hay suficiente stock");
            }
            product.setStock(product.getStock() - quantity);
            return productRepository.save(product);
        });
    }

    public List<Product> findAvailableProducts() {
        return productRepository.findByStockGreaterThan(0);
    }

}
