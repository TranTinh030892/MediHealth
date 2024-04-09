package com.example.medihealth.adapters.stat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.PrescriptionStat;

import java.util.List;

public class ScheduleStatMonthApdapter extends RecyclerView.Adapter<ScheduleStatMonthApdapter.ScheduleStatMonthViewHolder>{

    private List<PrescriptionStat> prescriptionStatList;

    public ScheduleStatMonthApdapter(List<PrescriptionStat> prescriptionStatList) {
        this.prescriptionStatList = prescriptionStatList;
    }

    @NonNull
    @Override
    public ScheduleStatMonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statmonth, parent, false);
        return new ScheduleStatMonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleStatMonthViewHolder holder, int position) {
        PrescriptionStat prescriptionStat = prescriptionStatList.get(position);
        holder.txtTitle.setText(prescriptionStat.getTitle());
        holder.txtProcess.setText(String.format("%.0f", prescriptionStat.getProcess())+"%");
        if(prescriptionStat.getProcess() <= 80) holder.txtProcess.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_cancel));
        else if(prescriptionStat.getProcess() >= 96) holder.txtProcess.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_confirm));
        else holder.txtProcess.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_yelow));
    }

    @Override
    public int getItemCount() {
        return this.prescriptionStatList.size();
    }


    public static class ScheduleStatMonthViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtProcess;
        public ScheduleStatMonthViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtProcess = itemView.findViewById(R.id.txtProcess);
        }
    }
}
