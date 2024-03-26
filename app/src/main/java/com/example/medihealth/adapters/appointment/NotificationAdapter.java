package com.example.medihealth.adapters.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.models.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel,NotificationAdapter.NotificationViewHolder> {

    Context context;
    private INotificationViewHolder iViewHolder;
    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options,Context context,
                               INotificationViewHolder iViewHolder) {
        super(options);
        this.context = context;
        this.iViewHolder = iViewHolder;
    }

    public void setiViewHolder(INotificationViewHolder iViewHolder) {
        this.iViewHolder = iViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull NotificationModel model) {
        holder.textTitle.setText(model.getTitle());
        holder.textTime.setText(timestampToString(model.getTimestamp()));
        if (!model.isSeen()){
            holder.stateNew.setVisibility(View.VISIBLE);
        }
        else holder.stateNew.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                iViewHolder.onClickItem(position, documentSnapshot);
            }
        });
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new NotificationAdapter.NotificationViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textTime;
        RelativeLayout stateNew;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.title);
            textTime = itemView.findViewById(R.id.time);
            stateNew = itemView.findViewById(R.id.state_news);
        }
    }
    public static String timestampToString(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }
    public interface INotificationViewHolder{
        void onClickItem(int positon, DocumentSnapshot documentSnapshot);
        void onDataLoaded(int size);
    }
}
