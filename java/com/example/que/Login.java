package com.example.que;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private Button enter_btn;
    private Button signup_btn;
    private EditText username;
    private EditText password;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);


        enter_btn = findViewById(R.id.enter_btn);
        signup_btn = findViewById(R.id.signup_btn);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        db = FirebaseFirestore.getInstance();

        enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username_str = username.getText().toString();
                String password_str = password.getText().toString();

                if (username_str.isEmpty() || password_str.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection("Users")
                        .whereEqualTo("username", username_str)
                        .whereEqualTo("password", password_str)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                                //Intent intent = new Intent(Login.this, MainActivity.class);
                                //startActivity(intent);
                                //finish();
                            } else {
                                Toast.makeText(Login.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(Login.this, "Login failed, try again", Toast.LENGTH_SHORT).show()
                        );
            }
        });
    }


}
