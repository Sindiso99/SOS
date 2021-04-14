package com.swansea.sindiso;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentLabels extends AppCompatActivity {

    private ListView containerListView;
    private Intent intent;
    private User user;
    private TextView toolbarHeader;
    private Button leaveActivityBtn;
    private Button scanQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_labels);

        Resources res = getResources();
        scanQrCode = (Button) findViewById(R.id.scan_qr_codes);
        containerListView = (ListView) findViewById(R.id.boxes_list);
        toolbarHeader = (TextView) findViewById(R.id.view_my_boxes_header);

        if (getIntent().hasExtra("com.swansea.sindiso.student")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.student");
            toolbarHeader.setText("View your Boxes");
        }

        DataBaseHandler dataBaseHandler = new DataBaseHandler(StudentLabels.this);
        user.setContainers(dataBaseHandler.getAllContainers(user.getId()));
        ContainerAdapter containerAdapter = new ContainerAdapter(this, user.getContainers());
        containerListView.setAdapter(containerAdapter);
        containerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(StudentLabels.this, ContainerQrCode.class);
                intent.putExtra("com.swansea.sindiso.container", containerAdapter.getItem(position));
                startActivity(intent);
                overridePendingTransition(R.anim.swipe_in_right, R.anim.swipe_out_left);
            }
        });

        leaveActivityBtn = (Button) findViewById(R.id.leave_my_boxes);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intent = new Intent(StudentLabels.this, HomePage.class);
               intent.putExtra("com.swansea.sindiso.takeUser", user);
               startActivity(intent);
            }
        });

        scanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StudentLabels.this, ScanLabel.class);
                intent.putExtra("com.swansea.sindiso.User", user);
                startActivity(intent);
            }
        });
    }



}