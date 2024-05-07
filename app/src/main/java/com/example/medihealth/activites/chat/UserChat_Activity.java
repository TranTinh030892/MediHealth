package com.example.medihealth.activites.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.chat.ChatRecyclerAdapter;
import com.example.medihealth.models.ChatMessage;
import com.example.medihealth.models.ChatRoom;
import com.example.medihealth.models.Employee;
import com.example.medihealth.models.Token;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class UserChat_Activity extends AppCompatActivity implements View.OnClickListener {
    Employee employee;
    String chatroomId ;
    ChatRoom chatRoom;
    TextView textViewName,textState;
    EditText editTextChat;
    ImageButton btnSend, btnBack;
    ChatRecyclerAdapter adapter;
    RecyclerView recyclerView;
    RelativeLayout iconSate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchat);
        employee = AndroidUtil.getEmployeeModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), employee.getUserId());
        initView();
        setOnclick();
        setInforEmployee();
        getOrCreateChatroomModel();
        setupChatRecyclerView();
    }
    private void initView() {
        textViewName = findViewById(R.id.fullName_Chat);
        editTextChat = findViewById(R.id.chat_message_input);
        editTextChat.requestFocus();
        btnSend = findViewById(R.id.message_send_btn);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.chat_recycler_view);
        iconSate = findViewById(R.id.icon);
        textState = findViewById(R.id.state);
    }
    private void setOnclick() {
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack){
            finish();
        }
        if (v.getId() == R.id.message_send_btn){
            String message = editTextChat.getText().toString().trim();
            if (message.isEmpty())
                return;
            editTextChat.setText("");
            sendMessage(message);
        }
    }
    private void setInforEmployee() {
        if (employee != null){
            textViewName.setText(employee.getFullName());
            FirebaseFirestore.getInstance().collection("state").document(employee.getUserId())
                    .addSnapshotListener((documentSnapshot, error) -> {
                        if (error != null) {
                            return;
                        }
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            boolean state = documentSnapshot.getBoolean("isState");
                            if (state) {
                                iconSate.setBackgroundResource(R.drawable.custom_olline);
                                textState.setText("Online");
                            } else {
                                iconSate.setBackgroundResource(R.drawable.custom_offline);
                                textState.setText("Offline");
                            }
                        } else {
                            Log.e("ERROR","ERROR");
                        }
                    });
        }
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
        if (chatRoom == null){
            getOrCreateChatroomModel();
        }
        else {
            chatRoom.setLastMessageTimestamp(Timestamp.now());
            chatRoom.setLastMessageSenderId(FirebaseUtil.currentUserId());
            chatRoom.setLastMessage(message);
            FirebaseUtil.getChatroomReference(chatroomId).set(chatRoom);
        }
        ChatMessage chatMessageModel = new ChatMessage(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            checkStateEmployee(message);
                        }
                    }
                });
        // repFirtMessage
        feedBackFirtMessage();
    }

    private void checkStateEmployee(String message) {
        FirebaseFirestore.getInstance().collection("state").document(employee.getUserId()).get()
                .addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        boolean state = task1.getResult().getBoolean("isState");
                        if (!state){
                            sendMessagetoAllTokenId(message);
                        }
                    }
                    else {
                        Log.e("ERROR","ERROR");
                    }
                });
    }

    private void feedBackFirtMessage() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    int documentCount = querySnapshot.size();
                    if (documentCount == 1) {
                        chatRoom.setLastMessageTimestamp(Timestamp.now());
                        chatRoom.setLastMessageSenderId(employee.getUserId());
                        String repMessage = "Xin chào, Cảm ơn bạn đã liên hệ với chúng tôi! Chúng tôi đã nhận được tin nhắn của bạn và sẽ sớm phản hồi lại bạn.";
                        chatRoom.setLastMessage(repMessage);
                        FirebaseUtil.getChatroomReference(chatroomId).set(chatRoom);

                        ChatMessage chatMessageModel1 = new ChatMessage(repMessage, employee.getUserId(), Timestamp.now());
                        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel1)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                        }
                                    }
                                });
                    }
                } else {
                    System.err.println("Không thể lấy QuerySnapshot từ task.");
                }
            } else {
                System.err.println("Lỗi trong quá trình truy vấn: " + task.getException());
            }
        });
    }

    private void sendMessagetoAllTokenId(String message) {
        Query query = FirebaseUtil.getTokenId().whereEqualTo("userId",employee.getUserId());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    Token token = documentSnapshot.toObject(Token.class);
                    if (token != null) {
                        List<String> tokenList = token.getTokenList();
                        for (String tokenString : tokenList){
                            FirebaseUtil.sendMessageNotificationtoEmployeeTokenId("employee",message,tokenString);
                        }
                    }
                }
            }
            else {
                Log.e("ERROR","Lỗi kết nối");
            }
        });
    }
}