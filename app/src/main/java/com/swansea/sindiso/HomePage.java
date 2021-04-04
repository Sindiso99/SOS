package com.swansea.sindiso;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    private Button logout;
    private Button help;
    private Button myContainersBtn;
    private Button qrCodeBtn;
    private User user;
    private Intent startIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        myContainersBtn = (Button) findViewById(R.id.manage_containers_btn);
        logout = (Button) findViewById(R.id.logoff_btn);
        qrCodeBtn = (Button) findViewById(R.id.manage_labels);

        if (getIntent().hasExtra("com.swansea.sindiso.takeUser")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.takeUser");
        }

        if(!user.isStudent()){
            Drawable icon = getApplicationContext().getResources().getDrawable(R.drawable.house_icon);
            icon.setBounds(0, 0, 100 ,100);
            myContainersBtn.setCompoundDrawables(icon, null, null, null);
            myContainersBtn.setText("My Space");
            qrCodeBtn.setText("Boxes for me");
        }


        myContainersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.isStudent()){
                    startIntent = new Intent (HomePage.this, HolderSpace.class);
                } else {
                startIntent = new Intent(HomePage.this, StudentContainers.class);
                startIntent.putExtra("com.swansea.sindiso.takeUser", user);
                }
                startActivity(startIntent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(HomePage.this, LoginActivity.class);
                startActivity(startIntent);
            }
        });
    }
}