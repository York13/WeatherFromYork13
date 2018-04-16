package com.york13.weatherfromyork13;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperWeatherFromYork13 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myFirstBase.db"; // название бд
    private static final int DATABASE_VERSION = 1; // версия базы данных
    static final String TABLE_NOTES = "notes"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CITY_AND_COUNTRY = "cityAndCountry";
    public static final String COLUMN_UPDATE = "updated";
    public static final String COLUMN_OTHER_CONDITIONS = "otherConditions";
    public static final String COLUMN_TEMPERATURE = "currentTemperature";
    public static final String COLUMN_WEATHER_ICON = "weatherIcon";

    public DataBaseHelperWeatherFromYork13(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITY_AND_COUNTRY + " TEXT, " +
                COLUMN_UPDATE + " TEXT, " +
                COLUMN_OTHER_CONDITIONS + " TEXT, " +
                COLUMN_TEMPERATURE + " TEXT, " +
                COLUMN_WEATHER_ICON + " TEXT " +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ((oldVersion == 1) && (newVersion == 2)) {
            String upgradeQuery = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_CITY_AND_COUNTRY + " TEXT DEFAULT 'Title'";
            db.execSQL(upgradeQuery);
        }
    }
}
