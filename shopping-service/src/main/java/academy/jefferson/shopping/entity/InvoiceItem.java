package academy.jefferson.shopping.entity;

import academy.jefferson.shopping.model.Product;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Data
@Entity
@Table(name = "tbl_invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive(message = "The field quantity must be greater than zero")
    private Double quantity;
    private Double price;
    @Column(name = "product_id")
    private Long productId;
    @Transient
    private Double subTotal;
    @Transient
    private Product product;

    public Double getSubTotal(){
        if(this.price > 0 && this.quantity > 0){
            return this.price * this.quantity;
        } else {
            return (double) 0;
        }
    }

    public InvoiceItem(){
        this.quantity = (double) 0;
        this.price = (double) 0;
    }

}
