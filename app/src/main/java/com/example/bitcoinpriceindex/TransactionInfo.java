package com.example.bitcoinpriceindex;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionInfo {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.####");

    private String date;
    private long transactionID;
    private double BTCPrice;
    private double BTCAmount;
    private boolean type; //false-buy true-sell

    private SimpleDateFormat sdf;

    public void setDate(long date) {
        Date d = new Date(date * 1000);
        this.date = sdf.format(d);
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public void setBTCPrice(double BTCPrice) {
        this.BTCPrice = BTCPrice;
    }

    public void setBTCAmount(double BTCAmount) {
        this.BTCAmount = BTCAmount;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public TransactionInfo(long date, long transactionID, double BTCPrice, double BTCAmount, boolean type) {
        sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        setDate(date);
        this.transactionID = transactionID;
        this.BTCPrice = BTCPrice;
        this.BTCAmount = BTCAmount;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public long getTransactionID() {
        return transactionID;
    }

    public double getBTCPrice() {
        return BTCPrice;
    }

    public double getBTCAmount() {
        return BTCAmount;
    }

    public String getType() {
        return (type ? "sell" : "buy");
    }

    public String getSum() {
        return formatDouble(BTCAmount * BTCPrice);
    }

    public static String formatString(String s) {
        return s.substring(1, s.length() - 1);
    }

    public static String formatDouble(double d) {
        String s = decimalFormat.format(d);
        if (s.contains(",")) {
            s = s.replace(',', '.');
        }
        return s;
    }

}
