package com.orderverse.orderservice;

import com.orderverse.productservice.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public Order createOrder(Order order) {

        // Get product details from product-service
        try {
            ResponseEntity<Product> responseEntity = restTemplate.getForEntity(
                    "http://product-service/products/{productId}",
                    Product.class,
                    order.getProductId()
            );

            Product product = responseEntity.getBody();
            if (product != null) {
                // Check if product is available
                if (product.getQuantityAvailable() > 0) {
                    order.setProductName(product.getName());
                    // update product quantity
                    product.setQuantityAvailable(product.getQuantityAvailable() - 1);
                    restTemplate.put("http://product-service/products/{productId}", product, product.getId());
                    return orderRepository.save(order);
                } else {
                    throw new RuntimeException("Product is out of stock");
                }
            }
            System.out.println("Product: " + product);
        } catch (HttpClientErrorException.NotFound ex) {
            // Handle 404 error
            throw new RuntimeException("Product not found");
        }

        return null;

    }

    public Order updateOrder(Long id, Order order) {
        order.setId(id); // Ensure the correct ID is set
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}

