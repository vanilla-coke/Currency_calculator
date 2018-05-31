package com.example.darrenpankoff.currencyconverter.feature;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    /*
        represent the values of the exchange rates (e.g. CAD to USD, CAD to EUR)
        These change based on the selected countries
     */
    private double second_currency_rate = 0;
    private double third_currency_rate = 0;

    /*
        this variable simply keeps track of whether the user wants to calculate a decimal value
     */
    private boolean decimalClicked = false;

    /*
        total_currency simply keeps track of the total amount the user wants to calculate an exchange rate for

     */
    private String total_currency = "0";

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

    HashMap<String, String> countries_and_descriptions = new HashMap<>(); // perhaps read in text file


    /*
        These are the spinners where users can select which country they want the exchange rates for
     */

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    // count is simply a flag variable used to determine if this is the program loading for the first time or whether a change has been made
    // it is used for the spinners

    private boolean first_time = true;

    ArrayAdapter<CharSequence> adapter_for_countryCodes;
    ArrayAdapter<CharSequence> adapter_for_countryDescriptions; // contains list of items in spinners

    GetCountries api_access; // use this to make async API calls


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        spinner1 = (Spinner) findViewById(R.id.currency_spinner1);
        spinner2 = (Spinner) findViewById(R.id.currency_spinner2);
        spinner3 = (Spinner) findViewById(R.id.currency_spinner3);

        adapter_for_countryCodes = ArrayAdapter.createFromResource(this, R.array.countryNames, android.R.layout.simple_list_item_1);
        adapter_for_countryDescriptions = ArrayAdapter.createFromResource(this, R.array.countryDescriptions, android.R.layout.simple_list_item_1);
        adapter_for_countryDescriptions.setDropDownViewResource(R.layout.spinner_dropdown); // set the layout of the spinner dropdown menu

        api_access = new GetCountries(); // create object for doing Async API calls

        spinner1.setAdapter(adapter_for_countryDescriptions); // set the values of the spinner from strings.xml - countryNames
        spinner2.setAdapter(adapter_for_countryDescriptions);
        spinner3.setAdapter(adapter_for_countryDescriptions);

        for (int i = 0; i < adapter_for_countryCodes.getCount(); i++) {
            countries_and_descriptions.put(adapter_for_countryDescriptions.getItem(i).toString(), adapter_for_countryCodes.getItem(i).toString());
        }

        second_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country2_code));
        third_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country3_code));





        /*
            Below we want to handle the user selecting a new country from the spinner list.
            We also deal with setting the initial spinner values upon program initialization
         */

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!first_time) {

                    // user has selected new country
                    country1_code = countries_and_descriptions.get(spinner1.getSelectedItem().toString()); // change the country code
                    TextView first_country = (TextView) findViewById(R.id.first_currency_desc);
                    first_country.setText(country1_code);

                    // check if 1 vs 2 are same countries, and 1 vs 3 are same. If they are the exchange rate is just equal i.e. 1:1
                    if (country1_code.equals(country2_code)) {
                        second_currency_rate = 1.0;
                    } else {
                        second_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country2_code));
                    }

                    if (country1_code.equals(country3_code)) {
                        third_currency_rate = 1.0;

                    } else {
                        third_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country3_code));
                    }
                    display();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do not do anything
            }
        });



        /*
            same as above
         */

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!first_time){

                    country2_code = countries_and_descriptions.get(spinner2.getSelectedItem().toString());
                    TextView second_country = (TextView) findViewById(R.id.second_currency_rate_desc);
                    second_country.setText(country2_code);


                    if (country1_code.equals(country2_code)) {
                        second_currency_rate = 1.0;
                    } else {
                        second_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country2_code));
                    }
                    display();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do not do anything
            }

        });

        /*
            same as above
         */
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (first_time) {
                    first_time = false; // set flag to false

                } else {
                    country3_code = countries_and_descriptions.get(spinner3.getSelectedItem().toString());
                    TextView third_country = (TextView) findViewById(R.id.third_currency_rate_desc);
                    third_country.setText(country3_code);


                    if (country1_code.equals(country3_code)) {
                        third_currency_rate = 1.0;
                    } else {
                        third_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country3_code));
                    }
                    display();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do not do anything
            }
        });
    }

    /*
        Below is simply an AsyncTask that will utilize the alpha advantageAPI in order to obtain currency exchange rates.
        The countries are the ones passed into the params object in doInBackground. It returns a string to the caller.
    */

    static private class GetCountries extends AsyncTask<String, Void, String> {

        private String api_base_url = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE";
        private final String key = "DBXVG3H0XRDSR4FW"; // ADD YOUR OWN API KEY HERE
        private String api_results = "";


        @Override
        protected String doInBackground(String... params) {

            try {
                URL api = new URL(api_base_url + "&from_currency=" + params[0] + "&to_currency=" + params[1] + "&apikey=" + key);
                HttpsURLConnection urlConnection = (HttpsURLConnection) api.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder my_string = new StringBuilder();
                    String current_line;
                    while ((current_line = bufferedReader.readLine()) != null) {
                        my_string.append(current_line).append("\n");
                    }
                    bufferedReader.close();
                    this.onPostExecute(my_string.toString());
                    return api_results; // return the results to the caller


                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR DETECTED", e.getMessage(), e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {

            if (response == null) {
                api_results = "ERROR OCCURRED FETCHING DATA";
            }

            try {
                JSONObject obj = new JSONObject(response);
                JSONObject r = new JSONObject(obj.getString("Realtime Currency Exchange Rate"));
                api_results = r.getString("5. Exchange Rate");
                Log.i("RESULTS", api_results);

            } catch (JSONException e) {
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
        if (result == R.id.num0) {
            if (!decimalClicked) {
                total_currency += "0";
            }
            // note if the decimal is clicked, adding a zero does not change the value therefore do nothing.
        } else if (result == R.id.num1) {
            total_currency += "1";
        } else if (result == R.id.num2) {
            total_currency += "2";
        } else if (result == R.id.num3) {
            total_currency += "3";
        } else if (result == R.id.num4) {
            total_currency += "4";
        } else if (result == R.id.num5) {
            total_currency += "5";
        } else if (result == R.id.num6) {
            total_currency += "6";
        } else if (result == R.id.num7) {
            total_currency += "7";
        } else if (result == R.id.num8) {
            total_currency += "8";
        } else if (result == R.id.num9) {
            total_currency += "9";
        } else if (result == R.id.decimal) {
            if (!decimalClicked) {
                total_currency += ".";
                decimalClicked = true;
            }
        } else if (result == R.id.clear) {
            total_currency = "0";
        } else if (result == R.id.deletePrev ) {
            if (total_currency.length() > 0) {
                total_currency = total_currency.substring(0, total_currency.length() - 1);
                if (total_currency.length() > 0 && total_currency.charAt(total_currency.length() - 1) == '.') {
                    total_currency = total_currency.substring(0, total_currency.length() - 1);
                    // this is the case where we deleted a decimal value e.g. 1.3 (remove the 3) we would be left with 1.
                    // therefore we need to delete the extra decimal.
                    decimalClicked = false;
                }
            }
        }

        display(); // no matter what we want to call display;
    }


    public void display() {

        // want to keep the length of the double < 14 integers
        if (total_currency.length() > 14) {
            total_currency = total_currency.substring(0, total_currency.length() - 1);
        }
        //check if # begins with 0 i.e. we don't want to display 0343 we just want 343 however 0.343 is okay
        if(total_currency.length() > 1 && total_currency.charAt(0) == '0' ){
            if(total_currency.charAt(1) != '.'){
                total_currency = total_currency.substring(1);
            }
        }

        /*
            Here we simply convert the currency rates and then format them to strings with 3 decimal places

         */

        if (total_currency.length() == 0) {
            total_currency = "0"; // ensures that if the user hits the DEL button and removes all integers, the number reverts back to 0
        }

        Double temp = Double.parseDouble(total_currency);
        temp *= second_currency_rate;
        String currency1_to_currency2 = temp.toString();

        temp = Double.parseDouble(total_currency);
        temp *= third_currency_rate;
        String currency1_to_currency3 = temp.toString();



        /*
            We set the calculator view of all three currencies to display the proper currency conversion amounts

            e.g. 1 CAD to USD at 0.75% = $0.75
         */

        TextView current = (TextView) findViewById(R.id.first_currency);
        current.setText(total_currency);

        TextView secondCurrency = (TextView) findViewById(R.id.second_currency_rate);
        secondCurrency.setText(currency1_to_currency2);

        TextView thirdCurrency = (TextView) findViewById(R.id.third_currency_rate);
        thirdCurrency.setText(currency1_to_currency3);

    }
}
