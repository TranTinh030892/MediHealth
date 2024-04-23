package com.example.medihealth.activities.stat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.activities.MainActivity;

public class StatHomeActivity extends AppCompatActivity {
    private EditText editTextStat;
    private Button btnContinue;
    private ImageView imgBack;
    private boolean active_btnContinue;
    private int status_selectStat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_home);

        editTextStat = findViewById(R.id.editTextStat);
        btnContinue = findViewById(R.id.button_continue);
        imgBack = findViewById(R.id.image_back);
        active_btnContinue = false;
        btnContinue.setBackgroundResource(R.drawable.button_background_notactive);
        btnContinue.setTextColor(getResources().getColor(R.color.black));

        editTextStat.setText("-- Chọn loại thống kê --");
        editTextStat.setTextColor(getResources().getColor(R.color.baclground_cancel));
        editTextStat.setBackgroundResource(R.drawable.background_ic_chervon_down);
        editTextStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.menu_stat_home, null);

                final PopupWindow popupWindow = new PopupWindow(popupView, editTextStat.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);

                TextView statDay = popupView.findViewById(R.id.menu_item_1);
                TextView statWeek = popupView.findViewById(R.id.menu_item_2);
                TextView statMonth = popupView.findViewById(R.id.menu_item_3);

                statDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextStat.setText("Thống kê theo ngày");
                        active_btnContinue = true;
                        status_selectStat = 1;
                        editTextStat.setTextColor(getResources().getColor(R.color.black));
                        btnContinue.setBackgroundResource(R.drawable.button_background_active);
                        btnContinue.setTextColor(getResources().getColor(R.color.white));
                        popupWindow.dismiss();
                    }
                });

                statWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextStat.setText("Thống kê theo tuần");
                        active_btnContinue = true;
                        status_selectStat = 2;
                        editTextStat.setTextColor(getResources().getColor(R.color.black));
                        btnContinue.setBackgroundResource(R.drawable.button_background_active);
                        btnContinue.setTextColor(getResources().getColor(R.color.white));
                        popupWindow.dismiss();
                    }
                });

                statMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextStat.setText("Thống kê theo tháng");
                        active_btnContinue = true;
                        status_selectStat = 3;
                        editTextStat.setTextColor(getResources().getColor(R.color.black));
                        btnContinue.setBackgroundResource(R.drawable.button_background_active);
                        btnContinue.setTextColor(getResources().getColor(R.color.white));
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        editTextStat.setBackgroundResource(R.drawable.background_ic_chervon_down);
                    }
                });

                popupWindow.showAsDropDown(editTextStat);
                editTextStat.setBackgroundResource(R.drawable.background_ic_chervon_up);
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(active_btnContinue){
                    Intent intent;
                    switch (status_selectStat) {
                        case 1:
                            intent = new Intent(StatHomeActivity.this, StatDayActivity.class);
                            break;
                        case 2:
                            intent = new Intent(StatHomeActivity.this, StatWeeksActivity.class);
                            break;
                        case 3:
                            intent = new Intent(StatHomeActivity.this, StatMonthActivity.class);
                            break;
                        default:
                            return;
                    }
                    startActivity(intent);
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatHomeActivity.this, MainActivity.class));
            }
        });
    }

}
