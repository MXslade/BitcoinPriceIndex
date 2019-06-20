package com.example.bitcoinpriceindex;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class RatesFragment extends Fragment {

    private View v;
    private TextView usdTextView;
    private TextView eurTextView;
    private TextView kztTextView;
    private TextView exchangeRateTextView;
    private GraphView graphView;
    private RadioButton weekRadioButton;
    private RadioButton monthRadioButton;
    private RadioButton yearRadioButton;

    private List<CurrencyInfoAtDate> graphList;
    private CurrencyRepository currencyRepository;

    private AmounOfTime chosenAmountOfTime = AmounOfTime.WEEK;
    private Currency chosenCurrency = Currency.USD;

    private void initControls() {
        initCurrenciesControls();
        initRadioGroup();
        initGraphView();
        currencyRepository = new CurrencyRepository();
    }

    private void initCurrenciesControls() {
        usdTextView = (TextView) v.findViewById(R.id.usdTextView);
        usdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCurrency = Currency.USD;
                updateData();
            }
        });
        eurTextView = (TextView) v.findViewById(R.id.eurTextView);
        eurTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCurrency = Currency.EUR;
                updateData();
            }
        });
        kztTextView = (TextView) v.findViewById(R.id.kztTextView);
        kztTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCurrency = Currency.KZT;
                updateData();
            }
        });
        exchangeRateTextView = (TextView) v.findViewById(R.id.exchangeRateTextView);
    }

    private void initRadioGroup() {
        weekRadioButton = (RadioButton) v.findViewById(R.id.weekRadioButton);
        weekRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAmountOfTime = AmounOfTime.WEEK;
                updateData();
            }
        });
        monthRadioButton = (RadioButton) v.findViewById(R.id.monthRadioButton);
        monthRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAmountOfTime = AmounOfTime.MONTH;
                updateData();
            }
        });
        yearRadioButton = (RadioButton) v.findViewById(R.id.yearRadioButton);
        yearRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAmountOfTime = AmounOfTime.YEAR;
                updateData();
            }
        });
    }

    private void initGraphView() {
        graphView = (GraphView) v.findViewById(R.id.graphView);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphView.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graphView.getGridLabelRenderer().setNumVerticalLabels(5);
        // activate horizontal zooming and scrolling
        graphView.getViewport().setScalable(true);
        // activate horizontal scrolling
        graphView.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        graphView.getViewport().setScalableY(true);
        // activate vertical scrolling
        graphView.getViewport().setScrollableY(true);
    }

    private void updateData() {
        updateControls();
        currencyRepository = new CurrencyRepository();
        exchangeRateTextView.setText(currencyRepository.getCurrencyExchangeInfo(chosenCurrency).toString());
        buildGraph(chosenAmountOfTime, chosenCurrency);
    }

    private void updateControls() {
        switch (chosenCurrency) {
            case USD:
                usdTextView.setTextColor(Color.RED);
                eurTextView.setTextColor(Color.BLACK);
                kztTextView.setTextColor(Color.BLACK);
                break;
            case EUR:
                usdTextView.setTextColor(Color.BLACK);
                eurTextView.setTextColor(Color.RED);
                kztTextView.setTextColor(Color.BLACK);
                break;
            case KZT:
                usdTextView.setTextColor(Color.BLACK);
                eurTextView.setTextColor(Color.BLACK);
                kztTextView.setTextColor(Color.RED);
                break;
        }
    }

    private void buildGraph(AmounOfTime amounOfTime, Currency currency) {
        graphView.removeAllSeries();
        currencyRepository = new CurrencyRepository();
        graphList = currencyRepository.getCurrencyInfoForLast(amounOfTime, currency);
        approximateData(amounOfTime);
        DataPoint[] dataPoints = new DataPoint[graphList.size()];
        for (int i = 0; i < dataPoints.length; ++i) {
            dataPoints[i] = new DataPoint(graphList.get(i).getDate(), graphList.get(i).getRate());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graphView.addSeries(series);
        graphView.getViewport().setMinX(graphList.get(0).getDate().getTime());
        graphView.getViewport().setMaxX(graphList.get(graphList.size() - 1).getDate().getTime());
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        if (chosenAmountOfTime == AmounOfTime.YEAR) {
            graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        } else {
            graphView.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        }
    }

    private void approximateData(AmounOfTime amounOfTime) {
        if (amounOfTime == AmounOfTime.WEEK) {
        } else if (amounOfTime == AmounOfTime.MONTH) {
            List<CurrencyInfoAtDate> list = new ArrayList<>();
            int counter = 0;
            double sum = 0;
            while (counter < graphList.size()) {
                sum += graphList.get(counter).getRate();
                ++counter;
                if (counter % 7 == 0) {
                    list.add(new CurrencyInfoAtDate(chosenCurrency, graphList.get(counter - 1).getDateAsString(), sum / 7));
                    sum = 0;
                }
            }
            list.add(new CurrencyInfoAtDate(chosenCurrency, graphList.get(counter - 1).getDateAsString(), sum / (counter % 7)));
            graphList = list;
        } else if (amounOfTime == AmounOfTime.YEAR) {
            List<CurrencyInfoAtDate> list = new ArrayList<>();
            int counter = 0;
            double sum = 0;
            while (counter < graphList.size()) {
                sum += graphList.get(counter).getRate();
                ++counter;
                if (counter % 31 == 0) {
                    list.add(new CurrencyInfoAtDate(chosenCurrency, graphList.get(counter - 1).getDateAsString(), sum / 31));
                    sum = 0;
                }
            }
            list.add(new CurrencyInfoAtDate(chosenCurrency, graphList.get(counter - 1).getDateAsString(), sum / (counter % 31)));
            graphList = list;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.rates_fragment, null);
        initControls();
        updateData();
        return v;
    }
}
