package com.example.medihealth.activities.show_schedule_totay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.activities.MainActivity;
import com.example.medihealth.adapters.show_schedule_today.ScheduleApdapter;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.apiservices.ScheduleService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleTodayActivity extends AppCompatActivity{
    private RecyclerView scheRecyclerView;
    private ScheduleApdapter adapter;
    private ImageView imgBack;
    private List<Schedule> scheduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_schedule);

        imgBack = findViewById(R.id.image_back);
        scheRecyclerView = findViewById(R.id.scheRecyclerView);
        scheRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleApdapter(scheduleList, new ScheduleApdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Schedule schedule){
                Intent intent = new Intent(ScheduleTodayActivity.this, ScheduleTodayDetailActivity.class);
                intent.putExtra("schedule", schedule);
                startActivity(intent);
            }
        });
        scheRecyclerView.setAdapter(adapter);

        ScheduleService scheduleService = RetrofitClient.createService(ScheduleService.class);
        scheduleService.getScheduleToday("12345").enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if(response.isSuccessful() && response.body() != null && !response.body().isEmpty()){
                    scheduleList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.d("API_Response", "Data received from API: " + response.body().toString());
                }else{
                    Log.e("API_Error", "Response body is empty or null");
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleTodayActivity.this, MainActivity.class));
            }
        });
    }
}
