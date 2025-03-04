package com.example.week1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutMeButton = findViewById(R.id.btnAboutMe);
        Button projectsButton = findViewById(R.id.btnProjects);
        Button contactButton = findViewById(R.id.btnContact);

        aboutMeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AboutMeActivity.class)));
        projectsButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ProjectsActivity.class)));
        contactButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ContactActivity.class)));
    }
}
