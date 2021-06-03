package com.spring.mybucks.controller;

import com.spring.mybucks.controller.request.NewCoffeeRequest;
import com.spring.mybucks.model.Coffee;
import com.spring.mybucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/coffee")
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;
    @GetMapping("/all")
    public List<Coffee> findAll(){
        return coffeeService.findAll();
    }
    @GetMapping("/one{name}")
    public Coffee findOne(@PathVariable String name){
        return coffeeService.findOneCoffee(name).get();
    }
    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffeeForJson(@Valid @RequestBody NewCoffeeRequest coffeeRequest){
        return coffeeService.createCoffee(coffeeRequest.getName(),coffeeRequest.getPrice());
    }

    @GetMapping(path = "/", params = "name")
    public Coffee getByName(@RequestParam String name) {
        return coffeeService.findOneCoffee(name).get();
    }

    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffee(@Valid NewCoffeeRequest coffeeRequest){
        return coffeeService.createCoffee(coffeeRequest.getName(),coffeeRequest.getPrice());
    }
}
