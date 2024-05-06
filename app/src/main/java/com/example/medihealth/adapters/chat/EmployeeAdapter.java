package com.example.medihealth.adapters.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activities.chat.UserChat_Activity;
import com.example.medihealth.models.Employee;
import com.example.medihealth.utils.AndroidUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EmployeeAdapter extends FirestoreRecyclerAdapter<Employee, EmployeeAdapter.EmployeeViewHolder> {
    Context context;

    public EmployeeAdapter(@NonNull FirestoreRecyclerOptions<Employee> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position, @NonNull Employee model) {
        holder.fullName.setText(model.getFullName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserChat_Activity.class);
                AndroidUtil.passEmployeeModelAsIntent(intent, model);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.infor_employee, parent, false);
        return new EmployeeViewHolder(itemView);
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView fullName;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture_employee);
            fullName = itemView.findViewById(R.id.name_employee);
        }
    }
}
