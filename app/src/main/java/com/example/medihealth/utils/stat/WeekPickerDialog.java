package com.example.medihealth.utils.stat;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekPickerDialog {

    private Context context;
    private OnWeekSetListener onWeekSetListener;
    private List<WeekInfo> weekList;
    private AlertDialog dialog;

    public WeekPickerDialog(Context context, OnWeekSetListener onWeekSetListener) {
        this.context = context;
        this.onWeekSetListener = onWeekSetListener;
        this.weekList = generateWeekList();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn tuần");

        ListView listView = new ListView(context);
        ArrayAdapter<WeekInfo> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, weekList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            WeekInfo selectedWeek = weekList.get(position);
            onWeekSetListener.onWeekSet(selectedWeek);
            dialog.dismiss();
        });

        builder.setView(listView);
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 1500);
    }

    private List<WeekInfo> generateWeekList() {
        List<WeekInfo> weeks = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 3);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, 2030);
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        endCalendar.set(Calendar.DAY_OF_MONTH, 31);

        while (calendar.before(endCalendar)) {
            String startOfWeek = sdf.format(calendar.getTime());
            LocalDate startDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_YEAR, 6);
            String endOfWeek = sdf.format(calendar.getTime());
            LocalDate endDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            String weekLabel = "Từ " + startOfWeek + " đến " + endOfWeek;
            weeks.add(new WeekInfo(weekLabel, startDate, endDate));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return weeks;
    }

    public interface OnWeekSetListener {
        void onWeekSet(WeekInfo weekInfo);
    }
}





