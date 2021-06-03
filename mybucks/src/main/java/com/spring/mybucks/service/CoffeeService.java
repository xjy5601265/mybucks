package com.spring.mybucks.service;

import com.spring.mybucks.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CoffeeService {
    private static final String CACHE = "mybucks-coffee";

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisTemplate<String,Coffee> redisTemplate;

    public Optional<Coffee> findOneCoffee(String name){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //先查redis缓存
        HashOperations<String,String,Coffee> redisHash = redisTemplate.opsForHash();
        if(redisTemplate.hasKey(CACHE) && redisHash.hasKey(CACHE,name)){
            log.info("Get coffee from Redis. {}", redisHash.get(CACHE, name));
            return Optional.of(redisHash.get(CACHE, name));
        }else{
            //缓存没有，查询mongodb 放入缓存
            List<Coffee> coffeeList = mongoTemplate.find(Query.query(Criteria.where("name").is(name)),Coffee.class);
            if(null != coffeeList && coffeeList.size() > 0){
                log.info("Get coffee from mongo:{}",coffeeList.get(0));
                redisHash.put(CACHE,name,coffeeList.get(0));
                redisTemplate.expire(CACHE,5, TimeUnit.MINUTES);
                return  Optional.of(coffeeList.get(0));
            }else{
                return Optional.empty();
            }
        }
    }

    public void initCoffeeMenu(){
        Coffee coffee1 = Coffee.builder().name("mocha").price(Money.of(CurrencyUnit.of("CNY"),20)).createTime(new Date()).updateTime(new Date()).build();
        Coffee coffee2 = Coffee.builder().name("latte").price(Money.of(CurrencyUnit.of("CNY"),30)).createTime(new Date()).updateTime(new Date()).build();
        Coffee coffee3 = Coffee.builder().name("expresso").price(Money.of(CurrencyUnit.of("CNY"),40)).createTime(new Date()).updateTime(new Date()).build();
        Coffee coffee4 = Coffee.builder().name("kabuqinuo").price(Money.of(CurrencyUnit.of("CNY"),50)).createTime(new Date()).updateTime(new Date()).build();
        coffee1 = mongoTemplate.save(coffee1);
        log.info("Coffee1 save:{}",coffee1);
        coffee2 = mongoTemplate.save(coffee2);
        log.info("Coffee2 save:{}",coffee2);
        coffee3 = mongoTemplate.save(coffee3);
        log.info("Coffee3 save:{}",coffee3);
        coffee4 = mongoTemplate.save(coffee4);
        log.info("Coffee4 save:{}",coffee4);
    }

    public Coffee createCoffee(String name,Money money){
        Coffee coffee1 = Coffee.builder().name(name).price(money).createTime(new Date()).updateTime(new Date()).build();
        coffee1 = mongoTemplate.save(coffee1);
        log.info("Coffee save:{}",coffee1);
        return coffee1;
    }

    public List<Coffee> findAll(){
        //先查redis缓存
        HashOperations<String,String,Coffee> redisHash = redisTemplate.opsForHash();
        if(redisTemplate.hasKey(CACHE)){
            return redisHash.values(CACHE);
        }else{
            //缓存没有，查询mongodb 放入缓存
            List<Coffee> coffeeList = mongoTemplate.findAll(Coffee.class);
//            List<Coffee> coffeeList = mongoTemplate.find(Query.query(Criteria.where("name").is(name)),Coffee.class);
            if(null != coffeeList && coffeeList.size() > 0){
                log.info("Get coffee from mongo:{}",coffeeList.toString());
                coffeeList.stream()
                        .forEach(c -> redisHash.put(CACHE,c.getName(),c));
                redisTemplate.expire(CACHE,5, TimeUnit.MINUTES);
                return  coffeeList;
            }else{
                return new ArrayList<Coffee>();
            }
        }
    }
    public List<Coffee> getCoffeeByName(List<String> names) {
        ArrayList<Coffee> results = new ArrayList<>();
        names.stream().forEach(n -> results.add(findOneCoffee(n).get()));
        return results;
    }
}
