package com.example.medihealth.adapters.book_appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Doctor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DoctorAdapter extends FirestoreRecyclerAdapter<Doctor,DoctorAdapter.DoctorViewHolder> {
    Context context;
    private IDoctorViewHolder iViewHolder;
    public DoctorAdapter(@NonNull FirestoreRecyclerOptions<Doctor> options, Context context, IDoctorViewHolder iDoctorViewHolder) {
        super(options);
        this.context = context;
        this.iViewHolder = iDoctorViewHolder;
    }

    public void setiViewHolder(IDoctorViewHolder iViewHolder) {
        this.iViewHolder = iViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull DoctorViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Doctor model) {
        holder.fullName.setText(model.getFullName());
        if (model.getGender().equals("Nam") || model.getGender().equals("")){
            holder.imageDoctor.setImageResource(R.drawable.doctor);
        }
        else holder.imageDoctor.setImageResource(R.drawable.doctor2);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iViewHolder.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (iViewHolder != null) {
            iViewHolder.onDataLoaded(getItemCount());
        }
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_pic_view,parent,false);
        return new DoctorViewHolder(itemView);
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder{
        TextView fullName;
        ImageView imageDoctor;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName_doctor);
            imageDoctor = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }

    public interface IDoctorViewHolder{
        void onClickItem(int positon);
        void onDataLoaded(int size);
    }
}
