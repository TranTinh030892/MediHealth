package com.example.medihealth.adapters.appointment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Customer_AppoitnmentAdapter extends FirestoreRecyclerAdapter<Appointment, Customer_AppoitnmentAdapter.AppointmentViewHolder> {
    Context context;

    public Customer_AppoitnmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull Appointment model) {
        UserModel userModel = model.getUserModel();
        Relative relative = model.getRelative();
        if (userModel != null && relative == null) {
            holder.userName.setText(userModel.getFullName());
        } else if (userModel == null && relative != null) {
            holder.userName.setText(relative.getFullName());
        } else holder.userName.setText("");
        setColorStateAppoiment(model, holder);

        String calendarStr = model.getTime() + " - " + model.getAppointmentDate();
        holder.calendar.setText(calendarStr);
        Doctor doctor = model.getDoctor();
        if (doctor != null) {
            holder.doctorName.setText("BS." + doctor.getFullName());
        } else {
            holder.doctorName.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_appontment_customer, parent, false);
        return new Customer_AppoitnmentAdapter.AppointmentViewHolder(itemView);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView userName, status, calendar, doctorName;
        RelativeLayout blockStateAppointment;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.fullName_user);
            status = itemView.findViewById(R.id.status_appointment);
            calendar = itemView.findViewById(R.id.calendar_appointment);
            doctorName = itemView.findViewById(R.id.fullName_doctor_appoitment);
            blockStateAppointment = itemView.findViewById(R.id.block_state_appointment);
        }
    }

    private void setColorStateAppoiment(Appointment appoiment, AppointmentViewHolder holder) {
        int bgColorState0 = ContextCompat.getColor(context, R.color.colorStatePending);
        int textColorState0 = ContextCompat.getColor(context, R.color.Red);
        int bgColorState1 = ContextCompat.getColor(context, R.color.colorStateApproved);
        int textColorState1 = ContextCompat.getColor(context, R.color.chat_color_receiver);
        int bgColorState2 = ContextCompat.getColor(context, R.color.chat_color_sender);
        int textColorState2 = ContextCompat.getColor(context, R.color.white);
        int bgColorState3 = ContextCompat.getColor(context, R.color.pink);
        if (appoiment.getStateAppointment() == 1) {
            holder.blockStateAppointment.setBackgroundTintList(ColorStateList.valueOf(bgColorState1));
            holder.status.setTextColor(ColorStateList.valueOf(textColorState1));
            holder.status.setText("Chờ khám");
        }
        if (appoiment.getStateAppointment() == 0) {
            holder.blockStateAppointment.setBackgroundTintList(ColorStateList.valueOf(bgColorState0));
            holder.status.setTextColor(ColorStateList.valueOf(textColorState0));
            holder.status.setText("Chờ duyệt");
        }
        if (appoiment.getStateAppointment() == 2) {
            holder.blockStateAppointment.setBackgroundTintList(ColorStateList.valueOf(bgColorState2));
            holder.status.setTextColor(ColorStateList.valueOf(textColorState2));
            holder.status.setText("Đã khám");
        }
        if (appoiment.getStateAppointment() == -1) {
            holder.blockStateAppointment.setBackgroundTintList(ColorStateList.valueOf(bgColorState3));
            holder.status.setTextColor(ColorStateList.valueOf(textColorState0));
            holder.status.setText("Đã hủy");
        }
    }
}
