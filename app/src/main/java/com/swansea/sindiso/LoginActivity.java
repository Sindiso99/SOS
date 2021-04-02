package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private boolean loginComplete = false;
    EditText userEditText;
    EditText passwordEditText;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);

        userEditText = (EditText) findViewById(R.id.userNameTextBox);
        passwordEditText = (EditText) findViewById(R.id.PasswordTextBox);
        TextView loginOutput = (TextView) findViewById(R.id.loginOutcome);

        Button loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userName = userEditText.getText().toString();
                    String passWord = passwordEditText.getText().toString();
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);
                    user = dataBaseHandler.getUserDetails(userName);
                    try {
                        loginOutput.setText(loginUser(user, passWord));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (loginComplete) {
                        if(user.isStudent()){
                        Intent startIntent = new Intent(LoginActivity.this, StudentContainers.class);
                        startIntent.putExtra("com.swansea.sindiso.takeUser", user);
                        startActivity(startIntent);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button registerStudentBtn = (Button) findViewById(R.id.register_student_button);
        registerStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userName = userEditText.getText().toString();
                    String passWord = passwordEditText.getText().toString();
                    user = new User(-1, userName, passWord, true);
                    boolean complete = dataBaseHandler.addUser(user);
                    if (complete){
                        loginOutput.setText("Registered Successfully");
                    } else {
                        loginOutput.setText("User Already Exists");
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                    user = new User(-1, "error", "error", false);
                }
            }
        });

        Button registerHolderBtn = (Button) findViewById(R.id.register_holder_button);
        registerHolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userName = userEditText.getText().toString();
                    String passWord = passwordEditText.getText().toString();
                    user = new User(-1, userName, passWord, false);
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);
                    boolean complete = dataBaseHandler.addUser(user);
                    if (complete){
                        loginOutput.setText("Registered Successfully");
                    } else {
                        loginOutput.setText("User Already Exists");
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                    user = new User(-1, "error", "error", false);
                }
            }
        });
    }

    private String loginUser(User user, String password) throws FileNotFoundException {
        if (user.getPassword().equals(password)) {
            loginComplete = true;
            return ("Welcome");
        } else {
            return ("Incorrect Username and Password");
        }
    }
    

    private String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream file = openFileInput(fileName);
            int x;
            while ((x = file.read()) != -1) {
                sb.append((char) x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (sb.toString());
    }
}