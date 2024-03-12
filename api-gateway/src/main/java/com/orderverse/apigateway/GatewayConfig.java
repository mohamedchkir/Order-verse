package com.orderverse.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service-route", r -> r
                        .path("/orders/**")
                        .uri("lb://order-service/orders")
                )
                .route("product-service-route", r -> r
                        .path("/products/**")
                        .uri("lb://product-service/products")
                )
                .build();
    }
}
