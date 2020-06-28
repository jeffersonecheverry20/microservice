package academy.jefferson.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "tbl_customers")
@Data
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The field numberId don't must be empty")
    @Size(min = 8, max = 8, message = "The length of field numberID don't must be greater than or smaller than 8")
    @Column(name = "number_id", unique = true, length = 8, nullable = false)
    private String numberID;

    @NotEmpty(message = "The field firstName don't must be empty")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "The field lastName don't must be empty")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "The field email don't must be empty")
    @Email(message = "The structure of email is not correct")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    @NotNull(message = "The field region don't must be empty")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Region region;

    private String state;

}
