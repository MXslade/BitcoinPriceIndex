package com.example.bitcoinpriceindex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ConverterFragment extends Fragment {

    private View v;
    private EditText converterEditText;
    private ImageView converterImageView;
    private TextView converterTextView;
    private RadioButton usdRadioButton;
    private RadioButton eurRadioButton;
    private RadioButton kztRadioButton;
    private Button convertButton;

    private boolean isCurrencyToBitcoin = true;
    private Currency chosenCurrency = Currency.USD;
    private CurrencyRepository currencyRepository;

    private void initControls() {
        initTextsAndImageView();
        initRadioGroup();
        initButton();
    }

    private void initTextsAndImageView() {
        converterEditText = (EditText) v.findViewById(R.id.converterEditText);
        converterImageView = (ImageView) v.findViewById(R.id.converterImageView);
        converterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCurrencyToBitcoin = !isCurrencyToBitcoin;
                resetTexts();
            }
        });
        converterTextView = (TextView) v.findViewById(R.id.converterTextView);
    }

    private void resetTexts() {
        converterEditText.setText("");
        converterTextView.setText("");
        if (isCurrencyToBitcoin) {
            converterTextView.setHint("Amount of Bitcoins");
        } else {
            converterTextView.setHint("Amount of " + chosenCurrency.toString());
        }
    }

    private void initRadioGroup() {
        usdRadioButton = (RadioButton) v.findViewById(R.id.usdRadioButton);
        usdRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCurrency = Currency.USD;
                resetTexts();
            }
        });
        eurRadioButton = (RadioButton) v.findViewById(R.id.eurRadioButton);
        eurRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCurrency = Currency.EUR;
                resetTexts();
            }
        });
        kztRadioButton = (RadioButton) v.findViewById(R.id.kztRadioButton);
        kztRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCurrency = Currency.KZT;
                resetTexts();
            }
        });
    }

    private void initButton() {
        convertButton = (Button) v.findViewById(R.id.convertButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencyRepository = new CurrencyRepository();
                double rate = currencyRepository.getCurrencyExchangeInfo(chosenCurrency);
                calcExchange(rate);
            }
        });
    }

    private void calcExchange(double rate) {
        if (converterEditText.getText().toString().equals("")) {
            return;
        }
        if (isCurrencyToBitcoin) {
            double amount = Double.parseDouble(converterEditText.getText().toString());
            double amountOfB = amount / rate;
            converterTextView.setText(TransactionInfo.formatDouble(amountOfB));
        } else {
            double amountOfB = Double.parseDouble(converterEditText.getText().toString());
            double amount = amountOfB * rate;
            converterTextView.setText(TransactionInfo.formatDouble(amount));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.converter_fragment, null);
        initControls();
        return v;
    }
}
