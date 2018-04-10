package com.york13.weatherfromyork13;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FragmentManager fragmentManager = getFragmentManager();

    private HomeScreen current = HomeScreen.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.container, new EmptyFragment()).commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(current.getItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.city_selection){
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберете город");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Отлично", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        EmptyFragment emptyFragment = (EmptyFragment)fragmentManager.findFragmentById(R.id.container);
        emptyFragment.citySelection(city);
        new LastCity(this).setCity(city);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.about_me: {
                selectScreen(HomeScreen.FIRST);
                break;
            }
            case R.id.contacts: {
                selectScreen(HomeScreen.SECOND);
                break;
            }
            case R.id.settings: {
                selectScreen(HomeScreen.THIRD);
                break;
            }
        }
        return true;
    }

    private void selectScreen(HomeScreen screen) {
        current = screen;
        switch (screen.getItemId()) {
            case R.id.about_me: {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Обо мне работает!", Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.contacts: {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Контакты работают!", Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.settings: {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Настройки работают!", Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
        }
    }
}
