package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContainerEditor extends AppCompatActivity {

    private Integer ownerId;
    private Button leaveActivityBtn;
    private Button saveContainerBtn;
    private Button deleteContainerBtn;
    private EditText labelInput, lengthInput, widthInput, heightInput, descInput;
    private TextView viewObjective;
    private Intent intent;
    private Container container;
    private boolean existingContainer = false;
    private DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_editor);
        labelInput = (EditText) findViewById(R.id.label_Edit);
        lengthInput = (EditText) findViewById(R.id.length_Edit);
        widthInput = (EditText) findViewById(R.id.width_edit);
        heightInput = (EditText) findViewById(R.id.height_edit);
        descInput = (EditText) findViewById(R.id.description_edit);
        viewObjective = (TextView) findViewById(R.id.edit_task);

        if (getIntent().hasExtra("com.swansea.sindiso.studentId")) {
        ownerId = getIntent().getIntExtra("com.swansea.sindiso.studentId", -1);
        viewObjective.setText("New Box");
        }

        if (getIntent().hasExtra("com.swansea.sindiso.containerToEdit")) {
            container = getIntent().getParcelableExtra("com.swansea.sindiso.containerToEdit");
            existingContainer = true;
            labelInput.setText(container.getLabel());
            lengthInput.setText(container.getLength().toString());
            widthInput.setText(container.getWidth().toString());
            heightInput.setText(container.getHeight().toString());
            descInput.setText(container.getDescription());
            viewObjective.setText("Edit Box");
        }

        leaveActivityBtn = (Button) findViewById(R.id.leave_editor);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ContainerEditor.this, StudentContainers.class);
                startActivity(intent);
                overridePendingTransition( R.anim.swipe_out_left, R.anim.swipe_in_right);
            }
        });

        saveContainerBtn = (Button) findViewById(R.id.enter_details_btn);
        saveContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (existingContainer) {
                        container = updatedContainer();
                    } else {
                        container = new Container(-1, labelInput.getText().toString(),
                                Integer.parseInt(lengthInput.getText().toString()), Integer.parseInt(heightInput.getText().toString()),
                                Integer.parseInt(widthInput.getText().toString()), descInput.getText().toString(), ownerId);
                    }
                    Toast.makeText(ContainerEditor.this, container.toString(), Toast.LENGTH_SHORT).show();
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(ContainerEditor.this);
                    boolean complete = dataBaseHandler.addContainer(container, existingContainer);
                    if(complete) {
                        intent = new Intent(ContainerEditor.this, StudentContainers.class);
                        startActivity(intent);
                        overridePendingTransition( R.anim.swipe_out_left, R.anim.swipe_in_right);
                    }
                } catch (Exception e) {
                    Toast.makeText(ContainerEditor.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteContainerBtn = (Button) findViewById(R.id.delete_button);
        deleteContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(existingContainer){
                        dataBaseHandler = new DataBaseHandler(ContainerEditor.this);
                        if(dataBaseHandler.deleteContainer(container.getId())){
                            Toast.makeText(ContainerEditor.this, R.string.delete_complete, Toast.LENGTH_SHORT).show();
                            intent = new Intent(ContainerEditor.this, StudentContainers.class);
                            startActivity(intent);
                            overridePendingTransition( R.anim.swipe_out_left, R.anim.swipe_in_right);
                        } else {
                            Toast.makeText(ContainerEditor.this, R.string.delete_failed, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContainerEditor.this, R.string.cannot_delete, Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private Container updatedContainer(){
        Container newContainer = new Container(container.getId(), labelInput.getText().toString(),
                Integer.parseInt(lengthInput.getText().toString()), Integer.parseInt(heightInput.getText().toString()),
                Integer.parseInt(widthInput.getText().toString()), descInput.getText().toString(), container.getOwnerId());
        return newContainer;
    }
}