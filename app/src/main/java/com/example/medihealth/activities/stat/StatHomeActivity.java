package com.example.medihealth.activities.stat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.activities.MainActivity;

public class StatHomeActivity extends AppCompatActivity {
    private Spinner spinnerStat;
    private Button btnContinue;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_home);

        spinnerStat = findViewById(R.id.spinnerStat);
        btnContinue = findViewById(R.id.button_continue);
        imgBack = findViewById(R.id.image_back);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStat.setAdapter(adapter);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = spinnerStat.getSelectedItemPosition();
                Intent intent;
                switch (selectedPosition){
                    case 0:
                        intent = new Intent(StatHomeActivity.this, StatDayActivity.class);
                        break;
                    case 1:
                        intent = new Intent(StatHomeActivity.this, StatWeeksActivity.class);
                        break;
                    case 2:
                        intent = new Intent(StatHomeActivity.this, StatMonthActivity.class);
                        break;
                    default:
                        return;
                }
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatHomeActivity.this, MainActivity.class));
            }
        });
    }
}
