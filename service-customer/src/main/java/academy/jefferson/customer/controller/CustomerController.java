package academy.jefferson.customer.controller;

import academy.jefferson.customer.entity.Customer;
import academy.jefferson.customer.entity.Region;
import academy.jefferson.customer.exception.ErrorMessage;
import academy.jefferson.customer.service.CustomerService;
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
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId", required = false) Long regionId){
        List<Customer> customers = new ArrayList<>();
        if(regionId == null){
            customers = customerService.findCustomerAll();
            if(customers == null && customers.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        } else {
            Region region = new Region();
            region.setId(regionId);
            customers = customerService.findCustomersByRegion(region);
            if(customers == null && customers.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable(value = "id") Long id){
        Customer customer = customerService.getCustomer(id);
        if(customer == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
        System.out.println("JSE --> Execute operation create");
        System.out.println("JSE --> The bindingResult "+result.hasErrors());
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatMessage(result));
        }
        Customer customerDB = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") Long id, @RequestBody Customer customer){
        Customer customerDB = customerService.getCustomer(id);
        if(customerDB == null){
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        customerDB = customerService.updateCustomer(customer);
        return ResponseEntity.ok(customerDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable(value = "id") Long id){
        Customer customerDB = customerService.getCustomer(id);
        if(customerDB == null){
            return ResponseEntity.notFound().build();
        }
        customerDB = customerService.deleteCustomer(customerDB);
        return ResponseEntity.ok(customerDB);
    }

    private String formatMessage(BindingResult result){
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("1")
                .messages(errors)
                .build();
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
        return  json;
    }

}
