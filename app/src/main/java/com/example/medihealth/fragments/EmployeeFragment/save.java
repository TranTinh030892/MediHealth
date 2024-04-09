//package com.example.medihealth.fragments.EmployeeFragment;
//
//import android.app.Dialog;
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Handler;
//import android.os.Looper;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.medihealth.R;
//import com.example.medihealth.adapters.appointment.Employee_AppointmentAdapter;
//import com.example.medihealth.models.Appointment;
//import com.example.medihealth.utils.FirebaseUtil;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.firebase.firestore.Query;
//
//import java.util.Calendar;
//
//public class Employee_Appointment_Fragment extends Fragment implements View.OnClickListener {
//    RelativeLayout btnPending, btnApproved, btnCancelled, btnDate,blockData, blockEmpty;
//    TextView textPending, textApproved, textCancelled, textDate;
//    EditText textSearch;
//    RecyclerView recyclerView;
//    Employee_AppointmentAdapter appoitnmentAdapter;
//    ProgressBar progressBar;
//    int btnSelected = 0;
//    private Handler mHandler = new Handler(Looper.getMainLooper());
//    private static final int DELAY = 1000;
//    public Employee_Appointment_Fragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View itemView =  inflater.inflate(R.layout.fragment_employee_appointment, container, false);
//        initView(itemView);
//        setOnclick();
//        setBackgroudButton(itemView.findViewById(R.id.btn_pending));
//        showData();
//        setupDataWithChangeEventOnEdiSearch();
//        startRepeatedTask();
//        return itemView;
//    }
//    private void initView(View itemView) {
//        btnPending = itemView.findViewById(R.id.btn_pending);
//        btnApproved = itemView.findViewById(R.id.btn_approved);
//        btnCancelled = itemView.findViewById(R.id.btn_cancelled);
//        textPending = itemView.findViewById(R.id.text_pending);
//        textApproved = itemView.findViewById(R.id.text_approved);
//        textCancelled = itemView.findViewById(R.id.text_cancelled);
//        btnDate = itemView.findViewById(R.id.btn_date);
//        textDate = itemView.findViewById(R.id.date);
//        textDate.setText(getCurrentDate());
//        textSearch = itemView.findViewById(R.id.search_name);
//        recyclerView = itemView.findViewById(R.id.list_Appointment);
//        progressBar = itemView.findViewById(R.id.loading);
//        blockData = itemView.findViewById(R.id.block_data);
//        blockEmpty = itemView.findViewById(R.id.block_empty);
//    }
//    private String getCurrentDate (){
//        final Calendar calendar = Calendar.getInstance();
//        int year, month, day;
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        String monthStr = String.valueOf(month),dayStr = String.valueOf(day);
//        if (month+1 < 10) monthStr = "0" + (month + 1);
//        if (day < 10) dayStr = "0" + day;
//
//        String result = dayStr+"/"+monthStr+"/"+year;
//        return result;
//    }
//
//    private void setOnclick() {
//        btnPending.setOnClickListener(this);
//        btnApproved.setOnClickListener(this);
//        btnCancelled.setOnClickListener(this);
//        btnDate.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.btn_pending){
//            btnSelected = 0;
//            setBackgroudButton(v.findViewById(R.id.btn_pending));
//            showData();
//        }
//        if (v.getId() == R.id.btn_approved){
//            btnSelected = 1;
//            setBackgroudButton(v.findViewById(R.id.btn_approved));
//            showData();
//        }
//        if (v.getId() == R.id.btn_cancelled){
//            btnSelected = -1;
//            setBackgroudButton(v.findViewById(R.id.btn_cancelled));
//            showData();
//        }
//        if (v.getId() == R.id.btn_date){
//            showDialogCalendar(Gravity.CENTER);
//        }
//    }
//    private void setBackgroudButton(View view){
//        int colorOfButtonSelected = ContextCompat.getColor(requireContext(), R.color.color_background_menu_appointment);
//        int colorOfButtonNotSelected = ContextCompat.getColor(requireContext(), R.color.white);
//        int colorOfTextSelected = ContextCompat.getColor(requireContext(), R.color.text_Color);
//        int colorOfTextNotSelected = ContextCompat.getColor(requireContext(), R.color.light_gray);
//        if (view.getId() == R.id.btn_pending){
//            btnPending.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonSelected));
//            textPending.setTextColor(colorOfTextSelected);
//            btnApproved.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
//            textApproved.setTextColor(colorOfTextNotSelected);
//            btnCancelled.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
//            textCancelled.setTextColor(colorOfTextNotSelected);
//        }
//        if (view.getId() == R.id.btn_approved) {
//            btnApproved.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonSelected));
//            textApproved.setTextColor(colorOfTextSelected);
//            btnPending.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
//            textPending.setTextColor(colorOfTextNotSelected);
//            btnCancelled.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
//            textCancelled.setTextColor(colorOfTextNotSelected);
//        }
//        if (view.getId() == R.id.btn_cancelled) {
//            btnApproved.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
//            textApproved.setTextColor(colorOfTextNotSelected);
//            btnPending.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonNotSelected));
//            textPending.setTextColor(colorOfTextNotSelected);
//            btnCancelled.setBackgroundTintList(ColorStateList.valueOf(colorOfButtonSelected));
//            textCancelled.setTextColor(colorOfTextSelected);
//        }
//    }
//    private void showDialogCalendar(int center) {
//        final Dialog dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_datepicker);
//        Window window = dialog.getWindow();
//        if (window == null){
//            return;
//        }
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = center;
//        window.setAttributes(windowAttributes);
//        if (Gravity.BOTTOM == center){
//            dialog.setCancelable(false);
//        }
//        else{
//            dialog.setCancelable(true);
//        }
//        DatePicker datePicker;
//        RelativeLayout btnCancel, btnSelect;
//        datePicker = dialog.findViewById(R.id.datePicker);
//        btnCancel = dialog.findViewById(R.id.cancel);
//        btnSelect = dialog.findViewById(R.id.select);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDate(datePicker);
//                dialog.dismiss();
//                showData();
//            }
//        });
//
//        dialog.show();
//    }
//
//    private void getDate(DatePicker datePicker) {
//        int dayOfMonth = datePicker.getDayOfMonth();
//        int monthOfYear = datePicker.getMonth();
//        int year = datePicker.getYear();
//        String dayOfMonthStr = String.valueOf(dayOfMonth),
//                monthOfYearStr = String.valueOf(monthOfYear+1);
//        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonthStr;
//        if (monthOfYear < 10) monthOfYearStr = "0" + monthOfYearStr;
//        String date = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
//        textDate.setText(date);
//    }
//
//    private void showData (){
//        String dateStr = textDate.getText().toString();
//        String searchStr = textSearch.getText().toString().trim();
//        setupRecyclerViewData(dateStr,searchStr);
//    }
//    private void setupRecyclerViewData(String dateStr, String search) {
//        String nameSearch = standardizedFullName(search);
//
//        Query query = FirebaseUtil.getAppointmentCollectionReference()
//                .whereEqualTo("bookDate", dateStr)
//                .whereGreaterThanOrEqualTo("userModel.fullName", nameSearch)
//                .whereLessThan("userModel.fullName", nameSearch + "\uf8ff")
//                .whereEqualTo("stateAppointment", btnSelected);
//
//        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
//                .setQuery(query, Appointment.class).build();
//        setupRecyclerView(options);
//    }
//
//
//    private void setupRecyclerView(FirestoreRecyclerOptions<Appointment> options) {
//        appoitnmentAdapter = new Employee_AppointmentAdapter(options, getContext());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(appoitnmentAdapter);
//        appoitnmentAdapter.startListening();
//    }
//
//    private void setupDataWithChangeEventOnEdiSearch() {
//        textSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                showData();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//    }
//    private String standardizedFullName(String search){
//        if (search.isEmpty()) return "";
//        String regix = "[\\s]+";
//        String[] arr = search.trim().split(regix);
//        if (arr.length == 0) return "";
//
//        String result = arr[0].substring(0,1).toUpperCase() + arr[0].substring(1).toLowerCase();
//        for (int i = 1; i < arr.length; i++) {
//            result += " " + arr[i].substring(0,1).toUpperCase() + arr[i].substring(1).toLowerCase();
//        }
//        return result;
//    }
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (appoitnmentAdapter != null){
//                if (appoitnmentAdapter.getItemCount() == 0){
//                    recyclerView.setVisibility(View.GONE);
//                    blockEmpty.setVisibility(View.VISIBLE);
//                }
//                else {
//                    blockEmpty.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                    recyclerView.setAdapter(appoitnmentAdapter);
//                    appoitnmentAdapter.startListening();
//                }
//            }
//            mHandler.postDelayed(this, DELAY);
//        }
//    };
//
//    private void startRepeatedTask() {
//        mHandler.postDelayed(mRunnable, DELAY);
//    }
//
//    private void stopRepeatedTask() {
//        mHandler.removeCallbacks(mRunnable);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopRepeatedTask();
//    }
//}