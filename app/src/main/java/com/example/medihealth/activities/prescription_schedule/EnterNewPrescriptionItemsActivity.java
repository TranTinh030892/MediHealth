package com.example.medihealth.activities.prescription_schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.PrescriptionItemAdapter;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.PrescriptionItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class EnterNewPrescriptionItemsActivity extends AppCompatActivity implements ItemTouchHelperListener {

    RelativeLayout rootView;
    Prescription prescription;
    TextView tvToolbar;
    TextInputLayout etName, etNote;
    ImageView btnBackToolbar;
    List<PrescriptionItem> prescriptionItems = new ArrayList<>();
    PrescriptionItemAdapter prescriptionItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_prescription_item);

        loadData();

        rootView = findViewById(R.id.root_view);

        // Custom toolbar
        tvToolbar = findViewById(R.id.tv_title);
        tvToolbar.setText("Thêm đơn thuốc");
        btnBackToolbar = findViewById(R.id.btn_back);
        btnBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etName = findViewById(R.id.et_name);
        etNote = findViewById(R.id.et_note);

        // Handle button add new prescription item to list below
        Button btnAddToList;
        btnAddToList = findViewById(R.id.btn_add_to_list);
        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPrescriptionItemToList();
            }
        });

        RecyclerView rcvListPrescriptionItem;
        rcvListPrescriptionItem = findViewById(R.id.rcv_list_prescription_item);
        rcvListPrescriptionItem.setLayoutManager(new LinearLayoutManager(this));
        prescriptionItemAdapter = new PrescriptionItemAdapter(prescriptionItems);
        rcvListPrescriptionItem.setAdapter(prescriptionItemAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(
                0,
                ItemTouchHelper.LEFT,
                this
        );
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvListPrescriptionItem);

        Button btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        EnterNewPrescriptionItemsActivity.this,
                        EnterNewSchedulesActivity.class
                );
                Bundle bundle = new Bundle();
                prescription.setPrescriptionItems(prescriptionItems);
                bundle.putSerializable("prescription", prescription);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        prescription = (Prescription) bundle.getSerializable("prescription");
        if (prescription.getPrescriptionItems() != null) {
            prescriptionItems.addAll(prescription.getPrescriptionItems());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addPrescriptionItemToList() {
        String name = etName.getEditText().getText().toString().trim();
        String note = etNote.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        PrescriptionItem prescriptionItem = new PrescriptionItem();
        prescriptionItem.setName(name);
        prescriptionItem.setNote(note);
        if (prescriptionItems.stream().noneMatch(
                (item) -> item.getName().equalsIgnoreCase(name)
                        && item.getNote().equalsIgnoreCase(note))) {

            prescriptionItems.add(prescriptionItem);
            prescriptionItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof PrescriptionItemAdapter.PrescriptionItemViewHolder) {
            int index = viewHolder.getAdapterPosition();
            PrescriptionItem prescriptionItem = prescriptionItems.get(index);
            removeItem(index);

            Snackbar snackbar = Snackbar.make(
                    rootView,
                    "Đã xóa",
                    Snackbar.LENGTH_SHORT
            );
            snackbar.setAction("Hoàn tác", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoItem(prescriptionItem, index);
                }
            });
            snackbar.show();
        }
    }

    private void undoItem(PrescriptionItem prescriptionItem, int index) {
        prescriptionItems.add(index, prescriptionItem);
        prescriptionItemAdapter.notifyItemInserted(index);
    }

    private void removeItem(int index) {
        prescriptionItems.remove(index);
        prescriptionItemAdapter.notifyItemRemoved(index);
    }
}
