package com.spring.mybucks.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class NewCoffeeRequest {
    @NotNull(message = "name不能为空")
    private String name;
    @NotNull
    private Money price;
}
