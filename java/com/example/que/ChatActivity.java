package com.example.chat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {

    private EditText messageInput;
    private ArrayAdapter<String> adapter;
    private List<String> messages = new ArrayList<>();

    private String hikeId = "hike_001";   // for POC
    private String userId = "user_123";   // from Firebase Auth later

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.messageInput);
        ListView listView = findViewById(R.id.messageList);
        Button sendBtn = findViewById(R.id.sendButton);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        sendBtn.setOnClickListener(v -> sendMessage());

        listenForMessages();
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        Message msg = new Message(userId, text);

        // Add message to Firestore under "chats/{hikeId}/messages"
        db.collection("chats")
                .document(hikeId)
                .collection("messages")
                .add(msg);

        messageInput.setText("");
    }

    private void listenForMessages() {
        db.collection("chats")
                .document(hikeId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null || snapshots == null) return;

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Message msg = dc.getDocument().toObject(Message.class);
                                messages.add(msg.senderId + ": " + msg.text);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}

