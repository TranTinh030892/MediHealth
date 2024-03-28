package com.example.medihealth.activitys.prescription_schedule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medihealth.R;

public class CustomDialog extends Dialog {

    Button btnOK;
    Button btnCancel;
    EditText etEnterField;
    TextView tvTitle;

    public CustomDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_enter_something);

        tvTitle = findViewById(R.id.tv_title);
        etEnterField = findViewById(R.id.et_enter_field);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOK = findViewById(R.id.btn_ok);

        setCancelable(false);

        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setHint(String hint) {
        etEnterField.setHint(hint);
    }

    public void setContent(String text) {
        etEnterField.setText(text);
    }

    public void setPositiveButton(String text, CustomOnClickListener<String> onClickPositiveButtonListener) {
        btnOK.setText(text);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPositiveButtonListener.onClick(etEnterField.getText().toString().trim());
            }
        });
    }

    public void setNegativeButton(String text, View.OnClickListener onClickNegativeButtonListener) {
        btnCancel.setText(text);
        btnCancel.setOnClickListener(onClickNegativeButtonListener);
    }


}
