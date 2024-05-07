package com.example.medihealth.adapters.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Doctor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DoctorAdapter extends FirestoreRecyclerAdapter<Doctor, DoctorAdapter.DoctorViewHolder> {
    Context context;
    private IDoctorViewHolder iViewHolder;
    private int selectedPosition = RecyclerView.NO_POSITION;
    SharedPreferences sharedPreferences;

    public DoctorAdapter(@NonNull FirestoreRecyclerOptions<Doctor> options, Context context, IDoctorViewHolder iDoctorViewHolder) {
        super(options);
        this.context = context;
        this.iViewHolder = iDoctorViewHolder;
        sharedPreferences = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
    }

    public void setiViewHolder(IDoctorViewHolder iViewHolder) {
        this.iViewHolder = iViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Doctor model) {
        holder.fullName.setText(model.getFullName());
        if (model.getGender().equals("Nam") || model.getGender().equals("")) {
            holder.imageDoctor.setImageResource(R.drawable.doctor);
        } else {
            holder.imageDoctor.setImageResource(R.drawable.doctor2);
        }

        int selectedPosition = sharedPreferences.getInt("indexDoctorSelected", 0);
        if (position == selectedPosition) {
            setBackgroundItem(holder);
        } else {
            holder.itemView.setBackground(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = sharedPreferences.getInt("indexDoctorSelected", RecyclerView.NO_POSITION);
                setIndexSelectedSharedPreferences("indexDoctorSelected", position);
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(position);
                iViewHolder.onClickItem(position);
            }
        });
    }

    private void setBackgroundItem(DoctorViewHolder holder) {
        Drawable selectedBackground = ContextCompat.getDrawable(context, R.drawable.custom_form_doctor);
        holder.itemView.setBackground(selectedBackground);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_pic_view, parent, false);
        return new DoctorViewHolder(itemView);
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView fullName;
        ImageView imageDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName_doctor);
            imageDoctor = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }

    public interface IDoctorViewHolder {
        void onClickItem(int positon);

        void onDataLoaded(int size);
    }

    private void setIndexSelectedSharedPreferences(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
