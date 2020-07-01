package academy.jefferson.shopping.client;

import academy.jefferson.shopping.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductHystrixFallbackFactory implements ProductClient {

    @Override
    public ResponseEntity<Product> getProducts(Long id) {
        Product product = Product.builder()
                .category(null)
                .description("none")
                .name("none")
                .price(0.0)
                .stock(0.0)
                .build();
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<Product> updateStock(Long id, Double quantity) {
        Product product = Product.builder()
                .category(null)
                .description("none")
                .name("none")
                .price(0.0)
                .stock(0.0)
                .build();
        return ResponseEntity.ok(product);
    }
}
