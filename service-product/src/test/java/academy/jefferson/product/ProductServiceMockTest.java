package academy.jefferson.product;

import academy.jefferson.product.entity.Category;
import academy.jefferson.product.entity.Product;
import academy.jefferson.product.repository.ProductRepository;
import academy.jefferson.product.service.ProductService;
import academy.jefferson.product.service.implementation.ProductServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImp(productRepository);
        Product product = Product.builder()
                .id(1L)
                .name("Computer")
                .category(Category.builder().id(1L).build())
                .price(Double.parseDouble("12.5"))
                .stock(Double.parseDouble("5"))
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
    }

    @Test
    public void whenValidGetID_thenReturnProduct(){
        Product product = productService.getProduct(1L);
        Assertions.assertThat(product.getName()).isEqualTo("Computer");
    }

    @Test
    public void whenValidUpdateStock_thenReturnNewStock(){
        Product product = productService.updateStock(1L, 5.0);
        Assertions.assertThat(product.getStock()).isEqualTo(10.0);
    }

}
