package com.example.darrenpankoff.currencyconverter.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.darrenpankoff.currencyconverter.feature.Data.Countries;


public class MainActivity extends AppCompatActivity {

    private double selected_currency = 0.0;
    private double second_currency = 0.0;
    private double third_currency = 0.0;

    private boolean decimalClicked = false;

    private String total_currency1 = "";
    private String total_currency2 = "";
    private String total_currency3 = "";

    private String total_currency1_description = "";
    private String total_currency2_description = "";
    private String total_currency3_description = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Countries api_access = new Countries(); // use this to make async calls

        Spinner spinner1 = (Spinner) findViewById(R.id.currency_spinner1);
        Spinner spinner2 = (Spinner) findViewById(R.id.currency_spinner2);
        Spinner spinner3 = (Spinner) findViewById(R.id.currency_spinner3);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countryNames, android.R.layout.simple_list_item_1);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

    }




    // resource ID's cannot be used in a switch statement therefore needed to use if else

    public void CalcButtonClick(View v){
        int result = v.getId();
        if(result == R.id.num0){
            if(selected_currency != 0 && !decimalClicked){
                selected_currency *= 10;
                display();
            }
        }
        else if (result == R.id.num1){
            if(decimalClicked){
                total_currency1 += "1";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num2){
            if(decimalClicked){
                total_currency1 += "2";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num3){
            if(decimalClicked){
                total_currency1 += "3";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num4){
            if(decimalClicked){
                total_currency1 += "4";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num5){
            if(decimalClicked){
                total_currency1 += "5";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num6){
            if(decimalClicked){
                total_currency1 += "6";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num7){
            if(decimalClicked){
                total_currency1 += "7";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num8){
            if(decimalClicked){
                total_currency1 += "8";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }
        else if (result == R.id.num9){
            if(decimalClicked){
                total_currency1 += "9";
                selected_currency = Double.parseDouble(total_currency1);
                display();

            }else{
                selected_currency++;
                display();

            }
        }else if (result == R.id.decimal) {
            decimalClicked = !decimalClicked;
        }
        else if(result == R.id.clear) {
            selected_currency = 0.0;
            second_currency = 0.0;
            third_currency = 0.0;
            total_currency1 = "";
            total_currency2 = "";
            total_currency3 = "";
        }
        else if(result == R.id.deletePrev){
            if(selected_currency != 0.0){
                total_currency1 = total_currency1.substring(0, total_currency1.length()-1);
                selected_currency = Double.parseDouble(total_currency1);
            }
        }
    }


    public void display(){


        second_currency *= selected_currency;
        third_currency *= selected_currency;

        total_currency1 = String.format("%.3f", selected_currency);
        total_currency2 = String.format("%.3f", second_currency);
        total_currency3 = String.format("%.3f", third_currency);


        TextView current = (TextView)findViewById(R.id.first_currency);
        current.setText(total_currency1);


        TextView secondCurrency = (TextView) findViewById(R.id.second_currency);
        secondCurrency.setText(total_currency2);


        TextView thirdCurrency = (TextView) findViewById(R.id.third_currency);
        thirdCurrency.setText(total_currency3);

        TextView currentDesc = (TextView) findViewById(R.id.first_currency_desc);
        currentDesc.setText(total_currency1_description);


        TextView secondCurrencyDesc = (TextView) findViewById(R.id.second_currency_desc);
        secondCurrencyDesc.setText(total_currency2_description);


        TextView thirdCurrencyDesc = (TextView) findViewById(R.id.third_currency_desc);
        thirdCurrencyDesc.setText(total_currency3_description);
    }






}
