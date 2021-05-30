package com.example.cbsd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.cbsd_project.helpers.ThemeUtil;
import com.google.android.gms.maps.MapView;


public class ChangeThemeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtil.setTheme(this);

        setContentView(R.layout.activity_change_theme);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.activity_change_theme_spinnerOptions);
        //create a list of items for the spinner.
        String[] items = new String[]{
                getString(R.string.theme_default),
                getString(R.string.theme_lime),
                getString(R.string.theme_blue),
                getString(R.string.theme_green),
                getString(R.string.theme_red),
        };
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        Button buttonChangeTheme = (Button) findViewById(R.id.activity_change_theme_buttonChangeTheme);

        buttonChangeTheme.setOnClickListener(v -> {
            String theme =  dropdown.getSelectedItem().toString();
            if (theme.equals(getString(R.string.theme_lime))) {
                    ThemeUtil.setCurrentTheme(ThemeUtil.Theme.Lime);
            } else
            if (theme.equals(getString(R.string.theme_green))) {
                ThemeUtil.setCurrentTheme(ThemeUtil.Theme.Green);
            } else
            if (theme.equals(getString(R.string.theme_blue))) {
                ThemeUtil.setCurrentTheme(ThemeUtil.Theme.Blue);
            } else
            if (theme.equals(getString(R.string.theme_red))) {
                ThemeUtil.setCurrentTheme(ThemeUtil.Theme.Red);
            } else
            if (theme.equals(getString(R.string.theme_default))) {
                ThemeUtil.setCurrentTheme(ThemeUtil.Theme.Default);
            }

            ThemeUtil.setTheme(this);
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        });

    }
}