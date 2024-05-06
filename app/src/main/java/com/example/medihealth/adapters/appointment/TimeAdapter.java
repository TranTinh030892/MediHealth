package com.example.medihealth.adapters.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    Context context;
    private List<String> timeList;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private TimeAdapter.ITimeViewHolder iViewHolder;
    SharedPreferences sharedPreferences;

    public TimeAdapter(Context context, List<String> timeList, ITimeViewHolder iViewHolder) {
        this.context = context;
        this.timeList = timeList;
        this.iViewHolder = iViewHolder;
        sharedPreferences = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
    }

    public void setiViewHolder(ITimeViewHolder iViewHolder) {
        this.iViewHolder = iViewHolder;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_time_item, parent, false);
        return new TimeViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String timeStr = timeList.get(position);
        holder.textTime.setText(timeStr);
        int selectedPosition = sharedPreferences.getInt("indexTimeSelected", -1);
        if (position == selectedPosition) {
            setBackgroundItem(holder);
        } else {
            setBackgroundNotSelectedItem(holder);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = sharedPreferences.getInt("indexTimeSelected", RecyclerView.NO_POSITION);
                setIndexSelectedSharedPreferences("indexTimeSelected", position);
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(position);
                iViewHolder.onClickItem(position);
            }
        });
    }

    public void clearData() {
        if (timeList != null) {
            timeList.clear();
        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView textTime;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.time);
        }
    }

    public interface ITimeViewHolder {
        void onClickItem(int positon);
    }

    private void setBackgroundItem(TimeViewHolder holder) {
        Drawable selectedBackground = ContextCompat.getDrawable(context, R.drawable.custom_form_time_selected);
        holder.itemView.setBackground(selectedBackground);
    }

    private void setBackgroundNotSelectedItem(TimeViewHolder holder) {
        Drawable selectedBackground = ContextCompat.getDrawable(context, R.drawable.custom_form_time_notselected);
        holder.itemView.setBackground(selectedBackground);
    }

    private void setIndexSelectedSharedPreferences(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
