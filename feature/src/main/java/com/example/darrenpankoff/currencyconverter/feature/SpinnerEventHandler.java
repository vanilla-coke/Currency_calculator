package com.example.darrenpankoff.currencyconverter.feature;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerEventHandler extends Activity implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if(first_time){
                spinner1.setSelection(0); // set the default spinner values
                spinner2.setSelection(1);
                spinner3.setSelection(2);
                first_time = !first_time; // set flag to false

            }else{
                Spinner currentSpinner = (Spinner) findViewById(selectedItemView.getId());
                String selectedItem = currentSpinner.getSelectedItem().toString();
                int currentId = selectedItemView.getId();

                if(currentId == R.id.currency_spinner1){
                    country1_code = selectedItem; // change the country code
                    TextView first_country = (TextView) findViewById(R.id.first_currency_desc);
                    first_country.setText(country1_code); // NEED HASHMAP

                    // check if 1 vs 2 are same countries, and 1 vs 3 are same. If they are the exchange rate is just equal i.e. 1:1
                    if(selectedItem.equals(country2_code)){
                        second_currency_rate = 1.0;
                    } else if(selectedItem.equals(country3_code)){
                        third_currency_rate = 1.0;

                    }else{
                        second_currency_rate = Double.parseDouble(api_access.doInBackground(selectedItem, country2_code));
                        third_currency_rate = Double.parseDouble(api_access.doInBackground(selectedItem, country3_code));
                    }
                }else if(currentId == R.id.currency_spinner2){
                    country2_code = selectedItem;
                    TextView second_country = (TextView) findViewById(R.id.second_currency_rate_desc);
                    second_country.setText(country2_code); //HASHMAP

                    if(selectedItem.equals(country2_code)){
                        second_currency_rate = 1.0;
                    }else{
                        second_currency_rate = Double.parseDouble(api_access.doInBackground(selectedItem, country2_code));
                    }
                }else {
                    country3_code = selectedItem;
                    TextView third_country = (TextView) findViewById(R.id.third_currency_rate_desc);
                    third_country.setText(country3_code); //HASHMAP

                    if(selectedItem.equals(country3_code)){
                        third_currency_rate = 1.0;
                    }else{
                        third_currency_rate = Double.parseDouble(api_access.doInBackground(selectedItem, country3_code));
                    }
                }

                currentSpinner.setSelection(adapter.getPosition(selectedItem)); // set the adapter to display the selected view

                display();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // do not do anything
        }

}
