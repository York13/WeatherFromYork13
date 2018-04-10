package com.york13.weatherfromyork13;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class EmptyFragment extends Fragment {
    Typeface weatherFont;

    TextView cityAndCountry;
    TextView updated;
    TextView otherConditions;
    TextView currentTemperature;
    TextView weatherIcon;

    Handler handler;

    public EmptyFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_empty, container, false);
        cityAndCountry = (TextView)rootView.findViewById(R.id.city_and_country);
        updated = (TextView)rootView.findViewById(R.id.updated);
        otherConditions = (TextView)rootView.findViewById(R.id.other_conditions);
        currentTemperature = (TextView)rootView.findViewById(R.id.current_temperature);
        weatherIcon = (TextView)rootView.findViewById(R.id.icon);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
        updateWeatherData(new LastCity(getActivity()).getCity());
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = WeatherDataLoader.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            cityAndCountry.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " + json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            Double deg = wind.getDouble("deg");

            otherConditions.setText(details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Влажность: " + main.getString("humidity") + "%" +
                            "\n" + "Давление: " + main.getString("pressure") + " hPa" +
                    "\n" + "Ветер: " + windDirection(deg) + ", " + wind.getString("speed") + " м/с");

            currentTemperature.setText(String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updated.setText("Последнее обновление данных: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("WeatherApplication", "Какое-то из полей не найдено в JSON-файле");
        }
    }

    private String windDirection(Double deg) {
        String[] windDirection = {"С","С-СВ","СВ","В-СВ","В","В-ЮВ","ЮВ","Ю-ЮВ","Ю","Ю-ЮЗ","ЮЗ","З-ЮЗ","З","З-СЗ","СЗ","С-СЗ","С"};
        Double stepOne = deg % 360;
        int stepTwo = (int)(stepOne / 22.5) + 1;
        return windDirection[stepTwo];
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void citySelection(String city){
        updateWeatherData(city);
    }
}
