package com.webflux.programacionreactiva.repository;

import com.webflux.programacionreactiva.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    Mono<Product> findByProductName(String productName);

    @Query("SELECT * FROM product WHERE id <> :id AND productName = :productName")
    Mono<Product> repeatName(Integer id, String productName);
}
