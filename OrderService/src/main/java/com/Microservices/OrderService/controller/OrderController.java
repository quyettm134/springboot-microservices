package com.Microservices.OrderService.controller;

import com.Microservices.OrderService.dto.OrderRequest;
import com.Microservices.OrderService.model.Order;
import com.Microservices.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
