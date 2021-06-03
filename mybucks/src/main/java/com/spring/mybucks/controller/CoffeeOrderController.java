package com.spring.mybucks.controller;

import com.spring.mybucks.controller.request.NewOrderRequest;
import com.spring.mybucks.model.Coffee;
import com.spring.mybucks.model.CoffeeOrder;
import com.spring.mybucks.service.CoffeeOrderService;
import com.spring.mybucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
    @Autowired
    private CoffeeOrderService coffeeOrderService;
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping(path = "/add",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder addCoffeeOrder(@Valid NewOrderRequest orderRequest){
        Coffee[] coffeeList = coffeeService.getCoffeeByName(orderRequest.getCoffeeList()).toArray(new Coffee[] {});
        return coffeeOrderService.createOrder(orderRequest.getCustomer(),coffeeList);
    }
    @GetMapping(path = "/get",params = "name")
    public List<CoffeeOrder> getOrderByCust(@RequestParam String name){
        return coffeeOrderService.findOrderByCustomer(name);
    }

}
