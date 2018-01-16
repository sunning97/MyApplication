package com.example.giangnguyen.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_input;
    Button btn_submit;
    TextView tv_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControl();
        addEvent();
    }

    private void addEvent() {
        btn_submit.setOnClickListener(this);
    }

    private void addControl() {
        edt_input = findViewById(R.id.edt_input);
        btn_submit = findViewById(R.id.btn_submit);
        tv_display = findViewById(R.id.tv_display);
    }

    @Override
    public void onClick(View view) {
        String s = edt_input.getText().toString();
        //String url = "http://192.168.1.102/weblazada/loaisanpham.php?maloaicha="+s;
        String url = "http://192.168.1.102/weblazada/loaisanpham.php";

        DownloadJW downloadJW = new DownloadJW();
        downloadJW.execute(url,s);

    }

    public class DownloadJW extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.appendQueryParameter("maloaicha",strings[1]);

                String truyvan = uriBuilder.build().getEncodedQuery();
                OutputStream outputStream = connection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(truyvan);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                outputStreamWriter.close();

                connection.connect();

                InputStream stream = connection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String line = "";
                StringBuilder builder = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    builder.append(line);
                }
                Log.d("kiem",builder.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
