package com.example.medihealth.activitys.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.search.DrugAdapter;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.Drug;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class SearchDrugActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DrugAdapter drugAdapter;
    EditText textSearch;
    ImageView iconClose;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);
        back = findViewById(R.id.back_btn);
        iconClose = findViewById(R.id.icon_close);
        textSearch = findViewById(R.id.search);
        textSearch.requestFocus();
        recyclerView = findViewById(R.id.list_drug);
        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSearch.setText("");
                iconClose.setVisibility(View.GONE);
                drugAdapter.clear();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchStr, int start, int before, int count) {
                if (!searchStr.toString().equals("")){
                    iconClose.setVisibility(View.VISIBLE);
                    showResultBySearchStr(searchStr.toString());
                }
                else{
                    iconClose.setVisibility(View.GONE);
                    drugAdapter.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showResultBySearchStr(String search) {
        String nameSearch = standardizedNameDrug(search);

        Query query = FirebaseUtil.getDrugCollectionReference()
                .whereGreaterThanOrEqualTo("name", nameSearch)
                .whereLessThan("name", nameSearch + "\uf8ff");

        FirestoreRecyclerOptions<Drug> options = new FirestoreRecyclerOptions.Builder<Drug>()
                .setQuery(query, new SnapshotParser<Drug>() {
                    @NonNull
                    @Override
                    public Drug parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        // Ánh xạ dữ liệu từ snapshot vào đối tượng Drug
                        Drug drug = new Drug();
                        drug.setName(snapshot.getString("name"));
                        drug.setIngredients(snapshot.getString("ingredients"));
                        drug.setFunction(snapshot.getString("function"));
                        drug.setExpiry(snapshot.getString("expiry"));
                        drug.setSideEffects(snapshot.getString("side effects"));
                        drug.setContraindicated(snapshot.getString("contraindicated"));
                        drug.setInteractions(snapshot.getString("drug interaction"));
                        return drug;
                    }
                }).build();
        drugAdapter = new DrugAdapter(options, getApplicationContext(), new DrugAdapter.IDrugViewHolder() {
            @Override
            public void onClickItem(int positon, String imageName) {
                Drug drug = drugAdapter.getItem(positon);
                Intent intent = new Intent(SearchDrugActivity.this, DrugDetailActivity.class);
                AndroidUtil.passDrugModelAsIntent(intent,drug);
                intent.putExtra("imageName",imageName);
                startActivity(intent);
            }

            @Override
            public void onDataLoaded(int size) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(drugAdapter);
        drugAdapter.startListening();
    }

    private String standardizedNameDrug(String name){
        return name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
    }
}