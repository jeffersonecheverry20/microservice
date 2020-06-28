package academy.jefferson.product.service.implementation;

import academy.jefferson.product.entity.Category;
import academy.jefferson.product.entity.Product;
import academy.jefferson.product.repository.ProductRepository;
import academy.jefferson.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    //@Autowired
    private final ProductRepository productRepository;

    @Override
    public List<Product> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("Created");
        product.setCreateProduct(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product product1 = getProduct(product.getId());
        if(product1 == null){
            return  null;
        }
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setCategory(product.getCategory());
        product1.setPrice(product.getPrice());
        return productRepository.save(product1);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product product1 = getProduct(id);
        if(product1 == null){
            return  null;
        }
        product1.setStatus("Deleted");
        return productRepository.save(product1);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product product1 = getProduct(id);
        if(product1 == null){
            return  null;
        }
        product1.setStock(product1.getStock() + quantity);
        return productRepository.save(product1);
    }
}
