package com.example.medihealth.activitys.prescription_schedule;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.DrugUserAdapter;
import com.example.medihealth.apiservices.DrugUserService;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.services.RemindService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrugUserManagement extends AppCompatActivity {

    final static String ADD_DRUG_USER = "Thêm";
    final static String EDIT_DRUG_USER = "Lưu";


    List<DrugUser> drugUsers = new ArrayList<>();
    DrugUserAdapter drugUserAdapter;
    FloatingActionButton fabAddDrugUser;
    TextView tvToolbar;
    SwipeRefreshLayout srlListDrugUser;
    RecyclerView rcvListDrugUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_user_management);
        tvToolbar = findViewById(R.id.tv_toolbar);
        tvToolbar.setText("Người dùng thuốc");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {android.Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS};
                requestPermissions(permissions, 1);
            }
        }

        fabAddDrugUser = findViewById(R.id.fab_add_drug_user);

        fabAddDrugUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEnterDrugUser(new DrugUser(), ADD_DRUG_USER);
            }
        });

        srlListDrugUser = findViewById(R.id.srl_list_drug_user);
        srlListDrugUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                srlListDrugUser.setRefreshing(false);
            }
        });

        drugUserAdapter = new DrugUserAdapter(drugUsers, new CustomOnClickListener<DrugUser>() {
            @Override
            public void onClick(DrugUser data) {
                Intent intent = new Intent(DrugUserManagement.this, PrescriptionManagement.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("drugUser", data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        drugUserAdapter.setOnClickDeleteListener(new CustomOnClickListener<DrugUser>() {
            @Override
            public void onClick(DrugUser data) {
                deleteDrugUser(data);
            }
        });

        drugUserAdapter.setOnClickEditListener(new CustomOnClickListener<DrugUser>() {
            @Override
            public void onClick(DrugUser data) {
                openDialogEnterDrugUser(data, EDIT_DRUG_USER);
            }
        });

        rcvListDrugUser = findViewById(R.id.rcv_list_drug_user);
        rcvListDrugUser.setLayoutManager(new LinearLayoutManager(this));
        rcvListDrugUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fabAddDrugUser.hide();
                } else {
                    fabAddDrugUser.show();
                }
            }
        });
        drugUserAdapter.setData(drugUsers);
        rcvListDrugUser.setAdapter(drugUserAdapter);
        getData();
    }

    public void requestAutoStartPermission() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
        startActivity(intent);
    }

    private void openDialogEnterDrugUser(DrugUser drugUser, String action) {
        CustomDialog dialog = new CustomDialog(this);

        dialog.setHint("Tên người dùng");

        if (action.equalsIgnoreCase(ADD_DRUG_USER)) {
            dialog.setTitle("Thêm người dùng thuốc");
        } else if (action.equalsIgnoreCase(EDIT_DRUG_USER)) {
            dialog.setTitle("Chỉnh sửa thông tin người dùng thuốc");
            dialog.setContent(drugUser.getName());
        }

        dialog.setNegativeButton("Hủy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton(action, new CustomOnClickListener<String>() {
            @Override
            public void onClick(String data) {
                drugUser.setName(data);
                if (action.equalsIgnoreCase(ADD_DRUG_USER)) {
                    addDrugUser(drugUser);
                } else {
                    editDrugUser(drugUser);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getData() {
        DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
        drugUserService.getAllByUser("12345").enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    ResponseObject responseObject = response.body();
                    Object data = responseObject.getData();
                    drugUsers = new Gson().fromJson(new Gson().toJson(data),
                            new TypeToken<List<DrugUser>>() {
                            }.getType());
                    drugUserAdapter.setData(drugUsers);
                }
                Log.i("GET_LIST_DRUG_USER", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("GET_LIST_DRUG_USER", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void addDrugUser(DrugUser newDrugUser) {
        newDrugUser.setUserId("12345");
        DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
        drugUserService.addDrugUser(newDrugUser).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            DrugUserManagement.this,
                            "Thêm người dùng mới thành công",
                            Toast.LENGTH_SHORT
                    ).show();
                    getData();
                } else {
                    Toast.makeText(
                            DrugUserManagement.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                Log.i("ADD_DRUG_USER", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("ADD_DRUG_USER", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void editDrugUser(DrugUser drugUser) {
        DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
        drugUserService.editDrugUser(drugUser).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            DrugUserManagement.this,
                            "Cập nhật thông tin thành công",
                            Toast.LENGTH_SHORT
                    ).show();
                    getData();
                } else {
                    Toast.makeText(
                            DrugUserManagement.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                Log.i("EDIT_DRUG_USER", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("EDIT_DRUG_USER", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void deleteDrugUser(DrugUser drugUser) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xác nhận");
        alertDialog.setMessage("Bạn có chắc chắn muốn xóa người dùng này không?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
                drugUserService.deleteDrugUser(drugUser.getId()).enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    DrugUserManagement.this,
                                    "Xóa người dùng thành công",
                                    Toast.LENGTH_SHORT
                            ).show();
                            getData();
                        } else {
                            Toast.makeText(
                                    DrugUserManagement.this,
                                    response.body().getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        Log.i("DELETE_DRUG_USER", response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Log.e("DELETE_DRUG_USER", Objects.requireNonNull(t.getMessage()));
                    }
                });
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

}
