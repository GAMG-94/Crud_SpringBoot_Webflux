package com.webflux.programacionreactiva.router;

import com.webflux.programacionreactiva.handler.ProductHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ProductRouter {

    private static final String PATH = "product";

    @Bean
    RouterFunction<ServerResponse> routerFunction(ProductHandler handler) {
        return RouterFunctions.route()
                .GET(PATH, handler::getAll)
                .GET(PATH + "/{id}", handler::getById)
                .POST(PATH, handler::saveProduct)
                .PUT(PATH + "/{id}", handler::updateProduct)
                .DELETE(PATH + "/{id}", handler::deleteProduct)
                .build();
    }
}
