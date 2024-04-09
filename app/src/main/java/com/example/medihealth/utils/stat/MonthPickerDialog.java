package com.example.medihealth.utils.stat;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthPickerDialog {

    private Context context;
    private OnMonthSetListener onMonthSetListener;
    private List<MonthInfo> monthList;
    private AlertDialog dialog;

    public MonthPickerDialog(Context context, OnMonthSetListener onMonthSetListener) {
        this.context = context;
        this.onMonthSetListener = onMonthSetListener;
        this.monthList = generateMonthList();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn tháng");

        ListView listView = new ListView(context);
        ArrayAdapter<MonthInfo> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, monthList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            MonthInfo selectedMonth = monthList.get(position);
            onMonthSetListener.onMonthSet(selectedMonth);
            dialog.dismiss();
        });

        builder.setView(listView);
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 1500);
    }

    private List<MonthInfo> generateMonthList() {
        List<MonthInfo> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, 2030);
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);

        while (calendar.before(endCalendar)) {
            LocalDate dayOfMonth = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
            String monthLabel = "Tháng " + (calendar.get(Calendar.MONTH) + 1 > 9 ? calendar.get(Calendar.MONTH) + 1:"0" + (calendar.get(Calendar.MONTH) + 1)) + "/" + calendar.get(Calendar.YEAR);
            MonthInfo monthInfo = new MonthInfo(monthLabel, dayOfMonth);
            months.add(monthInfo);
            calendar.add(Calendar.MONTH, 1);
        }

        return months;
    }



    public interface OnMonthSetListener {
        void onMonthSet(MonthInfo monthInfo);
    }
}


