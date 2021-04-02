package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContainerEditor extends AppCompatActivity {

    private int ownerId;
    private Button leaveActivityBtn;
    private Button saveContainerBtn;
    private EditText labelInput, lengthInput, widthInput, heightInput, descInput;
    private Intent intent;
    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_editor);
        labelInput = (EditText) findViewById(R.id.label_Edit);
        lengthInput = (EditText) findViewById(R.id.length_Edit);
        widthInput = (EditText) findViewById(R.id.width_edit);
        heightInput = (EditText) findViewById(R.id.height_edit);
        descInput = (EditText) findViewById(R.id.description_edit);

        if (getIntent().hasExtra("com.swansea.sindiso.studentId")) {
        ownerId = getIntent().getIntExtra("com.swansea.sindiso.studentId", -1);
        }

        if (getIntent().hasExtra("com.swansea.sindiso.containerToEdit")) {
            container = getIntent().getParcelableExtra("com.swansea.sindiso.containerToEdit");
            labelInput.setText(container.getLabel());
            lengthInput.setText(container.getLength().toString());
            widthInput.setText(container.getWidth().toString());
            heightInput.setText(container.getHeight().toString());
            descInput.setText(container.getDescription());
        }

        leaveActivityBtn = (Button) findViewById(R.id.leave_editor);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ContainerEditor.this, StudentContainers.class);
                startActivity(intent);
            }
        });

        saveContainerBtn = (Button) findViewById(R.id.enter_details_btn);
        saveContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    container = new Container(-1, labelInput.getText().toString(),
                            Integer.parseInt(lengthInput.getText().toString()), Integer.parseInt(heightInput.getText().toString()),
                            Integer.parseInt(widthInput.getText().toString()), descInput.getText().toString(), ownerId);
                    Toast.makeText(ContainerEditor.this, container.toString(), Toast.LENGTH_SHORT).show();
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(ContainerEditor.this);
                    boolean complete = dataBaseHandler.addContainer(container);
                    if(complete) {
                        intent = new Intent(ContainerEditor.this, StudentContainers.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(ContainerEditor.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}