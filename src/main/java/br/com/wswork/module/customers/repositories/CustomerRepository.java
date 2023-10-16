package br.com.wswork.module.customers.repositories;

import br.com.wswork.module.customers.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(@Param("email") final String email);

    Collection<Customer> findAllByEmail(@Param("email") final String email);

    @Query("SELECT c FROM Customer c " +
            "JOIN c.customerStores cs " +
            "WHERE cs.storeId = :storeId " +
            "AND c.email = :email")
    Optional<Customer> emailAlreadyExistsOnStore(@Param("storeId") final Long storeId, @Param("email") final String email);
}
