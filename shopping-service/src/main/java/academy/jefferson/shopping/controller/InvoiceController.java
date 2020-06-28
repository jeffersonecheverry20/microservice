package academy.jefferson.shopping.controller;

import academy.jefferson.shopping.entity.Invoice;
import academy.jefferson.shopping.exception.ErrorMessage;
import academy.jefferson.shopping.service.InvoiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> listAllInvoices(){
        List<Invoice> invoices = invoiceService.findInvoiceAll();
        if(invoices.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable(value = "id") Long id){
        Invoice invoice = invoiceService.getInvoice(id);
        if(invoice == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult result) {
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Invoice invoiceDB = invoiceService.createInvoice (invoice);

        return  ResponseEntity.status( HttpStatus.CREATED).body(invoiceDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable("id") long id, @RequestBody Invoice invoice) {
        invoice.setId(id);
        Invoice currentInvoice=invoiceService.updateInvoice(invoice);
        if (currentInvoice == null) {
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(currentInvoice);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice == null) {
            return  ResponseEntity.notFound().build();
        }
        invoice = invoiceService.deleteInvoice(invoice);
        return ResponseEntity.ok(invoice);
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
