package com.example.medihealth.adapters.stat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Prescription;

import java.util.List;

public class StatDayApdapter extends RecyclerView.Adapter<StatDayApdapter.StatDayViewHolder> {

    private List<Prescription> prescriptionList;

    public StatDayApdapter(List<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public StatDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statday, parent, false);
        return new StatDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatDayViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);
        holder.txtTitle.setText(prescription.getTitle());
        holder.listConfirmRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleStatDayApdapter scheduleStatDayApdapter = new ScheduleStatDayApdapter(prescription.getSchedules());
        holder.listConfirmRecyclerView.setAdapter(scheduleStatDayApdapter);
    }

    @Override
    public int getItemCount() {
        return this.prescriptionList.size();
    }

    public static class StatDayViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        RecyclerView listConfirmRecyclerView;

        public StatDayViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            listConfirmRecyclerView = itemView.findViewById(R.id.listConfirmRecyclerView);
        }
    }
}
