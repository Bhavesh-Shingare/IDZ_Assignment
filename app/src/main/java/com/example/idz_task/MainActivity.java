package com.example.idz_task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    String name,age,salary;
    private static String JSON_URL = "https://aamras.com/dummy/EmployeeDetails.json";

    ArrayList<HashMap<String,String>> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeeList = new ArrayList<>();
        lv= findViewById(R.id.listview);

        GetData getData = new GetData();
        getData.execute();

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Employee selectedEmployee = employeeList.get(position);
//
//                Intent intent = new Intent(MainActivity.this, EmployeeDetailsActivity.class);
//                intent.putExtra("EMPLOYEE", selectedEmployee);
//                startActivity(intent);
//            }
//        });

    }

    public class GetData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection)  url.openConnection();


                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();

                    while(data!= -1){

                        current += (char) data;
                        data = inputStreamReader.read();
                    }
                    return  current;

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }

            } catch(Exception e){
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("employees");
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    name= jsonObject1.getString("name");
                    age= jsonObject1.getString("age");
                    salary = jsonObject1.getString("salary");

                    HashMap<String,String> employees = new HashMap<>();

                    employees.put("name",name);
                    employees.put("age",age);
                    employees.put("salary",salary);

                    employeeList.add(employees);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, employeeList,
                    R.layout.row_layout,
                    new String[]{"name","age","salary"},
                    new int[]{R.id.textView, R.id.textView2, R.id.textView3});

            lv.setAdapter(listAdapter);
        }
    }

}