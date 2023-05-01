package com.Microservices.OrderService.service;

import com.Microservices.OrderService.dto.InventoryResponse;
import com.Microservices.OrderService.dto.OrderRequest;
import com.Microservices.OrderService.model.Order;
import com.Microservices.OrderService.model.OrderLineItems;
import com.Microservices.OrderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional()
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient webClient;

    public String createOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(
                orderLineItemsDto -> {
                    OrderLineItems orderLineItems = new OrderLineItems();
                    orderLineItems.setPrice(orderLineItemsDto.getPrice());
                    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
                    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
                    return orderLineItems;
                }
        ).toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryArray = webClient.get()
                .uri("http://localhost:8082/api/v1/inventories",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryArray).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
            return "Order created successfully!";
        }

        else throw new IllegalArgumentException("Some products are currently not in stock!");
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
