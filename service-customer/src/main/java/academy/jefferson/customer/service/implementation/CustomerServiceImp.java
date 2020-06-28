package academy.jefferson.customer.service.implementation;

import academy.jefferson.customer.entity.Customer;
import academy.jefferson.customer.entity.Region;
import academy.jefferson.customer.repository.CustomerRepository;
import academy.jefferson.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findCustomerAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersByRegion(Region region) {
        return customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customerDB = customerRepository.findByNumberID(customer.getNumberID());
        if(customerDB != null){
            return customerDB;
        }

        customer.setState("Created");

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerDB = getCustomer(customer.getId());
        if(customerDB != null){
            return customerDB;
        }
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());
        return customerRepository.save(customerDB);
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        Customer customerDB = getCustomer(customer.getId());
        if(customerDB != null){
            return customerDB;
        }
        customerDB.setState("Deleted");
        return customerRepository.save(customerDB);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
