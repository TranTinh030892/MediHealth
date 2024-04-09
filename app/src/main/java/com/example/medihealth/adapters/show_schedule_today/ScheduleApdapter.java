package com.example.medihealth.adapters.show_schedule_today;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleApdapter extends RecyclerView.Adapter<ScheduleApdapter.ScheduleViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(Schedule schedule);
    }
    private List<Schedule> scheduleList;
    private OnItemClickListener listener;

    public ScheduleApdapter(List<Schedule> scheduleList, OnItemClickListener listener) {
        this.scheduleList = scheduleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position){
        Schedule schedule = scheduleList.get(position);
        holder.txtTitle.setText(schedule.getPrescription().getTitle());
        holder.txtDrugUser.setText("Người uống: "+schedule.getPrescription().getDrugUser().getName());
        LocalTime time = schedule.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.txtSchedule.setText(time.format(formatter));
        LocalTime currentTime = LocalTime.now();
        if(time.isBefore(currentTime)){
            holder.txtSchedule.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_cancel));
            holder.txtTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_cancel));
            holder.txtDrugUser.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_cancel));
        }else{
            holder.txtSchedule.setTextColor(Color.BLACK);
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.txtDrugUser.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(schedule);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return  scheduleList.size();
    }

    public static class ScheduleViewHolder extends  RecyclerView.ViewHolder{
        TextView txtTitle, txtSchedule, txtDrugUser;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDrugUser = itemView.findViewById(R.id.txtNameDrugUser);
            txtSchedule = itemView.findViewById(R.id.txtSchedule);
        }
    }
}
