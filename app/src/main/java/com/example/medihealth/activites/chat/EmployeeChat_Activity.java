package com.example.medihealth.activites.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.chat.ChatRecyclerAdapter;
import com.example.medihealth.models.ChatMessage;
import com.example.medihealth.models.ChatRoom;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class EmployeeChat_Activity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String chatroomId;
    ChatRoom chatRoom;
    ImageView image_user;
    TextView textViewName;
    EditText editTextChat;
    ImageButton btnSend,btnBack;
    UserModel userModel;
    ChatRecyclerAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchat);
        initReference();
        initOnclick();
        userModel = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),userModel.getUserId());
        setInforUser();
        getOrCreateChatroomModel();
        setupChatRecyclerView();
    }

    private void initOnclick() {
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query,ChatMessage.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
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

    private void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoom = task.getResult().toObject(ChatRoom.class);
                if(chatRoom==null){
                    chatRoom = new ChatRoom(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),userModel.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatRoom);
                }
            }
        });
    }

    private void setInforUser() {
        textViewName.setText(String.format( userModel.getFullName()));
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        String profileString = sharedPreferences.getString("profile", "empty");
        if (!profileString.equals("empty")){
            String[] array = profileString.split(";");
            if (array.length > 0) {
                Picasso.get().load(array[1]).into(image_user);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
        else {
            image_user.setImageResource(R.drawable.profile);
        }
    }
    private void initReference() {
        image_user = findViewById(R.id.image_chat);
        textViewName = findViewById(R.id.fullName_Chat);
        editTextChat = findViewById(R.id.chat_message_input);
        editTextChat.requestFocus();
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.chat_recycler_view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.message_send_btn){
            String message = editTextChat.getText().toString().trim();
            if(message.isEmpty())
                return;
            sendMessage(message);
        }
        if (v.getId() == R.id.btnBack){
            Intent intent = new Intent(EmployeeChat_Activity.this, Employee_MainActivity.class);
            startActivity(intent);
        }
    }

    private void sendMessage(String message) {
        chatRoom.setLastMessageTimestamp(Timestamp.now());
        chatRoom.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatRoom.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatRoom);

        ChatMessage chatMessageModel = new ChatMessage(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            editTextChat.setText("");
                        }
                    }
                });
    }
}