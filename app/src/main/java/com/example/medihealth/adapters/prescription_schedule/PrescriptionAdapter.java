package com.example.medihealth.adapters.prescription_schedule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activitys.prescription_schedule.CustomOnClickListener;
import com.example.medihealth.models.Prescription;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {

    List<Prescription> prescriptions;
    CustomOnClickListener<Prescription> onClickListener;
    CustomOnClickListener<Prescription> onClickSwitchListener;

    public PrescriptionAdapter(List<Prescription> prescriptions, CustomOnClickListener<Prescription> onClickListener) {
        this.prescriptions = prescriptions;
        this.onClickListener = onClickListener;
    }

    public PrescriptionAdapter(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void setOnClickListener(CustomOnClickListener<Prescription> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnClickSwitchListener(CustomOnClickListener<Prescription> onClickSwitchListener) {
        this.onClickSwitchListener = onClickSwitchListener;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_item, parent, false);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Prescription prescription = prescriptions.get(position);
        if (prescription == null) return;
        holder.title.setText(prescription.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(prescription);
            }
        });

        holder.status.setChecked(prescription.isActive());
        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prescription.setActive(isChecked);
                onClickSwitchListener.onClick(prescription);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (prescriptions == null) {
            return 0;
        }
        return prescriptions.size();
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public static class PrescriptionViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        Switch status;

        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_prescription_title);
            status = itemView.findViewById(R.id.sw_prescription_status);
        }
    }

}
