package com.example.medihealth.adapters.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activites.chat.UserChat_Activity;
import com.example.medihealth.adapters.appointment.TimeAdapter;
import com.example.medihealth.models.Employee;
import com.example.medihealth.utils.AndroidUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeeAdapter extends FirestoreRecyclerAdapter<Employee,EmployeeAdapter.EmployeeViewHolder> {
    Context context;
    public EmployeeAdapter(@NonNull FirestoreRecyclerOptions<Employee> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position, @NonNull Employee model) {
        holder.fullName.setText(model.getFullName());
        FirebaseFirestore.getInstance().collection("state").document(model.getUserId())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        boolean state = documentSnapshot.getBoolean("isState");
                        if (state) {
                            holder.iconstate.setBackgroundResource(R.drawable.custom_olline);
                            holder.textSate.setText("Online");
                        } else {
                            holder.iconstate.setBackgroundResource(R.drawable.custom_offline);
                            holder.textSate.setText("Offline");
                        }
                    } else {
                        Log.e("ERROR","ERROR");
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserChat_Activity.class);
                AndroidUtil.passEmployeeModelAsIntent(intent,model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.infor_employee,parent,false);
        return new EmployeeViewHolder(itemView);
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        RelativeLayout iconstate;
        TextView fullName,textSate;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture_employee);
            fullName = itemView.findViewById(R.id.name_employee);
            iconstate = itemView.findViewById(R.id.icon);
            textSate = itemView.findViewById(R.id.state);
        }
    }
}
