package com.example.medihealth.activities.prescription_schedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.PrescriptionItemAdapter;
import com.example.medihealth.apiservices.PrescriptionService;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.PrescriptionItem;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPrescriptionItemsActivity extends AppCompatActivity implements ItemTouchHelperListener {

    Prescription prescription;
    ImageView btnBack;
    TextView tvToolbarTitle;
    RelativeLayout rootView;
    Button btnAddToList;
    Button btnComplete;
    TextInputLayout etName;
    TextInputLayout etNote;
    List<PrescriptionItem> prescriptionItems = new ArrayList<>();
    RecyclerView rcvListPrescriptionItem;
    PrescriptionItemAdapter prescriptionItemAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_prescription_item);

        loadData();

        rootView = findViewById(R.id.root_view);

        btnBack = findViewById(R.id.btn_back_toolbar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvToolbarTitle = findViewById(R.id.tv_toolbar);
        tvToolbarTitle.setText("Chỉnh sửa đơn thuốc");

        etName = findViewById(R.id.et_name);
        etNote = findViewById(R.id.et_note);

        btnAddToList = findViewById(R.id.btn_add_to_list);
        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPrescriptionItemToList();
            }
        });

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

        btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setText("Lưu");
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void backToViewDetail() {
        finish();
    }

    private void savePrescription() {
        prescription.setPrescriptionItems(prescriptionItems);
        PrescriptionService service = RetrofitClient.createService(PrescriptionService.class);
        service.editPrescription(prescription).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            EditPrescriptionItemsActivity.this,
                            "Cập nhật thành công",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.i("EDIT_PRESCRIPTION_ITEMS", response.body().getMessage());
                    backToViewDetail();
                } else {
                    Toast.makeText(
                            EditPrescriptionItemsActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.i("EDIT_PRESCRIPTION_ITEMS", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(
                        EditPrescriptionItemsActivity.this,
                        "Đã có lỗi xảy ra, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                ).show();
                Log.e("EDIT_PRESCRIPTION_ITEMS", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Bạn có chắc chắn muốn cập nhật thông tin đơn thuốc này không?");
        alertDialog.setTitle("Xác nhận");
        alertDialog.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savePrescription();
            }
        });

        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        prescription = (Prescription) bundle.getSerializable("prescription");
        prescriptionItems.addAll(prescription.getPrescriptionItems());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addPrescriptionItemToList() {
        String name = etName.getEditText().getText().toString().trim();
        String note = etNote.getEditText().getText().toString().trim();
        if (name.isEmpty() || note.isEmpty()) {
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
