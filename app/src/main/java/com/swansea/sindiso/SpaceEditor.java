package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SpaceEditor extends AppCompatActivity {

    private Integer ownerId;
    private Button leaveActivityBtn;
    private Button saveSpaceBtn;
    private EditText lengthInput;
    private EditText widthInput;
    private EditText heightInput;
    private EditText descInput;
    private TextView task;
    private Intent intent;
    private Space space;
    private DataBaseHandler dataBaseHandler;
    private boolean existingSpace = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_editor);
        lengthInput = (EditText) findViewById(R.id.space_length_Edit);
        widthInput = (EditText) findViewById(R.id.space_width_edit);
        heightInput = (EditText) findViewById(R.id.space_height_edit);
        descInput = (EditText) findViewById(R.id.space_description_edit);
        task = (TextView) findViewById(R.id.space_edit_task);

        if (getIntent().hasExtra("com.swansea.sindiso.ownerId")) {
            ownerId = getIntent().getIntExtra("com.swansea.sindiso.ownerId", -1);
        }
        dataBaseHandler = new DataBaseHandler(SpaceEditor.this);
        space = dataBaseHandler.getHolderSpace(ownerId);
        if (space.getOwnerId() != -1){
            existingSpace = true;
            lengthInput.setText(space.getLength().toString());
            heightInput.setText(space.getHeight().toString());
            widthInput.setText(space.getWidth().toString());
            descInput.setText(space.getDescription());
            task.setText("Edit Space Details");
        }

        leaveActivityBtn = (Button) findViewById(R.id.leave_space_editor);
        leaveActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SpaceEditor.this, HolderSpace.class);
                startActivity(intent);
            }
        });

        saveSpaceBtn = (Button) findViewById(R.id.enter_space_details_btn);
        saveSpaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    space = new Space(ownerId, Integer.parseInt(lengthInput.getText().toString()), Integer.parseInt(heightInput.getText().toString()),
                            Integer.parseInt(widthInput.getText().toString()), descInput.getText().toString());
                    if(dataBaseHandler.addHolderSpace(space, existingSpace)){
                        intent = new Intent(SpaceEditor.this, HolderSpace.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Toast.makeText(SpaceEditor.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}