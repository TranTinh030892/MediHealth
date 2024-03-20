package com.example.medihealth.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUtil {
    public interface StateCallback {
        void onStateReceived(boolean isOnline);
    }

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
    public static DocumentReference currentEmployeeDetails(){
        return FirebaseFirestore.getInstance().collection("employee").document(currentUserId());
    }
    public static DocumentReference getDoctorDetailsById(String doctorId){
        return FirebaseFirestore.getInstance().collection("doctor").document(doctorId);
    }
//    public static DocumentReference statusUser(){
//        return FirebaseFirestore.getInstance().collection("status").document(currentUserId());
//    }
//    public static void setStatus(boolean b){
//        Map<String, Boolean> statusUser = new HashMap<>();
//        statusUser.put("isStatus", b);
//        FirebaseUtil.statusUser().set(statusUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
//    }
    public static DocumentReference stateUserOlline(){
        return FirebaseFirestore.getInstance().collection("state").document(currentUserId());
    }
    public static void setState(boolean b){
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Map<String, Boolean> stateUser = new HashMap<>();
            stateUser.put("isState", b);
            FirebaseUtil.stateUserOlline().set(stateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }
    public static DocumentReference roleUser(){
        return FirebaseFirestore.getInstance().collection("role").document(currentUserId());
    }
    public static DocumentReference setTokenId(){
        return FirebaseFirestore.getInstance().collection("token").document(currentUserId());
    }
    public static DocumentReference getTokenId(String documentId){
        return FirebaseFirestore.getInstance().collection("token").document(documentId);
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static CollectionReference allEmployeeCollectionReference(){
        return FirebaseFirestore.getInstance().collection("employee");
    }
    public static CollectionReference allDoctorCollectionReference(){
        return FirebaseFirestore.getInstance().collection("doctor");
    }
    // thêm một đối tượng appointment vào document của collection appointment với id được lấy tự động
    public static CollectionReference getAppointmentCollectionReference(){
        return FirebaseFirestore.getInstance().collection("appointment");
    }
    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1,String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else{
            return userId2+"_"+userId1;
        }
    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if(userIds.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userIds.get(1));
        }else{
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

//    public static StorageReference  getCurrentProfilePicStorageRef(){
//        return FirebaseStorage.getInstance().getReference().child("profile_pic")
//                .child(FirebaseUtil.currentUserId());
//    }

//    public static StorageReference  getOtherProfilePicStorageRef(String otherUserId){
//        return FirebaseStorage.getInstance().getReference().child("profile_pic")
//                .child(otherUserId);
//    }


}










