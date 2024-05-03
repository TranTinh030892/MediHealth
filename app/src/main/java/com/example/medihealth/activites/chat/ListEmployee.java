package com.example.medihealth.activites.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.medihealth.R;
import com.example.medihealth.adapters.chat.EmployeeAdapter;
import com.example.medihealth.models.Employee;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ListEmployee extends AppCompatActivity {
    ImageButton backButton;
    RecyclerView recyclerView;
    EmployeeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);
        initView();
        setupRecyclerView();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recycler_view_listUser);
    }
    private void setupRecyclerView() {
        Query query = FirebaseUtil.allEmployeeCollectionReference();
        FirestoreRecyclerOptions<Employee> options = new FirestoreRecyclerOptions.Builder<Employee>()
                .setQuery(query,Employee.class).build();
        adapter = new EmployeeAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}