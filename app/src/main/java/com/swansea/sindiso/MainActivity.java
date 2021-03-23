package com.swansea.sindiso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private String fileName = "userInformation.txt";
    private boolean loginComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        EditText userEditText = (EditText) findViewById(R.id.userNameTextBox);
        EditText passwordEditText = (EditText) findViewById(R.id.PasswordTextBox);
        TextView loginOutput = (TextView) findViewById(R.id.loginOutcome);

        Button loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userEditText.getText().toString();
                String passWord = passwordEditText.getText().toString();
                try {
                    loginOutput.setText(loginUser(userName, passWord));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(loginComplete){
                    Intent startIntent = new Intent(MainActivity.this, student_containers.class);
                    startIntent.putExtra("com.swansea.sindiso.takeUser", userName);
                    startActivity(startIntent);
                }
            }
        });

        Button registerBtn = (Button) findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userEditText.getText().toString();
                String passWord = passwordEditText.getText().toString();
                loginOutput.setText(newUser(userName, passWord));
            }
        });

    }

    private String loginUser(String userName, String password) throws FileNotFoundException {
        String data = readFile(fileName);
        Scanner users = new Scanner(data);
        while(users.hasNextLine()){
            Scanner user = new Scanner(users.nextLine());
            if(userName.equals(user.next())){
                if(password.equals(user.next())){
                    loginComplete = true;
                    return ("Welcome " + userName);
                } else {
                    return ("Incorrect Password");
                }
            }
        }
        return ("Incorrect Username and Password");
    }

    private String newUser(String userName, String Password){
        if (existingUser(userName)){
            return ("Already Existing User");
        }
        try{
            FileOutputStream file = openFileOutput(fileName, Context.MODE_APPEND);
            String user = (userName + " " + Password + "\n");
            file.write(user.getBytes());
            file.flush();
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
            return ("File not found");
        }
        return ("New User Created");
    }

    private boolean existingUser(String userName){
        String data = readFile(fileName);
        Scanner users = new Scanner(data);
        while(users.hasNextLine()){
            Scanner user = new Scanner(users.nextLine());
            if (userName.equals(user.next())){
                return true;
            }
        }
        return false;
    }

    private String readFile(String fileName){
        StringBuilder sb = new StringBuilder();
        try{
          FileInputStream file = openFileInput(fileName);
          int x;
          while ((x = file.read()) != -1) {
              sb.append((char) x);
          }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return(sb.toString());
    }
}