package com.example.medihealth.adapters.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Relative;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RelativeAdapter extends FirestoreRecyclerAdapter<Relative, RelativeAdapter.RelativeViewHolder> {
    Context context;
    private IRelativeViewHolder iViewHolder;
    public RelativeAdapter(@NonNull FirestoreRecyclerOptions<Relative> options,Context context, IRelativeViewHolder iRelativeViewHolder) {
        super(options);
        this.context = context;
        this.iViewHolder = iRelativeViewHolder;
    }

    public void setiViewHolder(IRelativeViewHolder iViewHolder) {
        this.iViewHolder = iViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull RelativeViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Relative model) {
        holder.accountFullname.setText(model.getFullName());
        holder.accountBirth.setText("Ngày sinh: "+model.getBirth());
        holder.relationship.setText(model.getRelationship());
        holder.accountPhoneNumber.setText("Điện thoại: "+model.getPhoneNumber());
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
    @NonNull
    @Override
    public RelativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.relative_item,parent,false);
        return new RelativeAdapter.RelativeViewHolder(itemView);
    }

    public static class RelativeViewHolder extends RecyclerView.ViewHolder{
        TextView accountFullname,accountBirth,accountPhoneNumber,relationship;
        public RelativeViewHolder(@NonNull View itemView) {
            super(itemView);
            accountFullname = itemView.findViewById(R.id.fullName);
            accountBirth = itemView.findViewById(R.id.birth);
            accountPhoneNumber = itemView.findViewById(R.id.phoneNumber);
            relationship = itemView.findViewById(R.id.relationship);
        }
    }
    public interface IRelativeViewHolder{
        void onClickItem(int positon);
        void onDataLoaded(int size);
    }
}
