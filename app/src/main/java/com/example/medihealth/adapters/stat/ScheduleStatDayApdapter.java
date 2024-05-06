package com.example.medihealth.adapters.stat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.ConfirmNotification;
import com.example.medihealth.models.Schedule;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleStatDayApdapter extends RecyclerView.Adapter<ScheduleStatDayApdapter.ScheduleStatDayViewHolder> {

    private List<Schedule> scheduleList;

    public ScheduleStatDayApdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ScheduleStatDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_statday, parent, false);
        return new ScheduleStatDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleStatDayViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.txtSchedule.setText(schedule.getTime().format(timeFormatter));
        if (schedule.getConfirmNotifications() == null || schedule.getConfirmNotifications().isEmpty()) {
            holder.txtConfirm.setText("Chưa uống");
            holder.txtReason.setVisibility(View.GONE);
        } else {
            ConfirmNotification confirmNotification = schedule.getConfirmNotifications().get(0);
            if (confirmNotification.isCheck()) {
                holder.txtConfirm.setText("Đã uống");
                holder.txtConfirm.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_confirm));
                holder.txtReason.setVisibility(View.GONE);
            } else {
                holder.txtConfirm.setText("Bỏ uống");
                holder.txtConfirm.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_cancel));
                holder.txtReason.setVisibility(View.VISIBLE);
                holder.txtReason.setText("Lý do: " + confirmNotification.getDes());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.scheduleList.size();
    }

    public static class ScheduleStatDayViewHolder extends RecyclerView.ViewHolder {
        TextView txtSchedule, txtConfirm, txtReason;

        public ScheduleStatDayViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSchedule = itemView.findViewById(R.id.txtSchedule);
            txtConfirm = itemView.findViewById(R.id.txtConfirm);
            txtReason = itemView.findViewById(R.id.txtReason);
        }
    }
}
