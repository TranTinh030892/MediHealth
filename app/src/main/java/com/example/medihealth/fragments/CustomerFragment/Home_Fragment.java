package com.example.medihealth.fragments.CustomerFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.medihealth.R;
import com.example.medihealth.activitys.Login;
import com.example.medihealth.activitys.Search.SearchDrugActivity;
import com.example.medihealth.activitys.appointment.Infor_Appoitment_Activity;
import com.example.medihealth.activitys.chat.ListEmployee;
import com.example.medihealth.activitys.profile.EditProfile;
import com.example.medihealth.adapters.main.SlidePagerAdapter;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home_Fragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    CardView formLogin,formUser;
    RelativeLayout menuBook, menuReminder, menuPrescription, menuService, menuPayment,
    menuSearch, menuProfile, loadingForm, menuChat;
    private SlidePagerAdapter slidePagerAdapter;
    private Timer timer;
    ViewPager2 formSlide;
    private Button btnCalendar,btnLogin;
    private TextView textTemp, textName, textGender_Birth, textHeight, textWeight, textBMI;
    private ImageButton circleOne, circleTwo, circleThree, circleFour, circleFive, circleSix;
    ImageView imageAccount;
    private List<ImageButton>listCircles = new ArrayList<>();
    Dialog dialog ;
    public Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initView(itemView);
        setViewFormUser();
        callApiWeather();
        setOnlclick();
        createCalendarAnimation();
        createSlide();
        return itemView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        callApiWeather();
    }
    private void setOnlclick() {
        btnLogin.setOnClickListener(this);
        menuBook.setOnClickListener(this);
        menuReminder.setOnClickListener(this);
        menuPrescription.setOnClickListener(this);
        menuService.setOnClickListener(this);
        menuPayment.setOnClickListener(this);
        menuSearch.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuChat.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.btn_login && mAuth.getCurrentUser() == null){
            showDialogLogin(Gravity.CENTER);
            return;
        }
        if (v.getId() == R.id.btn_login){
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.block_book_outside){
            refreshSharedPreferences();
            Intent intent = new Intent(getActivity(), Infor_Appoitment_Activity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.form_inside_one_above){
            // menu nhắc lịch
        }
        if (v.getId() == R.id.form_inside_two_above){
            // menu quản lý đơn thuốc
        }
        if (v.getId() == R.id.form_inside_three_below){
            Intent intent = new Intent(getActivity(), EditProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_chat){
            Intent intent = new Intent(getActivity(), ListEmployee.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.form_inside_two_below){
            Intent intent = new Intent(getActivity(), SearchDrugActivity.class);
            startActivity(intent);
        }
    }

    private void showDialogLogin(int center) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notice_login_logout);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == center){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }
        RelativeLayout btnCancel, btnLogin;
        btnCancel = dialog.findViewById(R.id.cancel);
        btnLogin = dialog.findViewById(R.id.agree);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initView(View itemView) {
        loadingForm = itemView.findViewById(R.id.block_loading_form);
        formUser = itemView.findViewById(R.id.infor_user);
        formLogin = itemView.findViewById(R.id.not_logged);
        btnLogin = itemView.findViewById(R.id.btn_login);
        // profile
        imageAccount = itemView.findViewById(R.id.image_account_google);
        setImageAccount();
        textTemp = itemView.findViewById(R.id.temp);
        textName = itemView.findViewById(R.id.fullName_user);
        textGender_Birth = itemView.findViewById(R.id.gender_birth);
        textHeight = itemView.findViewById(R.id.number_height);
        textWeight = itemView.findViewById(R.id.number_weight);
        textBMI = itemView.findViewById(R.id.number_BMI);
        btnCalendar = itemView.findViewById(R.id.btn_calendar);
        // menu
        menuBook = itemView.findViewById(R.id.block_book_outside);
        menuReminder = itemView.findViewById(R.id.form_inside_one_above);
        menuPrescription = itemView.findViewById(R.id.form_inside_two_above);
        menuService = itemView.findViewById(R.id.form_inside_three_above);
        menuPayment = itemView.findViewById(R.id.form_inside_one_below);
        menuSearch = itemView.findViewById(R.id.form_inside_two_below);
        menuProfile = itemView.findViewById(R.id.form_inside_three_below);
        menuChat = itemView.findViewById(R.id.btn_chat);
        // slide
        formSlide = itemView.findViewById(R.id.slide);
        circleOne = itemView.findViewById(R.id.circle_one);listCircles.add(circleOne);
        circleTwo = itemView.findViewById(R.id.circle_two);listCircles.add(circleTwo);
        circleThree = itemView.findViewById(R.id.circle_three);listCircles.add(circleThree);
        circleFour = itemView.findViewById(R.id.circle_four);listCircles.add(circleFour);
        circleFive = itemView.findViewById(R.id.circle_five);listCircles.add(circleFive);
        circleSix = itemView.findViewById(R.id.circle_six);listCircles.add(circleSix);
    }
    private void setViewFormUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            int code = intent.getIntExtra("requestCodeLoadingFormUser", 1);
            if (code == 1102) {
                intent.removeExtra("requestCodeLoadingFormUser");
                createProgressBarLoadingFormUser(currentUser);
            }
            else {
                if (currentUser != null){
                    setFormUser(true);
                }
                else {
                    setFormUser(false);
                }
            }
        }
    }
    private void createProgressBarLoadingFormUser(FirebaseUser currentUser){
        loadingForm.setVisibility(View.VISIBLE);
        formUser.setVisibility(View.GONE);
        formLogin.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingForm.setVisibility(View.GONE);
                if (currentUser != null){
                    setFormUser(true);
                }
                else {
                    setFormUser(false);
                }
            }
        },2000);
    }
    private void setImageAccount() {
        String profileString = sharedPreferences.getString("profile", "empty");
        if (!profileString.equals("empty")){
            String[] array = profileString.split(";");
            if (array.length > 0) {
                Picasso.get().load(array[1]).into(imageAccount);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }

    private void callApiWeather() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "https://api.openweathermap.org/data/2.5/weather?q=Hanoi&appid=2c7dd62facdd0cb9881ca1fc1d17e974";

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);

                        double tempMin = jsonObject.getJSONObject("main").getDouble("temp_min");
                        double tempMax = jsonObject.getJSONObject("main").getDouble("temp_max");

                        double tempMinCelsius = tempMin - 273.15;
                        double tempMaxCelsius = tempMax - 273.15;

                        double tempAverage = (double)(tempMinCelsius + tempMaxCelsius)/2.0;
                        int tempAverage_round = (int) Math.round(tempAverage);
                        String tempAStr = String.valueOf(tempAverage_round)+"°C";
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textTemp.setText(tempAStr);
                                }
                            });
                        }
                    } else {
                        Log.e("Weather", "Lỗi: " + response.code() + " - " + response.message());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void createCalendarAnimation() {
        final TranslateAnimation moveUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.12f);
        moveUp.setDuration(500);

        final TranslateAnimation moveDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.12f,
                Animation.RELATIVE_TO_SELF, 0);
        moveDown.setDuration(500);

        moveUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnCalendar.startAnimation(moveDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        moveDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnCalendar.startAnimation(moveUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btnCalendar.startAnimation(moveUp);
        btnCalendar.startAnimation(moveUp);
    }
    private void createSlide() {
        slidePagerAdapter = new SlidePagerAdapter(getChildFragmentManager(), getLifecycle());
        formSlide.setAdapter(slidePagerAdapter);

        formSlide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setBackgroundCircle(position);
            }
        });

        final boolean[] isForward = {true};
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int nextSlide;
                        if (isForward[0]) {
                            nextSlide = formSlide.getCurrentItem() + 1;
                            if (nextSlide >= slidePagerAdapter.getItemCount()) {
                                isForward[0] = false;
                                nextSlide = formSlide.getCurrentItem() - 1;
                            }
                        } else {
                            nextSlide = formSlide.getCurrentItem() - 1;
                            if (nextSlide < 0) {
                                isForward[0] = true;
                                nextSlide = formSlide.getCurrentItem() + 1;
                            }
                        }
                        setBackgroundCircle(nextSlide);
                        // Di chuyển tới slide tiếp theo
                        formSlide.setCurrentItem(nextSlide, true);
                    }
                });
            }
        }, 4000, 4000);
    }
    private void setBackgroundCircle(int position){
        if (isAdded()) {
            int color = ContextCompat.getColor(requireContext(), R.color.colorCircleSelected);
            int colorDefault = ContextCompat.getColor(requireContext(), R.color.mainColor);
            for (int i = 0; i < listCircles.size(); i++) {
                if (i == position) {
                    listCircles.get(i).setBackgroundTintList(ColorStateList.valueOf(color));
                } else {
                    // Thiết lập màu mặc định cho các circle không được chọn
                    listCircles.get(i).setBackgroundTintList(ColorStateList.valueOf(colorDefault));
                }
            }
        }
    }

    private void setFormUser(boolean b){
        if (b){
            setDataUser();
            formUser.setVisibility(View.VISIBLE);
            formLogin.setVisibility(View.GONE);
        }
        else {
            formUser.setVisibility(View.GONE);
            formLogin.setVisibility(View.VISIBLE);
        }
    }

    private void setDataUser() {
        FirebaseUtil.currentUserDetails().get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                textName.setText(userModel.getFullName());
                textGender_Birth.setText(userModel.getGender()+" - "+userModel.getBirth());
                textHeight.setText(String.valueOf(userModel.getHeight()));
                textWeight.setText(String.valueOf(userModel.getWeight())); ;
                textBMI.setText(AndroidUtil.getBMI(userModel.getHeight(),userModel.getWeight()));
            } else {
                Log.e("ERROR", "User not found");
            }
        });
    }
    private void refreshSharedPreferences() {
        setIndexSelectedSharedPreferences("indexDoctorSelected",0);
        setIndexSelectedSharedPreferences("indexTimeSelected",-1);
    }
    private void setIndexSelectedSharedPreferences(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}