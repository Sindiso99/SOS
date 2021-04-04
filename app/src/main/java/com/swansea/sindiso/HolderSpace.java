package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HolderSpace extends AppCompatActivity {

    private Button editDetails;
    private Button leaveActivity;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder_space);

        leaveActivity = (Button) findViewById(R.id.leave_my_space);

        leaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HolderSpace.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}