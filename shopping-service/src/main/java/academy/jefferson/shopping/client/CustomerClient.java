package academy.jefferson.shopping.client;

import academy.jefferson.shopping.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "customer-service")
@RequestMapping(value = "/customers")
public interface CustomerClient {

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable(value = "id") Long id);

}
