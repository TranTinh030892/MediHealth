package com.example.medihealth.adapters.stat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.ConfirmNotification;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatWeekApdapter extends RecyclerView.Adapter<StatWeekApdapter.StatWeekViewHolder>{

    private List<Prescription> prescriptionList;
    private LocalDate day;

    public StatWeekApdapter(List<Prescription> prescriptionList, LocalDate day){
        this.prescriptionList = prescriptionList;
        this.day = day;
    }

    @NonNull
    @Override
    public StatWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statweek, parent, false);
        return new StatWeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatWeekViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);
        holder.txtTitle.setText(prescription.getTitle());
        List<Schedule> scheduleList = prescription.getSchedules();

        LocalDate localDay = day;
        //T2
        holder.t2RecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterT2 = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        localDay = localDay.plusDays(1);
        holder.t2RecyclerView.setAdapter(apdapterT2);
        //T3
        holder.t3RecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterT3 = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        localDay = localDay.plusDays(1);
        holder.t3RecyclerView.setAdapter(apdapterT3);
        //T4
        holder.t4RecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterT4 = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        localDay = localDay.plusDays(1);
        holder.t4RecyclerView.setAdapter(apdapterT4);
        //T5
        holder.t5RecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterT5 = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        localDay = localDay.plusDays(1);
        holder.t5RecyclerView.setAdapter(apdapterT5);
        //T6
        holder.t6RecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterT6 = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        localDay = localDay.plusDays(1);
        holder.t6RecyclerView.setAdapter(apdapterT6);
        //T7
        holder.t7RecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterT7 = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        localDay = localDay.plusDays(1);
        holder.t7RecyclerView.setAdapter(apdapterT7);
        //CN
        holder.cnRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ScheduleofStatWeekApdapter apdapterCN = new ScheduleofStatWeekApdapter(filterSchedule(scheduleList, localDay));
        holder.cnRecyclerView.setAdapter(apdapterCN);
    }

    @Override
    public int getItemCount() {
        return this.prescriptionList.size();
    }

    private List<Schedule> filterSchedule(List<Schedule> scheduleList, LocalDate date){
        List<Schedule> filteredSchedule = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            List<ConfirmNotification> confirmNotifications = schedule.getConfirmNotifications();
            List<ConfirmNotification> filteredConfirmNotifications = new ArrayList<>();
            if(confirmNotifications != null && !confirmNotifications.isEmpty()){
                for (ConfirmNotification confirmNotification : confirmNotifications) {
                    if (confirmNotification.getDate().isEqual(date)) {
                        filteredConfirmNotifications.add(confirmNotification);
                    }
                }
            }
            Schedule filteredScheduleItem = new Schedule();
            filteredScheduleItem.setConfirmNotifications(filteredConfirmNotifications);
            filteredSchedule.add(filteredScheduleItem);
        }

        return filteredSchedule;
    }

    public static class StatWeekViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        RecyclerView t2RecyclerView, t3RecyclerView, t4RecyclerView, t5RecyclerView, t6RecyclerView, t7RecyclerView, cnRecyclerView;
        public StatWeekViewHolder(@NonNull View itemView){
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            t2RecyclerView = itemView.findViewById(R.id.t2RecyclerView);
            t3RecyclerView = itemView.findViewById(R.id.t3RecyclerView);
            t4RecyclerView = itemView.findViewById(R.id.t4RecyclerView);
            t5RecyclerView = itemView.findViewById(R.id.t5RecyclerView);
            t6RecyclerView = itemView.findViewById(R.id.t6RecyclerView);
            t7RecyclerView = itemView.findViewById(R.id.t7RecyclerView);
            cnRecyclerView = itemView.findViewById(R.id.cnRecyclerView);
        }
    }
}
