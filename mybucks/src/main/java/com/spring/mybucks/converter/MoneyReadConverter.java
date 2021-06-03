package com.spring.mybucks.converter;

import org.bson.Document;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

public class MoneyReadConverter implements Converter<Document, Money> {
    @Override
    public Money convert(Document source){
        Document document = (Document) source.get("money");
        double amount = Double.parseDouble(document.getString("amount"));
        String currency = ((Document) document.get("currency")).getString("code");
        return Money.of(CurrencyUnit.of(currency),amount);
    }
}
