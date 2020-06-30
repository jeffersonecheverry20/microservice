package academy.jefferson.product.controller;

import academy.jefferson.product.entity.Category;
import academy.jefferson.product.entity.Product;
import academy.jefferson.product.exception.ErrorMessage;
import academy.jefferson.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProducts(@RequestParam(value = "categoryId", required = false) Long id){
        List<Product> listProducts = new ArrayList<>();
        if(id == null){
            listProducts = productService.listAllProduct();
            if(listProducts.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(listProducts);
        } else {
            listProducts = productService.findByCategory(Category.builder().id(id).build());
            if(listProducts.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(listProducts);
        }

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProducts(@PathVariable(name = "id") Long id){
        Product product = productService.getProduct(id);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);

    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){
        System.out.println("JSE --> Create product");
        System.out.println("JSE --> BindingResult is "+result.hasErrors());
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Product productCreate = productService.createProduct(product);
        if(productCreate == null){
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable(name = "id") Long id){
        product.setId(id);
        Product productUpdate = productService.updateProduct(product);
        if(productUpdate == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable(name = "id") Long id){
        Product productDelete = productService.deleteProduct(id);
        if(productDelete == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDelete);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable(name = "id") Long id, @RequestParam(value = "quantity", required = true) Double quantity){
        Product productUpdateStock = productService.updateStock(id, quantity);
        if(productUpdateStock == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productUpdateStock);
    }

    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    System.out.println("JSE --> The field is "+err.getField());
                    System.out.println("JSE --> The error message is "+err.getDefaultMessage());
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("1")
                .messages(errors).build();
        return converterObjectToString(errorMessage);
    }

    private String converterObjectToString(ErrorMessage errorMessage){
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(errorMessage);
        }catch(JsonProcessingException ex){
            ex.printStackTrace();
        }
        System.out.println("JSE --> The json is "+json);
        return json;
    }

}
