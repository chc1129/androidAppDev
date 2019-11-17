package com.example.weatherforecasts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ForecastFragment extends Fragment {

    private static final String KEY_CITY_CODE = "key_city_code";

    public static  ForecastFragment newInstance(String cityCode) {

        ForecastFragment fragment = new ForecastFragment();

        Bundle args = new Bundle();
        args.putString(KEY_CITY_CODE, cityCode);
        fragment.setArguments(args);

        return fragment;
    }
    private TextView location;
    private LinearLayout forecastsLayout;
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
                    View row = View.inflate(getContext(), R.layout.forecast_row, null);

                    TextView date = (TextView) row.findViewById(R.id.tv_date);
                    date.setText(forecast.dateLabel);

                    TextView telop = (TextView) row.findViewById(R.id.tv_telop);
                    telop.setText(forecast.telop);

                    TextView temperature =
                            (TextView) row.findViewById(R.id.tv_temperature);
                    temperature.setText(forecast.temperature.toString());

                    ImageView image = (ImageView) row.findViewById(R.id.iv_weather);

                    ImageLoaderTask task = new ImageLoaderTask();
                    task.execute(new ImageLoaderTask.Request(
                            image, forecast.image.url));

                    forecastsLayout.addView(row);
                }
            } else if (exception != null) {
                Toast.makeText(getContext(), exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaier,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, null);
        location = (TextView) view.findViewById(R.id.tv_location);
        forecastsLayout = (LinearLayout) view.findViewById(R.id.ll_forecasts);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String cityCode = getArguments().getString(KEY_CITY_CODE);
        new ApiTask().execute(cityCode);

    }
}