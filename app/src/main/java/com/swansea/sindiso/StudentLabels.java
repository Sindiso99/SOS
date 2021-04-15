package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentLabels extends AppCompatActivity {

    private ListView containerListView;
    private Intent intent;
    private User user;
    private TextView toolbarHeader;
    private Button leaveActivityBtn;
    private Button scanQrCode;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private List<User> myStudents;
    private List<String> myStudentNames;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_labels);

        scanQrCode = (Button) findViewById(R.id.scan_qr_codes);
        containerListView = (ListView) findViewById(R.id.boxes_list);
        toolbarHeader = (TextView) findViewById(R.id.view_my_boxes_header);
        spinner = (Spinner) findViewById(R.id.my_students_spinner);
        header = (TextView) findViewById(R.id.my_boxes_list_header);

        DataBaseHandler dataBaseHandler = new DataBaseHandler(StudentLabels.this);

        if (getIntent().hasExtra("com.swansea.sindiso.user")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.user");

        }

        if(user.isStudent()) {
            toolbarHeader.setText("View your Boxes");
            spinner.setVisibility(View.INVISIBLE);
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
        } else {
            handleHolder();
        }




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

    private void handleHolder(){
        toolbarHeader.setText("View Boxes For You");
        header.setText("My Student's Boxes");
        DataBaseHandler dataBaseHandler = new DataBaseHandler(StudentLabels.this);
        List<Integer> myStudentIds = dataBaseHandler.getMatches(user);
        myStudents = new ArrayList<>();
        myStudentNames = new ArrayList<>();
        for (Integer curId : myStudentIds){
            User tempUser;
            tempUser = dataBaseHandler.getUserDetails(curId);
            myStudents.add(tempUser);
            myStudentNames.add(tempUser.getUserName());
        }
        adapter = new ArrayAdapter<String>(StudentLabels.this, android.R.layout.simple_spinner_item, myStudentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User currentUser = myStudents.get(position);
                currentUser.setContainers(dataBaseHandler.getAllContainers(currentUser.getId()));
                ContainerAdapter containerAdapter = new ContainerAdapter(StudentLabels.this, currentUser.getContainers());
                containerListView.setAdapter(containerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });
    }



}