package com.example.darrenpankoff.currencyconverter.feature.Data;


import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Countries {


    public double getExchangeRate(String country1, String country2){

        GetCountries getRate = new GetCountries();

        Double result = Double.parseDouble(getRate.doInBackground(country1, country2));
        return result;

    }

    // params, progress, results

    static private class GetCountries extends AsyncTask<String, Void, String>{
        private String api_base_url = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE";
        private final String key = "API KEY";


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
                   return my_string.toString();


               }finally {
                   urlConnection.disconnect();
               }
           }
           catch (Exception e){
               Log.e("ERROR DETECTED", e.getMessage(), e);
               return null;
           }

        }

    }
    protected String onPostExectue(String response){

        if (response == null){
            response = "ERROR OCCURRED FETCHING DATA";

        }

        try{
            JSONObject obj = new JSONObject(response);

            JSONArray results = new JSONArray(obj.getString("Realtime Currency Exchange Rate"));

            JSONObject exchange_rate = results.getJSONObject(4);

            return exchange_rate.getString("5. Exchange Rate");

        }catch (JSONException e){
            Log.i("ERROR PARSING JSON", e.toString());
            Log.i("RESULTS", response);
            return "0.0";


        }

    }



}
