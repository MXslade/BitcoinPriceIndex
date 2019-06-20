package com.example.bitcoinpriceindex;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private RatesFragment ratesFragment;
    private TransactionsFragment transactionsFragment;
    private ConverterFragment converterFragment;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView bottomNavigationView;

    private void initControls() {
        ratesFragment = new RatesFragment();
        transactionsFragment = new TransactionsFragment();
        converterFragment = new ConverterFragment();
        changeFragmentTransaction(ratesFragment);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        initBottomNavigationViewListener();
    }

    private void changeFragmentTransaction(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(ratesFragment);
        fragmentTransaction.remove(transactionsFragment);
        fragmentTransaction.remove(converterFragment);
        fragmentTransaction.add(R.id.fragmentContainerFrameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initBottomNavigationViewListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ratesItem :
                        changeFragmentTransaction(ratesFragment);
                        break;
                    case R.id.transactionsItem :
                        changeFragmentTransaction(transactionsFragment);
                        break;
                    case R.id.converterItem:
                        changeFragmentTransaction(converterFragment);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    @Override
    public void onBackPressed() {}
}
