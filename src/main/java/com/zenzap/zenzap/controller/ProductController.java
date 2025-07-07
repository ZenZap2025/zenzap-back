package com.zenzap.zenzap.controller;
import com.zenzap.zenzap.controller.dto.OrderRequest;
import com.zenzap.zenzap.controller.dto.PurchaseRequest;
import com.zenzap.zenzap.entity.Product;
import com.zenzap.zenzap.service.ProductService;
import com.zenzap.zenzap.service.PurchaseEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Optional;


import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productService.findById(id).map(existing -> {
            product.setId(id);
            return ResponseEntity.ok(productService.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.findById(id).isPresent()) {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyProducts(@RequestBody OrderRequest order) {
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            return ResponseEntity.badRequest().body("La lista de productos está vacía.");
        }

        for (PurchaseRequest request : order.getProducts()) {
            Optional<Product> productOpt = productService.buyProduct(request.getProductId(), request.getQuantity());

            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Producto con ID " + request.getProductId() + " no encontrado.");
            }
        }

        // ✅ Enviar correo de confirmación con resumen de productos
        purchaseEmailService.sendPurchaseConfirmation(
                order.getEmail(),
                order.getClientName(),
                order.getAddress(),
                order.getProducts()
        );

        return ResponseEntity.ok("Compra procesada con éxito.");
    }


    @Autowired
    private PurchaseEmailService purchaseEmailService;






    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        return ResponseEntity.ok(productService.findAvailableProducts());
    }





}

