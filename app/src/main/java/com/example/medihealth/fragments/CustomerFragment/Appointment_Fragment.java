package com.example.medihealth.fragments.CustomerFragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.appointment.Customer_AppoitnmentAdapter;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class Appointment_Fragment extends Fragment implements View.OnClickListener {
    RelativeLayout btnAppointment, btnHistory;
    TextView textApointment, textHistory;
    RecyclerView recyclerView;
    Customer_AppoitnmentAdapter appoitnmentAdapter;
    ProgressBar progressBar;
    public Appointment_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView =  inflater.inflate(R.layout.fragment_book_appointment, container, false);
        initView(itemView);
        setOnclick();
        setBackgroudButton(true);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                setupAppointmentRecyclerView(FirebaseUtil.currentUserId());
            }
        }, 1000);
        return  itemView;
    }

    private void initView(View itemView) {
        btnAppointment = itemView.findViewById(R.id.btn_appointment);
        textApointment = itemView.findViewById(R.id.text_appointment);
        btnHistory = itemView.findViewById(R.id.btn_history);
        textHistory = itemView.findViewById(R.id.text_history);
        recyclerView = itemView.findViewById(R.id.list_Appointment);
        progressBar = itemView.findViewById(R.id.loading);
    }
    private void setOnclick() {
        btnAppointment.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_appointment){
            setBackgroudButton(true);
            setupAppointmentRecyclerView(FirebaseUtil.currentUserId());
        }
        if (v.getId() == R.id.btn_history){
            setBackgroudButton(false);
            setupHistoryRecyclerView(FirebaseUtil.currentUserId());
        }
    }
    private void setupAppointmentRecyclerView(String currentId) {
        Query query = FirebaseUtil.getAppointmentCollectionReference()
                .whereEqualTo("userModel.userId",currentId)
                .whereIn("stateAppointment", Arrays.asList(0, 1,-1));
        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
                .setQuery(query,Appointment.class).build();
        appoitnmentAdapter = new Customer_AppoitnmentAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(appoitnmentAdapter);
        appoitnmentAdapter.startListening();
    }
    private void setupHistoryRecyclerView(String currentId) {
        Query query = FirebaseUtil.getAppointmentCollectionReference()
                .whereEqualTo("userModel.userId",currentId)
                .whereIn("stateAppointment", Arrays.asList(-1, 2));
        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
                .setQuery(query,Appointment.class).build();
        appoitnmentAdapter = new Customer_AppoitnmentAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(appoitnmentAdapter);
        appoitnmentAdapter.startListening();
    }
    private void setBackgroudButton(boolean b){
        int colorOfButtonSelected = ContextCompat.getColor(requireContext(), R.color.color_background_menu_appointment);
        int colorOfButtonNotSelected = ContextCompat.getColor(requireContext(), R.color.white);
        int colorOfTextSelected = ContextCompat.getColor(requireContext(), R.color.text_Color);
        int colorOfTextNotSelected = ContextCompat.getColor(requireContext(), R.color.light_gray);
        if (b){
            btnAppointment.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonSelected));
            textApointment.setTextColor(colorOfTextSelected);
            btnHistory.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
            textHistory.setTextColor(colorOfTextNotSelected);
        }
        else {
            btnHistory.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonSelected));
            textApointment.setTextColor(colorOfTextNotSelected);
            btnAppointment.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
            textHistory.setTextColor(colorOfTextSelected);
        }
    }
}