package com.spring.mybucks.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "CoffeeOrder")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrder implements Serializable {
    @Id
    private String id;
    private Date createTime;
    private Date updateTime;
    private String customer;
    private List<Coffee> items;
    private OrderState state;
}
