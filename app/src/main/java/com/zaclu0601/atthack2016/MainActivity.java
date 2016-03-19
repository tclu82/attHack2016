package com.zaclu0601.atthack2016;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView myPoint;
    EditText myX;
    EditText myY;


    ArrayList<String> name = new ArrayList<>();
    ArrayList<Double> x = new ArrayList<>();
    ArrayList<Double> y = new ArrayList<>();
//    String link = "https://data.seattle.gov/resource/9n95-kprs.geojson?$where=within_circle(the_geom,%2047.59,%20-122.3,%201000)";

    StringBuffer sb = new StringBuffer("https://data.seattle.gov/resource/9n95-kprs.geojson?$where=within_circle(the_geom,%2047.59,%20-122.3,%2001000)");
    String s;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        myPoint = (TextView) findViewById(R.id.myPoint);

        Button btn = (Button) findViewById(R.id.myFind);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Enter the distance: ");



                //sb.append(myX);
                //sb.append(")");



                new JASONTask().execute(sb.toString());

            }
        });
    }

    public class JASONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

//            ArrayList<String> name = new ArrayList<>();
//            ArrayList<Double> x = new ArrayList<>();
//            ArrayList<Double> y = new ArrayList<>();





            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer bufferX = new StringBuffer();
                StringBuffer bufferY = new StringBuffer();
                String line;

                line = reader.readLine();

                String token[] = line.split(",");

                for (String s : token) {

                    if (s.substring(0,11).equals("\"point_x\":\""))
                        x.add(Double.parseDouble(s.substring(11,25)));

                    else if (s.substring(0,11).equals("\"point_y\":\""))
                        y.add(Double.parseDouble(s.substring(11,25)));



                    if (s.substring(0, 12).equals("\"alt_name\":\"")) {
                        int i = 12;
                        while (s.charAt(i) != '\"') i++;

                        name.add(s.substring(12,i));

                    }

                }

                for (int i = 0; i < x.size(); i++)
                    System.out.println(name.get(i) + ": " + x.get(i) + ", " + y.get(i));




                //buffer.append(line);
                //while ((line = reader.readLine()) != null) buffer.append(line);

                return bufferY.toString();


//                String findJS = buffer.toString();
//                JSONObject parentObj = new JSONObject(findJS);
//                JSONArray parentArray = parentObj.getJSONArray("features");
//
//                JSONObject finalObj = parentArray.getJSONObject(0);
//
//                String name = finalObj.getString("alt_name");
//
//                double latitude = Double.parseDouble(finalObj.getString("latitude"));
//                double longitude = Double.parseDouble(finalObj.getString("longitude"));
//
//                return "latitude: " + Double.toString(latitude) + ", "+ "longitude: " + Double.toString(longitude);


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

//            }catch (JSONException e) {
//                e.printStackTrace();

            } finally {

                if (connection != null) connection.disconnect();

                try {

                    if (reader != null) reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            myPoint.setText(s.toString());
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
