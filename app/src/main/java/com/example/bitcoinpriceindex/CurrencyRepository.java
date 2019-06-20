package com.example.bitcoinpriceindex;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrencyRepository {

    private final String USD_URL = "https://api.coindesk.com/v1/bpi/currentprice/USD.json";
    private final String EUR_URL = "https://api.coindesk.com/v1/bpi/currentprice/EUR.json";
    private final String KZT_URL = "https://api.coindesk.com/v1/bpi/currentprice/KZT.json";
    private final String TRANSACTIONS_URL = "https://www.bitstamp.net/api/v2/transactions/btcusd/";

    private NetworkAsyncTask networkAsyncTask;

    @TargetApi(Build.VERSION_CODES.O)
    private String[] defineStartANdEndDates(AmounOfTime amounOfTime) {
        String[] dates = new String[2];
        LocalDateTime currentTime = LocalDateTime.now();
        int year = currentTime.getYear();
        int month = currentTime.getMonth().ordinal() + 1;
        int day = currentTime.getDayOfMonth();
        int[] pre_res = DateFinder.findDateBefore(year, month, day, 1);
        year = pre_res[0];
        month = pre_res[1];
        day = pre_res[2];
        dates[1] = year + "-"
                + (month > 9 ? month : "0" + month) + "-"
                + (day > 9 ? day : "0" + day);

        int[] startDates;
        switch (amounOfTime) {
            case WEEK:
                startDates = DateFinder.findDateBefore(year, month, day, 7);
                break;
            case MONTH:
                startDates = DateFinder.findDateBefore(year, month, day, 31);
                break;
            case YEAR:
                startDates = DateFinder.findDateBefore(year, month, day, 366);
                break;
            default:
                startDates = DateFinder.findDateBefore(year, month, day, 7);
                break;
        }
        dates[0] = startDates[0] + "-"
                + (startDates[1] > 9 ? startDates[1] : "0" + startDates[1]) + "-"
                + (startDates[2] > 9 ? startDates[2] : "0" + startDates[2]);
        return dates;
    }

    public CurrencyRepository() {
        networkAsyncTask = new NetworkAsyncTask();
    }

    public Double getCurrencyExchangeInfo(Currency currency) {
        String currentCurrency;
        String currentCurrencyUrl;
        switch (currency) {
            case USD:
                currentCurrency = "USD";
                currentCurrencyUrl = USD_URL;
                break;
            case EUR:
                currentCurrency = "EUR";
                currentCurrencyUrl = EUR_URL;
                break;
            case KZT:
                currentCurrency = "KZT";
                currentCurrencyUrl = KZT_URL;
                break;
            default:
                currentCurrency = "USD";
                currentCurrencyUrl = USD_URL;
                    break;
        }
        try {
            networkAsyncTask.execute(currentCurrencyUrl);
            JSONObject jsonObject = networkAsyncTask.get();
            return jsonObject.getJSONObject("bpi").getJSONObject(currentCurrency).getDouble("rate_float");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CurrencyInfoAtDate> getCurrencyInfoForLast(AmounOfTime amounOfTime, Currency currency) {
        String currentCurrency;
        String startDate;
        String endDate;
        switch (currency) {
            case USD:
                currentCurrency = "USD";
                break;
            case EUR:
                currentCurrency = "EUR";
                break;
            case KZT:
                currentCurrency = "KZT";
                break;
            default:
                currentCurrency = "USD";
                break;
        }
        String[] dates = defineStartANdEndDates(amounOfTime);
        startDate = dates[0];
        endDate = dates[1];
        try {
            networkAsyncTask.execute("https://api.coindesk.com/v1/bpi/historical/close.json?currency=" + currentCurrency + "&start=" + startDate + "&end=" + endDate);
            JSONObject jsonObject = networkAsyncTask.get();
            jsonObject = jsonObject.getJSONObject("bpi");
            List<CurrencyInfoAtDate> result = new ArrayList<>();
            while (!startDate.equals(endDate)) {
                //Log.d("mLogs", startDate + " " + Double.toString(jsonObject.getDouble(startDate)));
                result.add(new CurrencyInfoAtDate(currency, startDate, jsonObject.getDouble(startDate)));
                startDate = DateFinder.findNextDay(startDate);
            }
            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
