package academy.jefferson.shopping.client;

import academy.jefferson.shopping.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-product")
@RequestMapping(value = "/products")
public interface ProductClient {

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProducts(@PathVariable(name = "id") Long id);

    @GetMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable(name = "id") Long id, @RequestParam(value = "quantity", required = true) Double quantity);

}
