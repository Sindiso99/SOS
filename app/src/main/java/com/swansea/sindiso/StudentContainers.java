package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentContainers extends AppCompatActivity {

    private ListView containerListView;
    private Button addContainerBtn;
    private Intent editContainer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_containers);
        TextView studentHeader = (TextView) findViewById(R.id.student_header);
        containerListView = (ListView) findViewById(R.id.container_list);

        if (getIntent().hasExtra("com.swansea.sindiso.student")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.student");
            studentHeader.setText(user.getUserName());
        }
        DataBaseHandler dataBaseHandler = new DataBaseHandler(StudentContainers.this);

        user.setContainers(dataBaseHandler.getAllContainers(user.getId()));
        ContainerAdapter containerAdapter = new ContainerAdapter(this, user.getContainers());
        containerListView.setAdapter(containerAdapter);
        containerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editContainer = new Intent(StudentContainers.this, ContainerEditor.class);
                editContainer.putExtra("com.swansea.sindiso.containerToEdit", containerAdapter.getItem(position));
                startActivity(editContainer);
                overridePendingTransition(R.anim.swipe_in_right, R.anim.swipe_out_left);
            }
        });

        Button leaveActivityBtn = (Button) findViewById(R.id.leave_activity);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(StudentContainers.this, HomePage.class);
                startIntent.putExtra("com.swansea.sindiso.takeUser", user);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        addContainerBtn = (Button) findViewById(R.id.add_Container_btn);
        addContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContainer = new Intent(StudentContainers.this, ContainerEditor.class);
                editContainer.putExtra("com.swansea.sindiso.studentId", user.getId());
                startActivity(editContainer);
            }
        });

    }


}