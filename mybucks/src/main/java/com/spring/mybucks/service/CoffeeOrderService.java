package com.spring.mybucks.service;

import com.mongodb.client.result.UpdateResult;
import com.spring.mybucks.model.Coffee;
import com.spring.mybucks.model.CoffeeOrder;
import com.spring.mybucks.model.OrderState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Service
public class CoffeeOrderService {
    private static final String CACHE = "mybucks-coffeeorder";
    @Autowired
    private MongoTemplate mongoTemplate;
    public CoffeeOrder createOrder(String customer,Coffee... coffee){
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                                .customer(customer)
                                .items(Arrays.asList(coffee))
                                .state(OrderState.INIT)
                                .createTime(new Date())
                                .updateTime(new Date())
                                .build();
        CoffeeOrder saved = mongoTemplate.save(coffeeOrder);
        log.info("New Order: {}", saved);
        return saved;
    }

    public boolean updateState(CoffeeOrder coffeeOrder, OrderState state){
        if(state.compareTo(coffeeOrder.getState()) <= 0){
            log.warn("Wrong State order: {}, {}", state, coffeeOrder.getState());
            return false;
        }
        UpdateResult result = mongoTemplate.updateFirst(query(where("id").is(coffeeOrder.getId())),
                new Update().set("state", state).currentDate("updateTime"),Coffee.class);
        log.info("Update Result: {}", result.getModifiedCount());
        log.info("Updated Order: {}", coffeeOrder);
        return true;
    }

    public List<CoffeeOrder> findOrderByCustomer(String customer){
        List<CoffeeOrder> orderList = mongoTemplate.find(Query.query(Criteria.where("customer").is(customer)),CoffeeOrder.class);
        if(null != orderList && orderList.size() > 0){
            log.info("orderList find:{}",orderList);
            return  orderList;
        }else{
            return null;
        }
    }
}
