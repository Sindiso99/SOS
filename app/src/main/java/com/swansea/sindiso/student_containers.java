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

public class student_containers extends AppCompatActivity {

    ListView containerListView;
    String[] containerNames;
    String[] containerDescriptions;
    String[] containerVolumes;
    Button addContainerBtn;
    Intent editContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_containers);
        TextView studentHeader = (TextView) findViewById(R.id.student_header);

        Resources res = getResources();
        containerListView = (ListView) findViewById(R.id.container_list);
        containerNames = res.getStringArray(R.array.containers);
        containerDescriptions = res.getStringArray(R.array.descriptions);
        containerVolumes = res.getStringArray(R.array.volumes);

        ContainerAdapter containerAdapter = new ContainerAdapter(this, containerNames, containerDescriptions, containerVolumes);
        containerListView.setAdapter(containerAdapter);
        containerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editContainer = new Intent(student_containers.this, ContainerEditor.class);
                editContainer.putExtra("com.swansea.sindiso.CONTAINER_INDEX", position);
                startActivity(editContainer);
            }
        });

        if (getIntent().hasExtra("com.swansea.sindiso.takeUser")) {
            User user = getIntent().getParcelableExtra("com.swansea.sindiso.takeUser");
            studentHeader.setText(user.getUserName());
        }

        Button leaveActivityBtn = (Button) findViewById(R.id.leave_activity);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(student_containers.this, MainActivity.class);
                startActivity(startIntent);
            }
        });

        addContainerBtn = (Button) findViewById(R.id.add_Container_btn);
        addContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContainer = new Intent(student_containers.this, ContainerEditor.class);
                //editContainer.putExtra("com.swansea.sindiso.USER_ID", position);
                startActivity(editContainer);
            }
        });

    }


}