package com.example.medihealth.adapters.prescription_schedule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activities.prescription_schedule.SwipeItemViewHolder;
import com.example.medihealth.models.PrescriptionItem;

import java.util.List;

public class PrescriptionItemAdapter extends RecyclerView.Adapter<PrescriptionItemAdapter.PrescriptionItemViewHolder> {

    List<PrescriptionItem> prescriptionItems;


    @SuppressLint("NotifyDataSetChanged")
    public PrescriptionItemAdapter(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrescriptionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_detail_item, parent, false);
        return new PrescriptionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionItemViewHolder holder, int position) {
        PrescriptionItem prescriptionItem = prescriptionItems.get(position);
        if (prescriptionItem == null) return;
        holder.name.setText(prescriptionItem.getName());
        holder.note.setText(prescriptionItem.getNote());
    }

    @Override
    public int getItemCount() {
        if (prescriptionItems != null) {
            return prescriptionItems.size();
        }
        return 0;
    }

    public static class PrescriptionItemViewHolder extends SwipeItemViewHolder {

        TextView name, note;

        public PrescriptionItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            note = itemView.findViewById(R.id.tv_note);
            layoutForeground = itemView.findViewById(R.id.layout_foreground);
        }
    }
}
