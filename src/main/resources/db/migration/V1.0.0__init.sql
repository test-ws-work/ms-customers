CREATE TABLE customers (
     id SERIAL PRIMARY KEY NOT NULL,
     first_name VARCHAR(40) NOT NULL,
     last_name VARCHAR(60) NOT NULL,
     age INT NOT NULL,
     email VARCHAR(50) NOT NULL,
     password VARCHAR(255) NOT NULL,
     customer_type VARCHAR(60)
);

CREATE TABLE customers_store (
    id SERIAL PRIMARY KEY NOT NULL,
    store_id INT NOT NULL,
    customer_id INT NOT NULL,

    FOREIGN KEY (customer_id) REFERENCES customers(id)
);