package academy.jefferson.shopping.service.implementation;

import academy.jefferson.shopping.client.CustomerClient;
import academy.jefferson.shopping.client.ProductClient;
import academy.jefferson.shopping.entity.Invoice;
import academy.jefferson.shopping.entity.InvoiceItem;
import academy.jefferson.shopping.model.Customer;
import academy.jefferson.shopping.model.Product;
import academy.jefferson.shopping.repository.InvoiceRepository;
import academy.jefferson.shopping.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImp implements InvoiceService {

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice ( invoice.getNumberInvoice () );
        if (invoiceDB !=null){
            return  invoiceDB;
        }
        invoice.setState("CREATED");
        invoiceDB = invoiceRepository.save(invoice);
        invoiceDB.getItems().forEach( invoiceItem -> {
            productClient.updateStock(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });

        return invoiceDB;
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null){
            return  null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null){
            return  null;
        }
        invoiceDB.setState("Deleted");
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice getInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if(invoice != null){
            Customer customer = customerClient.getCustomer(id).getBody();
            invoice.setCustomer(customer);
            List<InvoiceItem> listItem = invoice.getItems().stream()
                    .map(invoiceItem -> {
                        Product product = productClient.getProducts(invoiceItem.getProductId()).getBody();
                        invoiceItem.setProduct(product);
                        return invoiceItem;
                    }).collect(Collectors.toList());
            invoice.setItems(listItem);
        }
        return invoice;
    }

}
