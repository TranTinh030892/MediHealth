package com.example.medihealth.adapters.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;

import com.example.medihealth.models.Drug;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrugAdapter extends FirestoreRecyclerAdapter<Drug,DrugAdapter.DrugViewHolder> {

    Context context;
    private DrugAdapter.IDrugViewHolder iViewHolder;
    public DrugAdapter(@NonNull FirestoreRecyclerOptions<Drug> options, Context context, DrugAdapter.IDrugViewHolder iViewHolder) {
        super(options);
        this.context = context;
        this.iViewHolder = iViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull DrugViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Drug model) {
        holder.name.setText(model.getName());
        holder.active.setText("Hoạt chất:      "+model.getName()+" "+generateRandom(1)+"00mg");
        holder.number.setText("Số ĐK:             GC-"+generateRandom(3)+"-"+generateRandom(2));

        List<String> listImage = new ArrayList<>();
        listImage.add("drug");listImage.add("drug1");listImage.add("drug2");
        listImage.add("drug3");listImage.add("drug4");listImage.add("drug1");listImage.add("drug2");
        listImage.add("drug3");listImage.add("drug4");listImage.add("drug1");
        int index = generateRandom(1);
        String imageName = listImage.get(index);

        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        int imageResourceId = resources.getIdentifier(imageName, "drawable", packageName);

        holder.imageDrug.setImageResource(imageResourceId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iViewHolder.onClickItem(position,imageName);
            }
        });
    }
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    @NonNull
    @Override
    public DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_item,parent,false);
        return new DrugAdapter.DrugViewHolder(itemView);
    }
    public void clear() {
        getSnapshots().clear();
        notifyDataSetChanged();
    }
    public static class DrugViewHolder extends RecyclerView.ViewHolder{
        TextView name,active,number;
        ImageView imageDrug;
        public DrugViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            active = itemView.findViewById(R.id.text2);
            imageDrug = itemView.findViewById(R.id.image);
            number = itemView.findViewById(R.id.text3);
        }
    }
    public interface IDrugViewHolder{
        void onClickItem(int positon,String imageName);
        void onDataLoaded(int size);
    }
    private int generateRandom(int numberOfDigits) {
        if (numberOfDigits <= 0) {
            throw new IllegalArgumentException("Số lượng chữ số phải lớn hơn 0");
        }
        int lowerBound = (int) Math.pow(10, numberOfDigits - 1);
        int upperBound = (int) Math.pow(10, numberOfDigits) - 1;
        Random random = new Random();
        int randomNumber = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
        return randomNumber;
    }
}
