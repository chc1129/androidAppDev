package com.example.weatherforecasts;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public class ApiTask extends GetWeatherForecastTask {
        @Override
        protected void onPostExecute(WeatherForecast data) {
            super.onPostExecute(data);
            if (data != null) {
                result.setText(data.location.area + " "
                        + data.location.prefecture + " "
                        + data.location.city);

                for (WeatherForecast.Forecast forecast : data.forecastList) {
                    // windowsの場合「\」, mac/linuxの場合「/」
                    result.append("\n");
                    result.append(forecast.dateLabel + " " + forecast.telop);
                }
            } else if (exception != null) {
                Toast.makeText(getApplicationContext(), exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.tv_result);

        new ApiTask().execute("400040");
    }
}
