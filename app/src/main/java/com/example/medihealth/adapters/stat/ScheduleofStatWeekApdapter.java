package com.example.medihealth.adapters.stat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Schedule;

import java.util.List;

public class ScheduleofStatWeekApdapter extends RecyclerView.Adapter<ScheduleofStatWeekApdapter.ScheduleofStatWeekHolder>{

    private List<Schedule> scheduleList;
    private List<Boolean> checkSchedule;
    private boolean check;

    public ScheduleofStatWeekApdapter(List<Schedule> scheduleList, List<Boolean> checkSchedule) {
        this.scheduleList = scheduleList;
        this.checkSchedule = checkSchedule;
        this.check = false;
    }

    @NonNull
    @Override
    public ScheduleofStatWeekHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_statweek, parent, false);
        return new ScheduleofStatWeekHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleofStatWeekHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        if(schedule.getConfirmNotifications().isEmpty()){
            if(checkSchedule.get(position) || check){
                holder.imageView.setImageResource(R.drawable.circle_check_fill);
            }else{
                holder.imageView.setImageResource(R.drawable.circle_fill);
            }
        }else if(schedule.getConfirmNotifications().get(0).isCheck()){
            check = true;
            holder.imageView.setImageResource(R.drawable.circle_checked_fill);
        }else{
            check = true;
            holder.imageView.setImageResource(R.drawable.circle_x_fill);
        }
    }

    @Override
    public int getItemCount() {
        return this.scheduleList.size();
    }

    public static class ScheduleofStatWeekHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ScheduleofStatWeekHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
