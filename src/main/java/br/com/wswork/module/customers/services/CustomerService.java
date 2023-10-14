package br.com.wswork.module.customers.services;

import br.com.wswork.module.customers.configs.BusinessException;
import br.com.wswork.module.customers.constants.CustomerTypeEnum;
import br.com.wswork.module.customers.dtos.requests.CustomerDtoRequest;
import br.com.wswork.module.customers.dtos.requests.LoginDtoRequest;
import br.com.wswork.module.customers.dtos.responses.CustomerDtoResponse;
import br.com.wswork.module.customers.entities.Customer;
import br.com.wswork.module.customers.repositories.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class.getName());

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<CustomerDtoResponse> createLogist(final CustomerDtoRequest dto) {
        Customer logist = new Customer();
        logist.setAge(dto.getAge());
        logist.setEmail(dto.getEmail());
        logist.setPassword(dto.getPassword(), passwordEncoder);
        logist.setFirstName(dto.getFirstName());
        logist.setLastName(dto.getLastName());
        logist.setCustomerType(CustomerTypeEnum.ADMIN.getDescription());

        LOGGER.info("Saving logist on database...");
        Customer customer = customerRepository.save(logist);
        LOGGER.info("Saved.");

        CustomerDtoResponse response = buildCustomerResponse(customer);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CustomerDtoResponse> createCustomer(final CustomerDtoRequest dto) {
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

        CustomerDtoResponse response = buildCustomerResponse(customer);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CustomerDtoResponse> findByEmailAndPassword(final LoginDtoRequest dto) {
        LOGGER.info("Searching user to login...");
        Customer user = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Incorrect email or password."));
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
