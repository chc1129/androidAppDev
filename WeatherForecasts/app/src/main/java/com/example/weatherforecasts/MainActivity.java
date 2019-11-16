package com.example.weatherforecasts;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView location;
    private LinearLayout forecasterLayout;
    private ProgressBar progress;

    public class ApiTask extends GetWeatherForecastTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(WeatherForecast data) {
            super.onPostExecute(data);

            progress.setVisibility(View.GONE);

            if (data != null) {
                location.setText(data.location.area + " "
                        + data.location.prefecture + " "
                        + data.location.city);

                for (WeatherForecast.Forecast forecast : data.forecastList) {
                    View row = View.inflate(MainActivity.this, R.layout.forecast_row, null);
                    TextView date = (TextView) row.findViewById(R.id.tv_date);
                    date .setText(forecast.dateLabel);

                    TextView telop = (TextView) row.findViewById(R.id.tv_telop);
                    telop.setText(forecast.telop);

                    TextView temperature =
                            (TextView) row.findViewById(R.id.tv_temperature);
                    temperature.setText(forecast.temperature.toString());

                    ImageView image = (ImageView) row.findViewById(R.id.iv_weather);

                    ImageLoaderTask task = new ImageLoaderTask();
                    task.execute(new ImageLoaderTask.Request(
                            image, forecast.image.url));

                    forecasterLayout.addView(row);
                }
            } else if (exception != null) {
                Toast.makeText(getApplicationContext(), exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = (TextView) findViewById(R.id.tv_location);
        forecasterLayout = (LinearLayout) findViewById(R.id.ll_forecasts);
        progress = (ProgressBar) findViewById(R.id.progress);

        new ApiTask().execute("400040");
    }
}