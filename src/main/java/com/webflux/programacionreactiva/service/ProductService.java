package com.webflux.programacionreactiva.service;

import com.webflux.programacionreactiva.entity.Product;
import com.webflux.programacionreactiva.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Obteniendo todos los Productos
    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    // Obteniendo Producto por Id y Verificando de que Exista
    public Mono<Product> getById(Integer id) {
        return productRepository.findById(id).switchIfEmpty(Mono.error(new Exception("Product Not Found")));
    }

    // Guardando Producto Nuevo y Verificando que el Nombre No Exista
    public Mono<Product> saveProduct(Product product) {
        Mono<Boolean> existName = productRepository.findByProductName(product.getProductName()).hasElement();
        return existName.flatMap(exists -> exists ? Mono.error(new Exception("Product Name Already In Use")) : productRepository.save(product));
    }

    //Actualizando el Producto y Validando Que el Nombre No Este en Uso en Otro Producto y su Id Sea Diferente
    public Mono<Product> updateProduct(Integer id, Product product) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatName = productRepository.repeatName(id, product.getProductName()).hasElement();
        return productId.flatMap(
                existId -> existId ?
                        productRepeatName.flatMap(existName -> existName ? Mono.error(new Exception("Product Name Already In Use"))
                                : productRepository.save(new Product(id, product.getProductName(), product.getPrice())))
                : Mono.error(new Exception("Product Not Found"))
        );
    }

    //Eliminando Producto por Id
    public Mono<Void> deleteProduct(Integer id){
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        return productId.flatMap(prodId -> prodId ? productRepository.deleteById(id) : Mono.error(new Exception("Product Not Found")));
    }
}
