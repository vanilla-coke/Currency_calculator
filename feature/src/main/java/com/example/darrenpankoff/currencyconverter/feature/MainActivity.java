package com.example.darrenpankoff.currencyconverter.feature;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private double second_currency_rate = 0.0;
    private double third_currency_rate = 0.0;

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

    HashMap<String, String> countries_and_descriptions = new HashMap<>(); // perhaps read in text file


    /*
        These are our spinners, they are final because the content inside will not change.
     */

    private  Spinner spinner1 = (Spinner) findViewById(R.id.currency_spinner1);
    private  Spinner spinner2 = (Spinner) findViewById(R.id.currency_spinner2);
    private  Spinner spinner3 = (Spinner) findViewById(R.id.currency_spinner3);

    // count is simply a flag variable used to determine if this is the program loading for the first time or whether a change has been made
    // it is used for the spinners

    private boolean first_time = true;

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countryNames, android.R.layout.simple_list_item_1);
    // contains list of items in spinners

    GetCountries api_access = new GetCountries(); // use this to make async API calls


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinner1.setAdapter(adapter); // set the values of the spinner from strings.xml - countryNames
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

        /*
            Below we want to handle the user selecting a new country from the spinner list.
            We also deal with setting the initial spinner values upon program initialization
         */

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(first_time){
                    spinner1.setSelection(0); // set the default spinner values NOTE: only need to do this once upon initialization
                    spinner2.setSelection(1);
                    spinner3.setSelection(2);
                    first_time = !first_time; // set flag to false


                }else{
                    country1_code = spinner1.getSelectedItem().toString(); // change the country code
                    TextView first_country = (TextView) findViewById(R.id.first_currency_desc);
                    first_country.setText(country1_code); // NEED HASHMAP

                    // check if 1 vs 2 are same countries, and 1 vs 3 are same. If they are the exchange rate is just equal i.e. 1:1
                    if(country1_code.equals(country2_code)){
                        second_currency_rate = 1.0;
                    } else if(country1_code.equals(country3_code)){
                        third_currency_rate = 1.0;

                    }else{
                        second_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country2_code));
                        third_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country3_code));
                    }
                }
                display();

                }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do not do anything
            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                country2_code = spinner2.getSelectedItem().toString();
                TextView second_country = (TextView) findViewById(R.id.second_currency_rate_desc);
                second_country.setText(country2_code); //HASHMAP

                if(country1_code.equals(country2_code)){
                    second_currency_rate = 1.0;
                }else{
                    second_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country2_code));
                }
                display();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do not do anything
            }

        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                country3_code = spinner3.getSelectedItem().toString();
                TextView second_country = (TextView) findViewById(R.id.third_currency_rate_desc);
                second_country.setText(country3_code); //HASHMAP

                if(country1_code.equals(country3_code)){
                    third_currency_rate = 1.0;
                }else{
                    third_currency_rate = Double.parseDouble(api_access.doInBackground(country1_code, country3_code));
                }
                display();

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
        This is the portion that controls the spinner. Upon the user selecting a new item from the spinner, the program must
        correctly update the currency exchange rates, and then refresh the display to the user.
     */





    public class Spinner_activity extends Activity implements AdapterView.OnItemSelectedListener{


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Spinner currentSpinner = (Spinner) findViewById(view.getId());
            String selectedItem = currentSpinner.getSelectedItem().toString();
            int currentId = view.getId();


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void set_spinners(Spinner spinner){


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

                }
                // note if the decimal is clicked, adding a zero does not change the value therefore do nothing.
            } else if (result == R.id.num1) {
                if (decimalClicked) {
                    total_currency += "1";
                    selected_currency = Double.parseDouble(total_currency);

                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num2) {
                if (decimalClicked) {
                    total_currency += "2";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num3) {
                if (decimalClicked) {
                    total_currency += "3";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num4) {
                if (decimalClicked) {
                    total_currency += "4";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num5) {
                if (decimalClicked) {
                    total_currency += "5";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num6) {
                if (decimalClicked) {
                    total_currency += "6";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num7) {
                if (decimalClicked) {
                    total_currency += "7";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num8) {
                if (decimalClicked) {
                    total_currency += "8";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.num9) {
                if (decimalClicked) {
                    total_currency += "9";
                    selected_currency = Double.parseDouble(total_currency);


                } else {
                    selected_currency++;


                }
            } else if (result == R.id.decimal) {
                if(!decimalClicked){
                    total_currency += ".0";
                    decimalClicked = true;
                }
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
        display(); // no matter what we want to call display;
    }


    public void display() {

        /*
            Here we simply convert the currency rates and then format them to strings with 3 decimal places
         */

        total_currency = String.format("%.3f", selected_currency);

        Double tmp = selected_currency * second_currency_rate;
        currency_to_currency2 = String.format("%.3f", tmp.toString());


        tmp = selected_currency * third_currency_rate;
        currency_to_currency3 = String.format("%.3f", tmp.toString());


        /*
            We set the calculator view of all three currencies to display the proper currency conversion amounts

            e.g. 1 CAD to USD at 0.75% = $0.75
         */

        TextView current = (TextView) findViewById(R.id.first_currency);
        current.setText(total_currency);


        TextView secondCurrency = (TextView) findViewById(R.id.second_currency_rate);
        secondCurrency.setText(currency_to_currency2);


        TextView thirdCurrency = (TextView) findViewById(R.id.third_currency_rate);
        thirdCurrency.setText(currency_to_currency3);

        /*
                Here we set the text below the currency amounts to display the proper country description
                e.g. CAD = Canadian Dollars
         */

        TextView currentDesc = (TextView) findViewById(R.id.first_currency_desc);
        currentDesc.setText(country1_code);


        TextView secondCurrencyDesc = (TextView) findViewById(R.id.second_currency_rate_desc);
        secondCurrencyDesc.setText(country2_code);


        TextView thirdCurrencyDesc = (TextView) findViewById(R.id.third_currency_rate_desc);
        thirdCurrencyDesc.setText(country3_code);
    }


}
