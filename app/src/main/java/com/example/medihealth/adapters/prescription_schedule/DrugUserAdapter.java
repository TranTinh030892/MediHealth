package com.example.medihealth.adapters.prescription_schedule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activitys.prescription_schedule.CustomOnClickListener;
import com.example.medihealth.activitys.prescription_schedule.DrugUserManagement;
import com.example.medihealth.models.DrugUser;

import java.util.List;

public class DrugUserAdapter extends RecyclerView.Adapter<DrugUserAdapter.DrugUserViewHolder> {

    List<DrugUser> drugUsers;
    CustomOnClickListener<DrugUser> onClickListener;
    CustomOnClickListener<DrugUser> onClickEditListener;
    CustomOnClickListener<DrugUser> onClickDeleteListener;

    public DrugUserAdapter(List<DrugUser> drugUsers, CustomOnClickListener<DrugUser> onClickListener) {
        this.drugUsers = drugUsers;
        this.onClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<DrugUser> drugUsers) {
        this.drugUsers = drugUsers;
        notifyDataSetChanged();
    }

    public void setOnClickListener(CustomOnClickListener<DrugUser> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnClickEditListener(CustomOnClickListener<DrugUser> onClickEditListener) {
        this.onClickEditListener = onClickEditListener;
    }

    public void setOnClickDeleteListener(CustomOnClickListener<DrugUser> onClickDeleteListener) {
        this.onClickDeleteListener = onClickDeleteListener;
    }

    @NonNull
    @Override
    public DrugUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drug_user_item, parent, false);
        return new DrugUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugUserViewHolder holder, int position) {
        final DrugUser drugUser = drugUsers.get(position);
        if (drugUser == null) return;
        holder.no.setText(String.valueOf(position + 1));
        holder.name.setText(drugUser.getName());

        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(drugUser);
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(drugUser);
            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, drugUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (drugUsers != null) {
            return drugUsers.size();
        }
        return 0;
    }

    public static class DrugUserViewHolder extends RecyclerView.ViewHolder {

        TextView no;
        TextView name;
        ImageView btnMore;

        public DrugUserViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.tv_drug_user_no);
            name = itemView.findViewById(R.id.tv_drug_user_name);
            btnMore = itemView.findViewById(R.id.iv_drug_user_more);
        }
    }

    private void showPopupMenu(View view, DrugUser drugUser) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_drug_user);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_edit_drug_user) {
                    onClickEditListener.onClick(drugUser);
                    return true;
                }
                if (item.getItemId() == R.id.menu_delete_drug_user) {
                    onClickDeleteListener.onClick(drugUser);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
