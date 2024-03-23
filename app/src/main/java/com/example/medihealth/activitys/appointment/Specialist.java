package com.example.medihealth.activitys.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;

import java.util.ArrayList;
import java.util.List;

public class Specialist extends AppCompatActivity implements View.OnClickListener {
    EditText textSearch;
    ImageButton btnBack, btnSearch;
    Button btnSpecialist1,btnSpecialist2,btnSpecialist3, btnSpecialist4, btnSpecialist5,
            btnSpecialist6,btnSpecialist7,btnSpecialist8,btnSpecialist9,btnSpecialist10,
    btnSpecialist11,btnSpecialist12,btnSpecialist13,btnSpecialist14,btnSpecialist15,btnSpecialist16,
            btnSpecialist17,btnSpecialist18,btnSpecialist19,btnSpecialist20;
    List<Button>listButtons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist);
        initView();
        initOnClick();
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                getResult(newText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void initOnClick() {
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnSpecialist1.setOnClickListener(this);
        btnSpecialist2.setOnClickListener(this);
        btnSpecialist3.setOnClickListener(this);
        btnSpecialist4.setOnClickListener(this);
        btnSpecialist5.setOnClickListener(this);
        btnSpecialist6.setOnClickListener(this);
        btnSpecialist7.setOnClickListener(this);
        btnSpecialist8.setOnClickListener(this);
        btnSpecialist9.setOnClickListener(this);
        btnSpecialist10.setOnClickListener(this);
        btnSpecialist11.setOnClickListener(this);
        btnSpecialist12.setOnClickListener(this);
        btnSpecialist13.setOnClickListener(this);
        btnSpecialist14.setOnClickListener(this);
        btnSpecialist15.setOnClickListener(this);
        btnSpecialist16.setOnClickListener(this);
        btnSpecialist17.setOnClickListener(this);
        btnSpecialist18.setOnClickListener(this);
        btnSpecialist19.setOnClickListener(this);
        btnSpecialist20.setOnClickListener(this);
    }

    private void initView() {
        btnBack = findViewById(R.id.back_btn);
        textSearch = findViewById(R.id.edit_seach_specialist);
        btnSearch = findViewById(R.id.search_specialist_btn);
        btnSpecialist1 = findViewById(R.id.specialist1);listButtons.add(btnSpecialist1);
        btnSpecialist2 = findViewById(R.id.specialist2);listButtons.add(btnSpecialist2);
        btnSpecialist3 = findViewById(R.id.specialist3);listButtons.add(btnSpecialist3);
        btnSpecialist4 = findViewById(R.id.specialist4);listButtons.add(btnSpecialist4);
        btnSpecialist5 = findViewById(R.id.specialist5);listButtons.add(btnSpecialist5);
        btnSpecialist6 = findViewById(R.id.specialist6);listButtons.add(btnSpecialist6);
        btnSpecialist7 = findViewById(R.id.specialist7);listButtons.add(btnSpecialist7);
        btnSpecialist8 = findViewById(R.id.specialist8);listButtons.add(btnSpecialist8);
        btnSpecialist9 = findViewById(R.id.specialist9);listButtons.add(btnSpecialist9);
        btnSpecialist10 = findViewById(R.id.specialist10);listButtons.add(btnSpecialist10);
        btnSpecialist11 = findViewById(R.id.specialist11);listButtons.add(btnSpecialist11);
        btnSpecialist12 = findViewById(R.id.specialist12);listButtons.add(btnSpecialist12);
        btnSpecialist13 = findViewById(R.id.specialist13);listButtons.add(btnSpecialist13);
        btnSpecialist14 = findViewById(R.id.specialist14);listButtons.add(btnSpecialist14);
        btnSpecialist15 = findViewById(R.id.specialist15);listButtons.add(btnSpecialist15);
        btnSpecialist16 = findViewById(R.id.specialist16);listButtons.add(btnSpecialist16);
        btnSpecialist17 = findViewById(R.id.specialist17);listButtons.add(btnSpecialist17);
        btnSpecialist18 = findViewById(R.id.specialist18);listButtons.add(btnSpecialist18);
        btnSpecialist19 = findViewById(R.id.specialist19);listButtons.add(btnSpecialist19);
        btnSpecialist20 = findViewById(R.id.specialist20);listButtons.add(btnSpecialist20);
    }

    @Override
    public void onClick(View v) {
        // click search
        if (v.getId() == R.id.search_specialist_btn){
            String searchStr = textSearch.getText().toString();
            getResult(searchStr);
            return;
        }
        // click select specialist
        Intent intent = new Intent(Specialist.this,Infor_Appoitment_Activity.class);
        String specialistSelected = "specialistSelected";
        intent.putExtra("requestCode",100);
        if(v.getId() == R.id.specialist4){
            intent.putExtra(specialistSelected,"Hô hấp");
        }
        else if(v.getId() == R.id.specialist6){
            intent.putExtra(specialistSelected,"Tim mạch");
        }
        else if(v.getId() == R.id.specialist1){
            intent.putExtra(specialistSelected,"Khám tổng quát");
        }
        else if(v.getId() == R.id.specialist2){
            intent.putExtra(specialistSelected,"Tai mũi họng");
        }
        else if(v.getId() == R.id.specialist3){
            intent.putExtra(specialistSelected,"Sản phụ khoa");
        }
        else if(v.getId() == R.id.specialist5){
            intent.putExtra(specialistSelected,"Răng hàm mặt");
        }
        else if(v.getId() == R.id.specialist7){
            intent.putExtra(specialistSelected,"Ngoại khoa");
        }
        else if(v.getId() == R.id.specialist8){
            intent.putExtra(specialistSelected,"Mắt");
        }
        else if(v.getId() == R.id.specialist9){
            intent.putExtra(specialistSelected,"Da liễu");
        }
        else if(v.getId() == R.id.specialist10){
            intent.putExtra(specialistSelected,"Nội khoa");
        }
        else if(v.getId() == R.id.specialist11){
            intent.putExtra(specialistSelected,"Thần kinh");
        }
        else if(v.getId() == R.id.specialist12){
            intent.putExtra(specialistSelected,"Chân tay miệng");
        }
        else if(v.getId() == R.id.specialist13){
            intent.putExtra(specialistSelected,"Nhi khoa");
        }
        else if(v.getId() == R.id.specialist14){
            intent.putExtra(specialistSelected,"Nội tiêu hóa");
        }
        else if(v.getId() == R.id.specialist15){
            intent.putExtra(specialistSelected,"Tiết niệu");
        }
        else if(v.getId() == R.id.specialist16){
            intent.putExtra(specialistSelected,"Thẩm mỹ");
        }
        else if(v.getId() == R.id.specialist17 || v.getId() == R.id.specialist18 ||
                v.getId() == R.id.specialist19 || v.getId() == R.id.specialist20){
            Toast.makeText(this, "Chọn chuyên khoa khác", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    private void getResult(String searchStr) {
        for (int i = 0 ; i < listButtons.size() ; i++){
            listButtons.get(i).setVisibility(View.VISIBLE);
            listButtons.get(i).setBackgroundResource(R.drawable.custom_button_specialist);
        }
        if(searchStr.equals(""))return; boolean check = false;
        for(int i = 0 ; i < listButtons.size() ; i++){
            String specialist = listButtons.get(i).getText().toString();
            if (!specialist.toLowerCase().contains(searchStr.toLowerCase())){
                listButtons.get(i).setVisibility(View.GONE);
            }
            else {
                if (!check){
                    listButtons.get(i).setBackgroundResource(R.drawable.custom_button);
                    check = true;
                }
            }
        }
    }
}