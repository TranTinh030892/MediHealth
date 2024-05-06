package com.example.medihealth.activities.prescription_schedule;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.medihealth.R;

public class QRCodeDialog extends AlertDialog {

    private Bitmap qrCodeBitmap;

    public QRCodeDialog(Context context, Bitmap qrCodeBitmap) {
        super(context);
        this.qrCodeBitmap = qrCodeBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_corner_16);
        setContentView(R.layout.dialog_qr_code);
        ImageView qrView = findViewById(R.id.qr_view);
        qrView.setImageBitmap(qrCodeBitmap);
    }
}
