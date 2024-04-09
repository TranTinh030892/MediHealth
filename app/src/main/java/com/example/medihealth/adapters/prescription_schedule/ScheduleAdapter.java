package com.example.medihealth.adapters.prescription_schedule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activities.prescription_schedule.CustomOnClickListener;
import com.example.medihealth.activities.prescription_schedule.SwipeItemViewHolder;
import com.example.medihealth.models.Schedule;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    List<Schedule> schedules;
    CustomOnClickListener<Schedule> onClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public ScheduleAdapter(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    public void setOnClickListener(CustomOnClickListener<Schedule> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.time.setText(schedule.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(schedule);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (schedules != null) {
            return schedules.size();
        }
        return 0;
    }

    public static class ScheduleViewHolder extends SwipeItemViewHolder {

        TextView time;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_prescription_schedule_time);
            layoutForeground = itemView.findViewById(R.id.layout_foreground);
        }
    }

}
