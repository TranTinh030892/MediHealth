package com.example.medihealth.activitys.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.chat.ChatRecyclerAdapter;
import com.example.medihealth.models.ChatMessage;
import com.example.medihealth.models.ChatRoom;
import com.example.medihealth.models.Employee;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserChat_Activity extends AppCompatActivity {
    Employee employee;
    String chatroomId , employeeTokenId = "";
    ChatRoom chatRoom;
    TextView textViewName;
    EditText editTextChat;
    ImageButton btnSend;
    ChatRecyclerAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onStart() {
        super.onStart();
        getTokenId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchat);
        employee = AndroidUtil.getEmployeeModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), employee.getUserId());
        initView();
        setInforEmployee();
        getOrCreateChatroomModel();
        setupChatRecyclerView();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextChat.getText().toString().trim();
                if (message.isEmpty())
                    return;
                sendMessage(message);
            }
        });
    }
    private void initView() {
        textViewName = findViewById(R.id.fullName_Chat);
        editTextChat = findViewById(R.id.chat_message_input);
        editTextChat.requestFocus();
        btnSend = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.chat_recycler_view);
    }
    private void setInforEmployee() {
        textViewName.setText(employee.getFullName());
    }
    private void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoom = task.getResult().toObject(ChatRoom.class);
                if (chatRoom == null) {
                    //first time chat
                    chatRoom = new ChatRoom(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(), employee.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatRoom);
                }
            }
        });
    }
    private void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }
    private void sendMessage(String message) {
        chatRoom.setLastMessageTimestamp(Timestamp.now());
        chatRoom.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatRoom.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatRoom);

        ChatMessage chatMessageModel = new ChatMessage(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            editTextChat.setText("");
                        }
                    }
                });
        FirebaseFirestore.getInstance().collection("state").document(employee.getUserId()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean state = task.getResult().getBoolean("isState");
                        if (!state){
                            if (!employeeTokenId.equals("")){
                                Log.e("CHECKTOKEN",employeeTokenId);
                                sendNotification(message,"cbMWBzodQoO-7B8Pc5dEqM:APA91bFDdI26h7v211ZIDgwAe-BSBhyqcWnkQ7Li1vM854w_ilpoPFm_bTLW0cKA3_HqCQdzhU3SlFZTyIXJ8au_jfBxEev0lWi0RuDr4fnETwkfsP7Si3OIKgBhcV6Qi8qz-j8y1qWm");
                            }
                        }
                    }
                    else {
                        Log.e("ERROR","ERROR");
                    }
                });
    }
    private void getTokenId(){
        FirebaseUtil.getTokenId(employee.getUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String tokenStr = "";
                tokenStr = documentSnapshot.getString("tokenId");
                if (!tokenStr.equals("")){
                    employeeTokenId = tokenStr;
                }
                else {
                    Log.e("ERROR", "Không có dữ liệu");
                }
            } else {
                Log.e("ERROR", "Lỗi kết nối Firebase");
            }
        });
    }
    private void sendNotification(String message,String tokenId) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel currentUser = task.getResult().toObject(UserModel.class);
                try {
                    JSONObject jsonObject = new JSONObject();

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("title", currentUser.getFullName());
                    dataObj.put("body", message);

                    dataObj.put("userId", currentUser.getUserId());
                    jsonObject.put("data", dataObj);
                    jsonObject.put("to", tokenId);

                    callApi(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAA2YcskTU:APA91bFbJHA1gMDoHTkHauaDOJZfuzxcYqq6TjBqUoyAp-0I8YHhjIxh3VvtJUS39Od1biRCgomTSO_C5AOMsAHf_ovnSCR6IwO_MglmxIUgWZuL-ADA0MRXqA-leMYLGODLnHx_v7AN")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}