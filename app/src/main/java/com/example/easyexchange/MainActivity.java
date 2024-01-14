package com.example.easyexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;



public class MainActivity extends AppCompatActivity {
    TextView convertFromDropdownTextView ,convertToDropdownTextView, conversionRateText;
    EditText editTextSearch,amountToConvert, ConversionRate;

    ArrayList<String> arrayList;
    Dialog fromDialog;
    Dialog toDialog;
    ListView listView;
    Button convertButton;
    String convertFromValue, convertToValue, conversionValue;

    String[] country = {"USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convertFromDropdownTextView = findViewById(R.id.convert_from_dropdown_menu);
        convertToDropdownTextView = findViewById(R.id.convert_to_dropdown_menu);
        convertButton = findViewById(R.id.conversionButton);

        conversionRateText = findViewById(R.id.conversionRateText);
        amountToConvert = findViewById(R.id.amountToConvert);
        ConversionRate = findViewById(R.id.ConversionRate);
        arrayList = new ArrayList<>();
        for(String i : country){
            arrayList.add(i);
        }


        convertFromDropdownTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fromDialog = new Dialog( MainActivity.this); fromDialog.setContentView(R.layout.from);
                fromDialog.getWindow().setLayout(650,900);
                fromDialog.show();

                ListView listView = fromDialog.findViewById(R.id.list1);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Lorsqu'un élément est cliqué, vous pouvez exécuter un code ici
                           convertFromValue = (String) parent.getItemAtPosition(position);
                        //Toast.makeText(MainActivity.this, "Selected Item 1: " + selectedItem, Toast.LENGTH_SHORT).show();
                        convertFromDropdownTextView.setText(convertFromValue);
                        fromDialog.dismiss();

                    }
                });


            }

        });

        convertToDropdownTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fromDialog = new Dialog( MainActivity.this); fromDialog.setContentView(R.layout.to);
                fromDialog.getWindow().setLayout(600,900);
                fromDialog.show();
                ListView listView = fromDialog.findViewById(R.id.list);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Lorsqu'un élément est cliqué, vous pouvez exécuter un code ici
                        convertToValue = (String) parent.getItemAtPosition(position);
                        convertToDropdownTextView.setText(convertToValue);
                        listView.setVisibility(View.GONE);
                        fromDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Selected Item 2: " + selectedItem, Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });



        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conversionValue= String.valueOf(amountToConvert.getText());

                    boolean enableButton = !TextUtils.isEmpty(convertFromValue) &&
                        !TextUtils.isEmpty(convertToValue) &&
                        !TextUtils.isEmpty(conversionValue);
                if(enableButton){
                    convertCurrency(convertFromValue, convertToValue, conversionValue);

                }else
                    Toast.makeText(MainActivity.this, "Please fill in all the input fields", Toast.LENGTH_SHORT).show();
            }
        });

}


    public void convertCurrency(String fromCurrency, String toCurrency, String amount) {

        OkHttpClient client = new OkHttpClient();

       
        String apiKey = "b5022c80fbf39e296c411168";
        String apiUrl = "https://v6.exchangerate-api.com/v6/"+apiKey+"/pair/"+fromCurrency+"/"+toCurrency+"/"+amount;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    reader.close();
                    connection.disconnect();

                    String apiResult = stringBuilder.toString();

                    JSONObject jsonObject = new JSONObject(apiResult);
                    String conversionResult = jsonObject.getString("conversion_result");
                    ConversionRate.setText(conversionResult);

                } else {
                    Log.e("MainActivity", "HTTP error code: " + responseCode);
                }
            } catch (Exception err) {
                Log.e("MainActivity", "Error during API call", err);
            }

        });

    }


}


