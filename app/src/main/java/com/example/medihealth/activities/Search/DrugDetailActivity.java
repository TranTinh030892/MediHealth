package com.example.medihealth.activities.Search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medihealth.R;
import com.example.medihealth.models.Drug;
import com.example.medihealth.utils.AndroidUtil;

public class DrugDetailActivity extends AppCompatActivity {
    Drug drug;
    ImageView imageDrug;
    ImageButton back;
    TextView name,ingredients,function,expiry,sideEffects,contraindicated,interactions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail);
        drug = AndroidUtil.getDrugModelFromIntent(getIntent());
        initView();
        setupDrugDetail(drug);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        back = findViewById(R.id.back_btn);
        name = findViewById(R.id.name);
        ingredients = findViewById(R.id.ingredients);
        function = findViewById(R.id.function);
        expiry = findViewById(R.id.expiry);
        sideEffects = findViewById(R.id.sideEffects);
        contraindicated = findViewById(R.id.contraindicated);
        interactions = findViewById(R.id.interactions);
        imageDrug = findViewById(R.id.imageDrug);
    }

    private void setupDrugDetail(Drug drug) {
        name.setText(drug.getName());
        ingredients.setText(drug.getIngredients());
        function.setText(drug.getFunction());
        expiry.setText(drug.getExpiry());
        sideEffects.setText(drug.getSideEffects());
        contraindicated.setText(drug.getContraindicated());
        interactions.setText(drug.getInteractions());

        String imageName = getIntent().getStringExtra("imageName");
        Resources resources = getResources();
        String packageName = getPackageName();
        int imageResourceId = resources.getIdentifier(imageName, "drawable", packageName);
        imageDrug.setImageResource(imageResourceId);
    }
}