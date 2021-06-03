package com.spring.mybucks.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "Coffee")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    @Id
    private String id;
    private Date createTime;
    private Date updateTime;
    private String name;
    private Money price;
}
