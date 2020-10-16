package com.example.quizsamp;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.app.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class ThemeActivity extends AppCompatActivity {

    private GridView themeGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thèmes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        themeGrid = findViewById(R.id.themeGridView);

        List<String> themeList = new ArrayList<>();
        themeList.add("Animaux");
        themeList.add("Culture");
        themeList.add("Musique");
        themeList.add("Jeux Vidéo");
        themeList.add("Plantes");
        themeList.add("Monuments");
        themeList.add("Langue");
        themeList.add("Nourriture");

        ThemeAdapter adapter = new ThemeAdapter(themeList);
        themeGrid.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ThemeActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}