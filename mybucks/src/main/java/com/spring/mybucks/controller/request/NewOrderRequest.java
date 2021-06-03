package com.spring.mybucks.controller.request;

import com.spring.mybucks.model.Coffee;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class NewOrderRequest {
    @NotNull
    private String customer;
    @NotNull
    private List<String> coffeeList;
}
