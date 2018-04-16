package com.york13.weatherfromyork13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteDataSource {

    private DataBaseHelperWeatherFromYork13 dbHelper;
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DataBaseHelperWeatherFromYork13.COLUMN_ID,
            DataBaseHelperWeatherFromYork13.COLUMN_CITY_AND_COUNTRY,
            DataBaseHelperWeatherFromYork13.COLUMN_UPDATE,
            DataBaseHelperWeatherFromYork13.COLUMN_OTHER_CONDITIONS,
            DataBaseHelperWeatherFromYork13.COLUMN_TEMPERATURE,
            DataBaseHelperWeatherFromYork13.COLUMN_WEATHER_ICON
    };

    public NoteDataSource(Context context) {
        dbHelper = new DataBaseHelperWeatherFromYork13(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Note addNote(String cityAndCountry, String updated, String otherConditions, String currentTemperature,
    String weatherIcon) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelperWeatherFromYork13.COLUMN_CITY_AND_COUNTRY, cityAndCountry);
        values.put(DataBaseHelperWeatherFromYork13.COLUMN_UPDATE, updated);
        values.put(DataBaseHelperWeatherFromYork13.COLUMN_OTHER_CONDITIONS, otherConditions);
        values.put(DataBaseHelperWeatherFromYork13.COLUMN_TEMPERATURE, currentTemperature);
        values.put(DataBaseHelperWeatherFromYork13.COLUMN_WEATHER_ICON, weatherIcon);
        long insertId = database.insert(DataBaseHelperWeatherFromYork13.TABLE_NOTES, null,
                values);
        Note newNote = new Note();
        newNote.setCityAndCountry(cityAndCountry);
        newNote.setUpdated(updated);
        newNote.setOtherConditions(otherConditions);
        newNote.setCurrentTemperature(currentTemperature);
        newNote.setWeatherIcon(weatherIcon);
        newNote.setId(insertId);
        return newNote;
    }

    public void editNote(long id, String cityAndCountry, String updated, String otherConditions, String currentTemperature,
                         String weatherIcon) {
        ContentValues editedNote = new ContentValues();
        editedNote.put(dbHelper.COLUMN_ID, id);
        editedNote.put(dbHelper.COLUMN_CITY_AND_COUNTRY, cityAndCountry);
        editedNote.put(dbHelper.COLUMN_UPDATE, updated);
        editedNote.put(dbHelper.COLUMN_OTHER_CONDITIONS, otherConditions);
        editedNote.put(dbHelper.COLUMN_TEMPERATURE, currentTemperature);
        editedNote.put(dbHelper.COLUMN_WEATHER_ICON, weatherIcon);

        database.update(dbHelper.TABLE_NOTES, editedNote,dbHelper.COLUMN_ID + "=" + id,
                null);
    }

    public void deleteNote(Note note) {
        long id = note.getId();
        database.delete(DataBaseHelperWeatherFromYork13.TABLE_NOTES, DataBaseHelperWeatherFromYork13.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll() {
        database.delete(DataBaseHelperWeatherFromYork13.TABLE_NOTES, null, null);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();

        Cursor cursor = database.query(DataBaseHelperWeatherFromYork13.TABLE_NOTES,
                notesAllColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setCityAndCountry(cursor.getString(1));
        note.setUpdated(cursor.getString(2));
        note.setOtherConditions(cursor.getString(3));
        note.setCurrentTemperature(cursor.getString(4));
        note.setWeatherIcon(cursor.getString(5));
        return note;
    }
}
