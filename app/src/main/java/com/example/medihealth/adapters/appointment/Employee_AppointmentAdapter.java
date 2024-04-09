package com.example.medihealth.adapters.appointment;

import android.content.Context;
import android.util.Log;
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
import com.example.medihealth.models.NotificationModel;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

public class Employee_AppointmentAdapter extends FirestoreRecyclerAdapter<Appointment, Employee_AppointmentAdapter.AppointmentViewHolder> {
    Context context;
    private OnItemClickListener mListener;
    public Employee_AppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull Appointment model) {
        if(model.getStateAppointment() == 1){
            holder.btnApprove.setVisibility(View.GONE);
        }
        if(model.getStateAppointment() == -1){
            holder.blockOne.setVisibility(View.GONE);
            holder.blockTwo.setVisibility(View.VISIBLE);
        }

        UserModel userModel = model.getUserModel();
        if (userModel != null) {
            holder.userName.setText(userModel.getFullName());
            holder.userPhonenumber.setText(userModel.getPhoneNumber());
        } else {
            holder.userName.setText("");
            holder.userPhonenumber.setText("");
        }

        String calendarStr = "STT"+" "+ model.getOrder()+" - "+model.getAppointmentDate();
        holder.calendar.setText(calendarStr);
        Doctor doctor = model.getDoctor();
        if (doctor != null) {
            holder.doctorName.setText(doctor.getFullName());
        } else {
            holder.doctorName.setText("");
        }
        String appointmentId = getSnapshots().getSnapshot(position).getId();
        holder.itemView.setTag(appointmentId);
    }
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_appointment_employee,parent,false);
        return new Employee_AppointmentAdapter.AppointmentViewHolder(itemView);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName, userPhonenumber, calendar, doctorName;
        RelativeLayout blockOne, blockTwo, btnApprove, btnCancel, btnRestore;
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
            btnApprove.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            btnRestore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String appointmentId = (String) itemView.getTag();
            if (v.getId() == R.id.block_approve_appointment){
                updateStateAppointment(appointmentId,1);
                getAppointmentById(appointmentId);
            }
            if (v.getId() == R.id.block_cancel_appointment){
                updateStateAppointment(appointmentId,-1);
            }
            if (v.getId() == R.id.block_restore_appointment){
                updateStateAppointment(appointmentId,1);
            }
        }

        private void getAppointmentById(String appointmentId) {
            FirebaseUtil.getAppointmentDetailsById(appointmentId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Appointment appointment = task.getResult().toObject(Appointment.class);
                    if (appointment != null){
                        pushNotification(appointment);
                    }
                }
                else {
                    Log.e("ERROR","Lỗi kết nối");
                }
            });
        }

        private void pushNotification(Appointment appointment) {
            String title = "Đặt lịch khám thành công";
            String body = "Chúc mừng bạn đã đặt lịch khám thành công tại Medihealth";
            boolean seen = false;
            Timestamp timestamp = Timestamp.now();
            String userId = appointment.getUserModel().getUserId();
            NotificationModel notificationModel = new NotificationModel(title,body,timestamp,seen,userId);
            FirebaseUtil.getNotificationsCollectionReference().add(notificationModel).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("SUCCESSFULL", "Lưu thành công Notification");
                }
                else {
                    Log.e("ERROR","Lỗi kết nối");
                }
            });
        }

        private void updateStateAppointment(String appointmentId,int state) {
            FirebaseUtil.getAppointmentDetailsById(appointmentId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Appointment appointment = task.getResult().toObject(Appointment.class);
                            appointment.setStateAppointment(state);
                            FirebaseUtil.getAppointmentDetailsById(appointmentId).set(appointment)
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            Log.e("SUCCESSFULL","Update lịch hẹn thành công");
                                        }
                                        else {
                                            Log.e("ERROR","Lỗi kết nối mạng");
                                        }
                                    });
                        }
                        else {
                            Log.e("ERROR","Lỗi kết nối mạng");
                        }
                    });
        }
    }
    public interface OnItemClickListener {
        void onApproveClick(String appointmentId);
        void onCancelClick(String appointmentId);
        void onRestoreClick(String appointmentId);
    }
}
