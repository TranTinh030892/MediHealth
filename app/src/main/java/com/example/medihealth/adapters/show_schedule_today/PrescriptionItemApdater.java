package com.example.medihealth.adapters.show_schedule_today;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.PrescriptionItem;

import java.util.List;

public class PrescriptionItemApdater extends RecyclerView.Adapter<PrescriptionItemApdater.PrescriptionItemViewHolder> {

    private List<PrescriptionItem> prescriptionItemList;

    public PrescriptionItemApdater(List<PrescriptionItem> prescriptionItemList) {
        this.prescriptionItemList = prescriptionItemList;
    }

    @NonNull
    @Override
    public PrescriptionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_item, parent, false);
        return new PrescriptionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionItemViewHolder holder, int position) {
        PrescriptionItem prescriptionItem = prescriptionItemList.get(position);
        holder.txtDrugName.setText(prescriptionItem.getName());
        holder.txtNote.setText(prescriptionItem.getNote());
    }

    @Override
    public int getItemCount() {
        return prescriptionItemList.size();
    }

    public static class PrescriptionItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtDrugName, txtNote;

        public PrescriptionItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDrugName = itemView.findViewById(R.id.txtDrugName);
            txtNote = itemView.findViewById(R.id.txtNote);
        }
    }
}
