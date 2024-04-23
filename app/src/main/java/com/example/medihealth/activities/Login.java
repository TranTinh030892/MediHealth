package com.example.medihealth.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.activities.chat.Employee_MainActivity;
import com.example.medihealth.activities.profile.Profile;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Employee;
import com.example.medihealth.models.Token;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Dialog dialog;
    SharedPreferences sharedPreferences;
    EditText emailUser, passWord;
    TextView register,loginGoogle;
    Button btnLogin,btnLoginDisabled;
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 20;
    Boolean checkInputEmail = false,checkInputPass = false;
    String tokenId;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        tokenId = task.getResult();
                        Log.e("TOKEN",tokenId);
                    } else {
                        Log.e("EROR","Khong lay duoc Token");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new Dialog(this);
        initView();
        setButtonLogin();
        initOnclick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showDialogLoadingLogin(Gravity.CENTER);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fireBaseAuth(account.getIdToken());
                    }
                }, 2000);
            }catch (Exception e){
                Log.e("ERROR","ERROR",e);
            }
        }
        else{
            CustomToast.showToast(getApplicationContext(),"Lỗi kết nối mạng",Toast.LENGTH_LONG);
        }
    }
    private void initView() {
        emailUser = findViewById(R.id.email);
        emailUser.requestFocus();
        passWord = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login_enable);
        btnLoginDisabled = findViewById(R.id.btn_login_disabled);
        loginGoogle = findViewById(R.id.login_google);
        register = findViewById(R.id.register);
    }
    private void setButtonLogin() {
        emailUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    checkInputEmail = true;
                }
                else checkInputEmail = false;
                if (checkInputEmail && checkInputPass){
                    btnLoginDisabled.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                }
                else {
                    btnLoginDisabled.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    checkInputPass = true;
                }
                else checkInputPass = false;
                if (checkInputEmail && checkInputPass){
                    btnLoginDisabled.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                }
                else {
                    btnLoginDisabled.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void initOnclick() {
        btnLogin.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
    }
    private void loginGoogle() {
        GoogleSignInOptions gos = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,gos);
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }
    private void checkProfile(String userId) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("ERROR", "Listen failed.", e);
                        }
                        if (documentSnapshot.exists() && !documentSnapshot.getData().isEmpty()) {
                            checkRole(userId);
                        } else {
                            Intent intent = new Intent(Login.this, Profile.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    private void checkRole(String currentUserid) {
        addTokenId();
        FirebaseFirestore.getInstance().collection("role")
                .document(currentUserid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("ERROR", "Listen failed.", e);
                        }
                        if (documentSnapshot != null && documentSnapshot.contains("isRole")) {
                            Number isRoleNumber = documentSnapshot.getLong("isRole");
                            if(isRoleNumber != null){
                                int isRole = isRoleNumber.intValue();
                                Intent intent = null;
                                if (isRole == 3) {
                                    intent = new Intent(Login.this, MainActivity.class);
                                } else if (isRole == 2){
                                    intent = new Intent(Login.this, Employee_MainActivity.class);
                                    intent.putExtra("requestCodeEmployee", 1103);
                                    setSharedPreferencesDataEmployee();
                                }
                                if (intent != null) {
                                    intent.putExtra("requestCodeLoadingFormUser", 1102);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Log.e("ERROR", "failed");
                        }
                    }
                });
    }

    private void addTokenId() {
        String currentUserId = FirebaseUtil.currentUserId();
        if (currentUserId != null){
            Query query = FirebaseUtil.getTokenId().whereEqualTo("userId",currentUserId);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        Token token = documentSnapshot.toObject(Token.class);
                        if (token != null) {
                            List<String> tokenList = token.getTokenList();
                            if (!tokenList.contains(tokenId)) {
                                tokenList.add(tokenId);
                                token.setTokenList(tokenList);
                                updateToken(documentSnapshot.getId(), token);
                            }
                        }
                    } else {
                        List<String> tokenList = new ArrayList<>();
                        tokenList.add(tokenId);
                        Token token = new Token(tokenList,currentUserId);
                        addNewToken(token);
                    }
                }
                else {
                    Log.e("ERROR","Lỗi kết nối");
                }
            });
        }
    }

    private void addNewToken(Token token) {
        FirebaseUtil.getTokenId().add(token).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d("SUCCESSFULL","Thêm mới Token thành công");
            }
            else Log.e("ERROR","Lỗi kết nối");
        });
    }

    private void updateToken(String documentId, Token token) {
        FirebaseUtil.getTokenByDocument(documentId).set(token).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d("SUCCESSFULL","Thêm tokenId thành công");
            }
            else Log.e("ERROR","Lỗi kết nối");
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login_enable){
            String email = emailUser.getText().toString();
            String pass = passWord.getText().toString();
            if (email.equals("") || pass.equals("")){
                Toast.makeText(this, "Điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            onClickLogin(email,pass);
        }
        else if (v.getId() == R.id.login_google){
            loginGoogle();
        }
    }

    private void showDialogLoadingLogin(int center) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_loading);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        TextView textTitle;
        textTitle = dialog.findViewById(R.id.title);
        textTitle.setText("Đang đăng nhập...");

        dialog.show();
    }

    private void onClickLogin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String currentUserid = mAuth.getCurrentUser().getUid();
                            showDialogLoadingLogin(Gravity.CENTER);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getAllUserIdAndCheck(currentUserid);
                                }
                            }, 2000);
                        }
                        else {
                            CustomToast.showToast(getApplicationContext(),"Lỗi kết nối mạng",Toast.LENGTH_LONG);
                        }
                    }
                });
    }
    private void fireBaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            setSharedPreferences(user.getDisplayName(),user.getPhotoUrl());
                            // them role
                            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                                Map<String, Integer> role = new HashMap<>();
                                role.put("isRole", 3);
                                FirebaseUtil.roleUser().set(role).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("LOGIN_GOOGLE_RESULT","SUCESSFULL");
                                    }
                                });
                            }
                            String currentUserid = mAuth.getCurrentUser().getUid();
                            getAllUserIdAndCheck(currentUserid);
                        }
                        else {
                            CustomToast.showToast(getApplicationContext(),"Lỗi kết nối mạng",Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    private void setSharedPreferences(String displayName, Uri photoUrl) {
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile",displayName+";"+photoUrl);
        editor.apply();

    }
    private void getAllUserIdAndCheck(String currentUserId){
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<String> listAllUserId = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    listAllUserId.add(document.getId());
                }
                checkUserOrEmployee(currentUserId, listAllUserId);
            } else {
                Log.e("ERROR","ERROR");
            }
        });
    }
    private void checkUserOrEmployee(String currentUserId,List<String> listAllUserId){
        if (listAllUserId.contains(currentUserId)){
            checkProfile(currentUserId);
        }
        else checkRole(currentUserId);
    }
    private void setSharedPreferencesDataEmployee() {
        FirebaseUtil.currentEmployeeDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Employee currentEmployee = task.getResult().toObject(Employee.class);
                if (currentEmployee != null) {
                    sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("inforFormEmployee",currentEmployee.getFullName());
                    editor.apply();
                }
            } else {
                Log.e("ERROR", "task is not successful or result is null");
            }
        });
    }
}