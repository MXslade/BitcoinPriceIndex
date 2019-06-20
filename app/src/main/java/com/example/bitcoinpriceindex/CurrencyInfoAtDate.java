package com.example.bitcoinpriceindex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyInfoAtDate {

    private Currency currency;
    private String date;
    private Double rate;

    public CurrencyInfoAtDate(Currency currency, String date, Double rate) {
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Date getDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDateAsString() {
        return date;
    }

    public Double getRate() {
        return rate;
    }
}
