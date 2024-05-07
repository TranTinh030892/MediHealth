package com.example.medihealth.adapters.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Employee_AppointmentAdapter extends FirestoreRecyclerAdapter<Appointment, Employee_AppointmentAdapter.AppointmentViewHolder> {
    Context context;
    private OnItemClickListener mListener;

    public Employee_AppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options, Context context, OnItemClickListener onItemClickListener) {
        super(options);
        this.context = context;
        this.mListener = onItemClickListener;
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull Appointment model) {
        if (model.getStateAppointment() == 1) {
            holder.btnApprove.setVisibility(View.GONE);
        }
        if (model.getStateAppointment() == -1) {
            holder.blockOne.setVisibility(View.GONE);
            holder.blockTwo.setVisibility(View.VISIBLE);
        }

        UserModel userModel = model.getUserModel();
        Relative relative = model.getRelative();
        if (userModel != null && relative == null) {
            holder.userName.setText(userModel.getFullName());
            holder.userPhonenumber.setText(userModel.getPhoneNumber());
        } else if (userModel == null && relative != null) {
            holder.userName.setText(relative.getFullName());
            holder.userPhonenumber.setText(relative.getPhoneNumber());
        } else {
            holder.userName.setText("");
            holder.userPhonenumber.setText("");
        }

        String calendarStr = model.getTime() + " - " + model.getAppointmentDate();
        holder.calendar.setText(calendarStr);
        Doctor doctor = model.getDoctor();
        if (doctor != null) {
            holder.doctorName.setText("BS." + doctor.getFullName());
        } else {
            holder.doctorName.setText("");
        }
        String appointmentId = getSnapshots().getSnapshot(position).getId();
        holder.itemView.setTag(appointmentId);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteClick(appointmentId);
            }
        });
        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onApproveClick(appointmentId);
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick(appointmentId);
            }
        });
        holder.btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRestoreClick(appointmentId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_appointment_employee, parent, false);
        return new Employee_AppointmentAdapter.AppointmentViewHolder(itemView);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userPhonenumber, calendar, doctorName;
        RelativeLayout blockOne, blockTwo, btnApprove, btnCancel, btnRestore, btnDelete;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            blockOne = itemView.findViewById(R.id.block_one);
            blockTwo = itemView.findViewById(R.id.block_two);
            userName = itemView.findViewById(R.id.fullName_user);
            userPhonenumber = itemView.findViewById(R.id.phoneNumber_user);
            calendar = itemView.findViewById(R.id.calendar_appointment);
            doctorName = itemView.findViewById(R.id.fullName_doctor_appoitment);
            btnApprove = itemView.findViewById(R.id.block_approve_appointment);
            btnCancel = itemView.findViewById(R.id.block_cancel_appointment);
            btnRestore = itemView.findViewById(R.id.block_restore_appointment);
            btnDelete = itemView.findViewById(R.id.block_delete_appointment);
        }
    }

    public interface OnItemClickListener {
        void onApproveClick(String appointmentId);

        void onCancelClick(String appointmentId);

        void onRestoreClick(String appointmentId);

        void onDeleteClick(String appointmentId);
    }
}
