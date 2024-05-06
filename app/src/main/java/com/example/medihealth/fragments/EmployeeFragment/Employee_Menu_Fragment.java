package com.example.medihealth.fragments.EmployeeFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.medihealth.R;
import com.example.medihealth.activities.MainActivity;
import com.example.medihealth.models.Token;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class Employee_Menu_Fragment extends Fragment implements View.OnClickListener {
    GoogleSignInClient googleSignInClient;
    Dialog dialog, dialogSwitch;
    SharedPreferences sharedPreferences;
    RelativeLayout btnSetNotice;
    CardView btnLogout;
    ImageView iconMenu;
    TextView titleMenu, fullNameEmployee, birthUser;
    String currentUserId = "";

    public Employee_Menu_Fragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View iteamView = inflater.inflate(R.layout.fragment_menu, container, false);
        currentUserId = FirebaseUtil.currentUserId();
        dialog = new Dialog(getContext());
        sharedPreferences = getContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initView(iteamView);
        setInforEmployee();
        setOnclick();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        return iteamView;
    }

    private void setInforEmployee() {
        String inforFormEmployee = sharedPreferences.getString("inforFormEmployee", "empty");
        if (!inforFormEmployee.equals("empty")) {
            String[] array = inforFormEmployee.split(";");
            if (array.length > 0) {
                fullNameEmployee.setText(array[0]);
                birthUser.setVisibility(View.GONE);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }

    private void initView(View iteamView) {
        fullNameEmployee = iteamView.findViewById(R.id.fullName_user);
        birthUser = iteamView.findViewById(R.id.gender_birth);
        btnSetNotice = iteamView.findViewById(R.id.btn_menu4);
        btnLogout = iteamView.findViewById(R.id.btn_logout);
        iconMenu = iteamView.findViewById(R.id.icon_menu4);
        titleMenu = iteamView.findViewById(R.id.title_menu4);
        iconMenu.setBackgroundResource(R.drawable.notification);
        titleMenu.setText("Cài đặt thông báo");
    }

    private void setOnclick() {
        btnLogout.setOnClickListener(this);
        btnSetNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_logout) {
            showDialogLoadingLogout(Gravity.CENTER);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    sigout();
                }
            }, 2000);
        }
        if (v.getId() == R.id.btn_menu4) {
            showDialogSwich(Gravity.CENTER);
        }
    }

    private void sigout() {
        getCurrentUserTokenId(new TokenFetchCallback() {
            @Override
            public void onTokenFetchComplete() {
                removeAllSharedPreferences();
                FirebaseAuth.getInstance().signOut();
                googleSignInClient.signOut().addOnCompleteListener(task -> {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
            }
        });
    }


    private void getCurrentUserTokenId(TokenFetchCallback callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String tokenId = task.getResult();
                        removeTokenId(FirebaseUtil.currentUserId(), tokenId);
                        callback.onTokenFetchComplete(); // Gọi callback khi quá trình hoàn thành
                    } else {
                        Log.e("EROR", "Khong lay duoc Token");
                    }
                });
    }


    private void removeTokenId(String curentUserId, String tokenId) {
        Query query = FirebaseUtil.getTokenId().whereEqualTo("userId", curentUserId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    Token token = documentSnapshot.toObject(Token.class);
                    if (token != null) {
                        List<String> tokenList = token.getTokenList();
                        for (int i = 0; i < tokenList.size(); i++) {
                            if (tokenList.get(i).equals(tokenId)) {
                                tokenList.remove(i);
                                break;
                            }
                        }
                        updateTonken(documentSnapshot.getId(), tokenList);
                    }
                }
            } else {
                Log.e("ERROR", "Lỗi kết nối");
            }
        });
    }

    private void updateTonken(String documentSnapshot, List<String> tokenList) {
        Token token = new Token(tokenList, currentUserId);
        FirebaseUtil.getTokenByDocument(documentSnapshot).set(token).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("SUCCESSFULL", "Update tokenList thành công");
            } else Log.e("ERROR", "Lỗi kết nối");
        });
    }

    private void removeAllSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile", "empty");
        editor.putString("inforFormUser", "empty");
        editor.apply();
    }

    private void showDialogLoadingLogout(int center) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_loading);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        TextView textTitle;
        textTitle = dialog.findViewById(R.id.title);
        textTitle.setText("Đang đăng xuất...");

        dialog.show();
    }

    private void showDialogSwich(int center) {
        sharedPreferences = getContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        dialogSwitch = new Dialog(getActivity()); // Khởi tạo dialogSwitch
        dialogSwitch.requestWindowFeature(Window.FEATURE_NO_TITLE); // Đảm bảo gọi requestFeature() trước khi thêm nội dung
        dialogSwitch.setContentView(R.layout.custom_swich);
        Window window = dialogSwitch.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        Switch switchNotice;
        switchNotice = dialogSwitch.findViewById(R.id.notificationSwitch);

        // cài đặt trạng thái switch theo isCloseNotice
        String isCloseNotice = sharedPreferences.getString("isCloseNotice", "No");
        if (isCloseNotice.equals("Yes")) {
            switchNotice.setChecked(false);
        } else switchNotice.setChecked(true);

        // cài đặt isCloseNotice theo switch
        switchNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchNotice.isChecked()) {
                    editor.putString("isCloseNotice", "No");
                } else editor.putString("isCloseNotice", "Yes");
                editor.apply();
            }
        });
        dialogSwitch.show();
    }

    public interface TokenFetchCallback {
        void onTokenFetchComplete();
    }
}