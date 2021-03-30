package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContainerEditor extends AppCompatActivity {

    Button leaveActivityBtn;
    Button saveContainerBtn;
    EditText labelInput, lengthInput, widthInput, heightInput, descInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_editor);
        Intent intent = getIntent();
        int position = intent.getIntExtra("com.swansea.sindiso.CONTAINER_INDEX", -1);

        labelInput = (EditText) findViewById(R.id.label_Edit);
        lengthInput = (EditText) findViewById(R.id.length_Edit);
        widthInput = (EditText) findViewById(R.id.width_edit);
        heightInput = (EditText) findViewById(R.id.height_edit);
        descInput = (EditText) findViewById(R.id.description_edit);

        leaveActivityBtn = (Button) findViewById(R.id.leave_editor);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(ContainerEditor.this, student_containers.class);
                startActivity(startIntent);
            }
        });

        saveContainerBtn = (Button) findViewById(R.id.enter_details_btn);
        saveContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Container container = new Container(labelInput.getText().toString(),
                            Integer.parseInt(lengthInput.getText().toString()), Integer.parseInt(heightInput.getText().toString()),
                            Integer.parseInt(widthInput.getText().toString()), descInput.getText().toString());
                    Toast.makeText(ContainerEditor.this, container.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ContainerEditor.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}