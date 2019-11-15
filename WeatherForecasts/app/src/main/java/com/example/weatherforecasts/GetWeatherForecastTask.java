package com.example.weatherforecasts;

import android.os.AsyncTask;
import java.io.IOException;
import org.json.JSONException;

public class GetWeatherForecastTask extends AsyncTask<String, Void, WeatherForecast> {

    Exception exception;

    @Override
    protected WeatherForecast doInBackground(String... params) {

        try {
            return WeatherApi.getWeather(params[0]);
        } catch (IOException | JSONException e) {
            exception = e;
        }
        return null;
    }
}
