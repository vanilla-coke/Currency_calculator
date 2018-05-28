package com.example.darrenpankoff.currencyconverter.feature;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    /*
        Represents the amount of currency to be calculated
     */
    private double selected_currency = 0.0;

    /*
        represent the values of the exchange rates (e.g. CAD to USD, CAD to EUR)
        These change based on the selected countries
     */
    private double second_currency = 0.0;
    private double third_currency = 0.0;

    /*
        this variable simply keeps track of whether the user wants to calculate a decimal value
     */
    private boolean decimalClicked = false;

    /*
        total_currency simply keeps track of the total amount the user wants to calculate an exchange rate for
     */

    private String total_currency = "";
    private String currency_to_currency2 = "";
    private String currency_to_currency3 = "";


    /*
        The strings below will simply keep track of the country currencies the user is wanting to compare
        The first country is the one being compared (e.g. CAD) and the rest will be the two countries to compare (e.g. USD, EUR)
        These will change when a user selects a new country from the spinner list

     */

    private String country1_code = "CAD";
    private String country2_code = "USD";
    private String country3_code = "EUR";


    /*
        HashMap contains the country code as well as a description of the currency e.g. <CAD, Canadian Dollars>
     */

    HashMap<String, String> countries_and_descriptions = new HashMap<>(); // perhaps use Data.java to read in file and create this


    /*
        These are our spinners, they are final because the content inside will not change.
     */

    private final Spinner spinner1 = (Spinner) findViewById(R.id.currency_spinner1);
    private final Spinner spinner2 = (Spinner) findViewById(R.id.currency_spinner2);
    private final Spinner spinner3 = (Spinner) findViewById(R.id.currency_spinner3);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetCountries api_access = new GetCountries(); // use this to make async calls

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countryNames, android.R.layout.simple_list_item_1);
        spinner1.setAdapter(adapter); // set the values of the spinner from strings.xml - countryNames
        spinner1.setSelection(0); // set the default spinner values
        spinner2.setAdapter(adapter);
        spinner2.setSelection(1);
        spinner3.setAdapter(adapter);
        spinner3.setSelection(2);

        /*
            Here we get the default exchange values that will be used when application is initialized

         */
        second_currency = Double.parseDouble(api_access.doInBackground(country1_code, country2_code));
        third_currency = Double.parseDouble(api_access.doInBackground(country1_code, country3_code));

    }



    /*
        Below is simply an AsyncTask that will utilize the alpha advantageAPI in order to obtain currency exchange rates.
        The countries are the ones passed into the params object in doInBackground. It returns a string to the caller.
    */

    static private class GetCountries extends AsyncTask<String, Void, String> {

        private String api_base_url = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE";
        private final String key = "API KEY";
        private String api_results = "";


        @Override
        protected String doInBackground(String... params){

            try{
                URL api = new URL(api_base_url + "&from_currency=" + params[0] + "&to_currency=" + params[1] + "&apikey=" + key);
                HttpsURLConnection urlConnection = (HttpsURLConnection) api.openConnection();
                try{
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder my_string = new StringBuilder();
                    String current_line;
                    while((current_line = bufferedReader.readLine())!= null){
                        my_string.append(current_line).append("\n");
                    }
                    bufferedReader.close();
                    this.onPostExecute(my_string.toString());
                    return api_results; // return the results to the caller


                }finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e){
                Log.e("ERROR DETECTED", e.getMessage(), e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response){

            if (response == null){
                response = "ERROR OCCURRED FETCHING DATA";
            }

            try{
                JSONObject obj = new JSONObject(response);

                JSONArray results = new JSONArray(obj.getString("Realtime Currency Exchange Rate"));

                JSONObject exchange_rate = results.getJSONObject(4);

                api_results = exchange_rate.getString("5. Exchange Rate");

                Log.i("RESULTS", response);

            }catch (JSONException e){
                Log.i("ERROR PARSING JSON", e.toString());

            }

        }


    }


    /*
        This code below is for the calculator portion of the code.
        Based on which number or action button is selected, the appropriate changes will be made to the display
        Note: we will keep the length of the total currency to 14 digits (10 + 3 decimal places)

        resource ID's cannot be used in a switch statement therefore needed to use if else
     */

    public void CalcButtonClick(View v) {
        int result = v.getId();
        if (total_currency.length() <= 14) {
            if (result == R.id.num0) {
                if (selected_currency != 0 && !decimalClicked) {
                    total_currency += "0";
                    selected_currency *= 10;
                    display();
                }
                // note if the decimal is clicked, adding a zero does not change the value therefore do nothing.
            } else if (result == R.id.num1) {
                if (decimalClicked) {
                    total_currency += "1";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num2) {
                if (decimalClicked) {
                    total_currency += "2";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num3) {
                if (decimalClicked) {
                    total_currency += "3";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num4) {
                if (decimalClicked) {
                    total_currency += "4";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num5) {
                if (decimalClicked) {
                    total_currency += "5";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num6) {
                if (decimalClicked) {
                    total_currency += "6";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num7) {
                if (decimalClicked) {
                    total_currency += "7";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num8) {
                if (decimalClicked) {
                    total_currency += "8";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.num9) {
                if (decimalClicked) {
                    total_currency += "9";
                    selected_currency = Double.parseDouble(total_currency);
                    display();

                } else {
                    selected_currency++;
                    display();

                }
            } else if (result == R.id.decimal) {
                decimalClicked = !decimalClicked;

            } else if (result == R.id.clear) {
                selected_currency = 0.0;
                total_currency = "";

            } else if (result == R.id.deletePrev) {
                if (selected_currency != 0.0) {
                    total_currency = total_currency.substring(0, total_currency.length() - 1);
                    selected_currency = Double.parseDouble(total_currency);
                }
            }
        }
    }


    public void display() {

        /*
            Here we simply convert the currency rates and then format them to strings with 3 decimal places
         */

        total_currency = String.format("%.3f", selected_currency);

        Double tmp = selected_currency * second_currency;
        currency_to_currency2 = String.format("%.3f", tmp.toString());


        tmp = selected_currency * third_currency;
        currency_to_currency3 = String.format("%.3f", tmp.toString());


        /*
            We set the calculator view of all three currencies to display the proper currency conversion amounts

            e.g. 1 CAD to USD at 0.75% = $0.75
         */

        TextView current = (TextView) findViewById(R.id.first_currency);
        current.setText(total_currency);


        TextView secondCurrency = (TextView) findViewById(R.id.second_currency);
        secondCurrency.setText(currency_to_currency2);


        TextView thirdCurrency = (TextView) findViewById(R.id.third_currency);
        thirdCurrency.setText(currency_to_currency3);

        /*
                Here we set the text below the currency amounts to display the proper country description
                e.g. CAD = Canadian Dollars
         */

        TextView currentDesc = (TextView) findViewById(R.id.first_currency_desc);
        currentDesc.setText(country1_code);


        TextView secondCurrencyDesc = (TextView) findViewById(R.id.second_currency_desc);
        secondCurrencyDesc.setText(country2_code);


        TextView thirdCurrencyDesc = (TextView) findViewById(R.id.third_currency_desc);
        thirdCurrencyDesc.setText(country3_code);
    }


}
