package br.com.wswork.module.customers.services;

import br.com.wswork.module.customers.configs.BusinessException;
import br.com.wswork.module.customers.constants.CustomerTypeEnum;
import br.com.wswork.module.customers.dtos.requests.CustomerDtoRequest;
import br.com.wswork.module.customers.dtos.requests.LoginDtoRequest;
import br.com.wswork.module.customers.dtos.responses.CustomerDtoResponse;
import br.com.wswork.module.customers.entities.Customer;
import br.com.wswork.module.customers.entities.CustomerStore;
import br.com.wswork.module.customers.repositories.CustomerRepository;
import br.com.wswork.module.customers.repositories.CustomerStoreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class.getName());

    private final CustomerRepository customerRepository;
    private final CustomerStoreRepository customerStoreRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, CustomerStoreRepository customerStoreRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerStoreRepository = customerStoreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<CustomerDtoResponse> createCustomer(final CustomerDtoRequest dto, final Long storeId) {

        LOGGER.info("Verify email exists on store...");
        Optional<Customer> customerExists = customerRepository.emailAlreadyExistsOnStore(storeId, dto.getEmail());
        LOGGER.info("Already email registred");

        if (customerExists.isPresent()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Email already registred on this store.");
        }

        Customer customer = new Customer();
        customer.setAge(dto.getAge());
        customer.setEmail(dto.getEmail());
        customer.setPassword(dto.getPassword(), passwordEncoder);
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setCustomerType(CustomerTypeEnum.CUSTOMER.getDescription());

        LOGGER.info("Saving logist on database...");
        customer = customerRepository.save(customer);
        LOGGER.info("Saved.");

        CustomerStore customerStore = new CustomerStore();
        customerStore.setStoreId(storeId);
        customerStore.setCustomer(customer);

        LOGGER.info("Saving store this customer...");
        customerStoreRepository.save(customerStore);
        LOGGER.info("Saved.");

        CustomerDtoResponse response = buildCustomerResponse(customer);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CustomerDtoResponse> findByEmailAndPassword(final Long storeId, final LoginDtoRequest dto) {
        LOGGER.info("Searching user to login...");
        Customer user = customerRepository.emailAlreadyExistsOnStore(storeId, dto.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Incorrect email or password."));
        LOGGER.info("Found.");

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Incorrect email or password.");
        }

        CustomerDtoResponse response = buildCustomerResponse(user);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CustomerDtoResponse> findCustomerById(final Long customerId) {

        LOGGER.info("Searching customer by id...");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Customer not found."));
        LOGGER.info("Found.");

        CustomerDtoResponse response = buildCustomerResponse(customer);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CustomerDtoResponse> updateCustomer(final Long customerId, final CustomerDtoRequest dto) {

        LOGGER.info("Searching customer by id...");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Customer not found."));
        LOGGER.info("Found.");

        if (Objects.nonNull(dto.getAge())) {
            customer.setAge(dto.getAge());
        }

        if(Objects.nonNull(dto.getEmail())) {
            customer.setEmail(dto.getEmail());
        }

        if(Objects.nonNull(dto.getFirstName())) {
            customer.setFirstName(dto.getFirstName());
        }

        if(Objects.nonNull(dto.getLastName())) {
            customer.setLastName(dto.getLastName());
        }

        if(Objects.nonNull(dto.getPassword())) {
            customer.setPassword(dto.getPassword(), passwordEncoder);
        }

        LOGGER.info("Saving changes...");
        customerRepository.save(customer);
        LOGGER.info("Saved.");

        CustomerDtoResponse response = buildCustomerResponse(customer);

        return ResponseEntity.ok(response);
    }

    private CustomerDtoResponse buildCustomerResponse(final Customer customer) {
        CustomerDtoResponse response = new CustomerDtoResponse();
        response.setAge(customer.getAge());
        response.setEmail(customer.getEmail());
        response.setCustomerType(customer.getCustomerType());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setId(customer.getId());

        return response;
    }

    public void delete(final Long customerId) {

        LOGGER.info("Searching customer by id...");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Customer not found."));
        LOGGER.info("Found.");

        LOGGER.info("Deleting customer...");
        customerRepository.delete(customer);
        LOGGER.info("Deleted.");
    }
}
