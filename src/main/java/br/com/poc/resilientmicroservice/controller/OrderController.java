package br.com.poc.resilientmicroservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/api/order")
    public String findOrder() {
        return "Order found!";
    }
}
