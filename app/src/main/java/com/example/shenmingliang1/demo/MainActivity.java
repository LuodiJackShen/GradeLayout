package com.example.shenmingliang1.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GradeLayout.OnGradeUpdateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GradeLayout ly = findViewById(R.id.grade_ly);
        ly.setOnGradeUpdateListener(this);
        Button btn = findViewById(R.id.submit_btn);
        final List<String> list = new ArrayList<>();
        list.add("10");
        list.add("11");
        list.add("12");
        ly.setGradeTexts(list);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ly.setChosenGrade(1);
            }
        });
    }

    @Override
    public void onGradeUpdate(GradeLayout view, String grade) {
        if (view.getId() == R.id.grade_ly) {
            Toast.makeText(this, grade, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }
    }
}
