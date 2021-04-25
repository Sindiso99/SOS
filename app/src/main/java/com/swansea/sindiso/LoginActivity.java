package com.swansea.sindiso;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {
    private boolean loginComplete = false;
    private EditText userEditText;
    private EditText passwordEditText;
    private User user;
    private Intent startIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                        startIntent = new Intent(LoginActivity.this, HomePage.class);
                        startIntent.putExtra("com.swansea.sindiso.login", user);
                        startActivity(startIntent);
                        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
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
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);
                    if (dataBaseHandler.usernameTaken(userName)) {
                        loginOutput.setText("Username already in use");
                    } else {
                        askToAgree(userName, passWord, true);
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
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
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);
                    if (dataBaseHandler.usernameTaken(userName)) {
                        loginOutput.setText("Username already in use");
                    } else {
                        askToAgree(userName, passWord, false);
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
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

    private void askToAgree(String username, String password, boolean student) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        if (student){
            alertDialogBuilder.setMessage(R.string.register_student);
        } else {
            alertDialogBuilder.setMessage(R.string.register_holder);
        }
        alertDialogBuilder.setTitle(R.string.register_header);
        alertDialogBuilder.setPositiveButton(R.string.yes_to_register, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);
                user = new User(-1, username, password, student, false);
                if (dataBaseHandler.addUser(user, false)){
                    startIntent = new Intent(LoginActivity.this, HomePage.class);
                    startIntent.putExtra("com.swansea.sindiso.newUser", user);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                }

            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no_to_register, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}