package com.example.medihealth.activitys.Search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.medihealth.R;

public class SearchDrugActivity extends AppCompatActivity {
    EditText textSearch;
    ImageView iconClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);
        iconClose = findViewById(R.id.icon_close);
        textSearch = findViewById(R.id.search);
        textSearch.requestFocus();
        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSearch.setText("");
                iconClose.setVisibility(View.GONE);
            }
        });
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchStr, int start, int before, int count) {
                if (!searchStr.equals("")){
                    iconClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}