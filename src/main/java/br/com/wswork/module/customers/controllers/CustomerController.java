package br.com.wswork.module.customers.controllers;

import br.com.wswork.module.customers.dtos.requests.CustomerDtoRequest;
import br.com.wswork.module.customers.dtos.requests.LoginDtoRequest;
import br.com.wswork.module.customers.dtos.responses.CustomerDtoResponse;
import br.com.wswork.module.customers.services.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation("Create a new customer")
    @PostMapping("{storeId}")
    public ResponseEntity<CustomerDtoResponse> createCustomer(
            @RequestBody(required = true) final CustomerDtoRequest dto,
            @PathVariable(name = "storeId", required = true) final Long storeId) {

        return customerService.createCustomer(dto, storeId);
    }

    @ApiOperation("Search user by email and password")
    @PostMapping("")
    public ResponseEntity<CustomerDtoResponse> findByEmailAndPassword(
            @RequestParam(name = "storeId", required = true) final Long storeId,
            @RequestBody(required = true) final LoginDtoRequest dto) {

        return customerService.findByEmailAndPassword(storeId, dto);
    }

    @ApiOperation("Get customer profile")
    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDtoResponse> findCustomerById(
            @PathVariable(name = "customerId") final Long customerId) {

        return customerService.findCustomerById(customerId);
    }

    @ApiOperation("Update customer")
    @PatchMapping("{customerId}")
    public ResponseEntity<CustomerDtoResponse> updateCustomer(
            @PathVariable(name = "customerId") final Long customerId,
            @RequestBody(required = true) final CustomerDtoRequest request) {

        return customerService.updateCustomer(customerId, request);
    }

    @ApiOperation("Delete customer")
    @DeleteMapping("{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable(name = "customerId") final Long customerId) {

        customerService.delete(customerId);
        return ResponseEntity.noContent().build();
    }
}
