package com.example.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jack.view.GradeLayout;


public class MainActivity extends AppCompatActivity
        implements GradeLayout.OnGradeUpdateListener, View.OnClickListener {

    private GradeLayout mGradeLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGradeLy = findViewById(R.id.grade_ly);
        mGradeLy.setOnGradeUpdateListener(this);
        mGradeLy.setChosenGrade("5");
        Button changeGradeBtn = findViewById(R.id.change_grade_btn);
        changeGradeBtn.setOnClickListener(this);
        Button changeChosenItemBtn = findViewById(R.id.change_chosen_item_btn);
        changeChosenItemBtn.setOnClickListener(this);
        Button change2Btn = findViewById(R.id.change_grade2_btn);
        change2Btn.setOnClickListener(this);
    }

    @Override
    public void onGradeUpdate(GradeLayout view, String grade) {
        if (view.getId() == R.id.grade_ly) {
            Toast.makeText(this, grade, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_grade_btn:
                List<String> list = new ArrayList<>();
                list.add("100");
                list.add("200");
                list.add("300");
                list.add("400");
                list.add("500");
                list.add("600");
                list.add("700");
                list.add("800");
                list.add("900");
                list.add("1000");
                mGradeLy.setGradeTexts(list);
                break;
            case R.id.change_chosen_item_btn:
                mGradeLy.setChosenGrade("3");
                break;
            case R.id.change_grade2_btn:
                mGradeLy.setChosenGrade("9");
                break;
            default:
                break;
        }
    }
}
