package com.example.medihealth.adapters.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.Relative;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RelativeAdapter extends FirestoreRecyclerAdapter<Relative,RelativeAdapter.RelativeViewHolder> {
    Context context;
    private RelativeAdapter.IRelativeViewHolder iViewHolder;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public RelativeAdapter(@NonNull FirestoreRecyclerOptions<Relative> options,Context context,IRelativeViewHolder iViewHolder) {
        super(options);
        this.context = context;
        this.iViewHolder = iViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull RelativeViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Relative model) {
        holder.name.setText(getNameFromFullName(model.getFullName()));
        holder.detail.setText(model.getRelationship());
        if (position == selectedPosition) {
            setBackgroundItem(holder.formRelative);
            holder.tick.setVisibility(View.VISIBLE);
        } else {
            setBackgroundNotSelectedItem(holder.formRelative);
            holder.tick.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
                iViewHolder.onClickItem(position);
            }
        });
    }

    private void setBackgroundNotSelectedItem(RelativeLayout formRelative) {
        Drawable selectedBackground = ContextCompat.getDrawable(context, R.drawable.custom_form_person_notselected);
        formRelative.setBackground(selectedBackground);
    }

    private void setBackgroundItem(RelativeLayout formRelative) {
        Drawable selectedBackground = ContextCompat.getDrawable(context, R.drawable.custom_form_person_selected);
        formRelative.setBackground(selectedBackground);
    }

    @NonNull
    @Override
    public RelativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item,parent,false);
        return new RelativeAdapter.RelativeViewHolder(itemView);
    }

    public static class RelativeViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout formRelative,tick;
        TextView name,detail;
        public RelativeViewHolder(@NonNull View itemView) {
            super(itemView);
            formRelative = itemView.findViewById(R.id.form_relative);
            name = itemView.findViewById(R.id.name);
            detail = itemView.findViewById(R.id.btnDetail);
            tick = itemView.findViewById(R.id.tick);
        }
    }
    private String getNameFromFullName(String fullName){
        String[]str = fullName.split(" ");
        return str[str.length-1];
    }
    public interface IRelativeViewHolder{
        void onClickItem(int positon);
        void onDataLoaded(int size);
    }
    public void clearBackgroundItems() {
        selectedPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }
}
