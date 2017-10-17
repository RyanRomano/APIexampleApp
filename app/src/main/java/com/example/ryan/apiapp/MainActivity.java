package com.example.ryan.apiapp;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Declare widgets & variables
    EditText emailText;
    TextView responseView;
    ProgressBar progressBar;
    String email;
    JSONObject json;
    ArrayList<String> arrayList = new ArrayList<>();


    static final String API_KEY = "8ff3e9ba68defb3c";
    static final String API_URL = "https://api.fullcontact.com/v2/person.json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize widgets & values
        emailText = (EditText) findViewById(R.id.emailText);
        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button queryButton = (Button) findViewById(R.id.queryButton);


        //Click to get Api feed
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveApiFeedTask().execute();
            }
        });

    }


    class RetrieveApiFeedTask extends AsyncTask<Void, Void, String> {

        //Before you click
        protected void onPreExecute() {
            arrayList = new ArrayList<>();
            responseView.setText("");
            progressBar.setVisibility(View.VISIBLE);

            //Get email from EditText widget
            email = emailText.getText().toString();
        }

        //The instant it is clicked - do some things...
        protected String doInBackground(Void... args) {

            try {
                //Create URL with API key and info
                URL url = new URL(API_URL + "email=" + email + "&apiKey=" + API_KEY);

                //Open Connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {

                    //Read the stream line by line
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    //until no more lines are left
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    //close reader and builder
                    bufferedReader.close();
                    return stringBuilder.toString();
                }

                //closeConnection
                finally {
                    urlConnection.disconnect();
                }
            }
            //Check for errors
            catch (Exception e) {
                return null;
            }
        }

        //Once background tasks are completed and return a response,
        protected void onPostExecute(String response) {
            String returnString = "";
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            try {
                json = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray articles = json.getJSONArray("socialProfiles");
                for (int i = 0; i < articles.length(); i++) {
                    arrayList.add(articles.getJSONObject(i).get("typeName") + " ");
                    returnString += articles.getJSONObject(i).get("typeName");
                }
            }
            catch (Exception e){System.out.println("yea dang");}
            responseView.setText(returnString);
            progressBar.setVisibility(View.GONE);


            System.out.println("hello world ~~~~~ " + arrayList.size());
        }
    }

}