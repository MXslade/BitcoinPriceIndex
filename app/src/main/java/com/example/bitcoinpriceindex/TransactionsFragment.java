package com.example.bitcoinpriceindex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;

    private ArrayList<TransactionInfo> transactionsInfo = new ArrayList<>();

    private void initRecyclerView() {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadJSON() {
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadTransactionsCall = serviceAPI.readTransactionsArray();

        loadTransactionsCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                for (int i = 0; i < response.body().size(); ++i) {
                    if (i == 500) {
                        break;
                    }
                    JsonObject jsonObject = response.body().get(i).getAsJsonObject();
                    if (transactionsInfo.size() == 500) {
                        transactionsInfo.get(i).setDate(Long.parseLong(TransactionInfo.formatString(jsonObject.get("date").toString())));
                        transactionsInfo.get(i).setTransactionID(Long.parseLong(TransactionInfo.formatString(jsonObject.get("tid").toString())));
                        transactionsInfo.get(i).setBTCPrice(Double.parseDouble(TransactionInfo.formatString(jsonObject.get("price").toString())));
                        transactionsInfo.get(i).setBTCAmount(Double.parseDouble(TransactionInfo.formatString(jsonObject.get("amount").toString())));
                        transactionsInfo.get(i).setType(TransactionInfo.formatString(jsonObject.get("type").toString()).equals("1"));
                    } else {
                        transactionsInfo.add(new TransactionInfo(
                                Long.parseLong(TransactionInfo.formatString(jsonObject.get("date").toString())),
                                Long.parseLong(TransactionInfo.formatString(jsonObject.get("tid").toString())),
                                Double.parseDouble(TransactionInfo.formatString(jsonObject.get("price").toString())),
                                Double.parseDouble(TransactionInfo.formatString(jsonObject.get("amount").toString())),
                                TransactionInfo.formatString(jsonObject.get("type").toString()).equals("1")
                        ));
                    }
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(transactionsInfo));
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {}
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.transactions_fragment, null);
        loadJSON();
        initRecyclerView();
        return v;
    }

}
