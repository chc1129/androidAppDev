package com.example.weatherforecasts;

import android.os.AsyncTask;
import java.io.IOException;

public class GetWeatherForecastTask extends AsyncTask<String, Void, String> {

    Exception exception;

    @Override
    protected String doInBackground(String... params) {

        try {
            return WeatherApi.getWeather(params[0]);
        } catch (IOException e) {
            exception = e;
        }
        return null;
    }
}
