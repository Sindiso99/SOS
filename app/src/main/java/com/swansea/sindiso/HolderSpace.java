package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HolderSpace extends AppCompatActivity {

    private Button editDetails;
    private Button leaveActivity;
    private Intent intent;
    private User user;
    private Space space;
    private TextView ownerName;
    private TextView spaceSize;
    private TextView spaceDescription;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder_space);

        if (getIntent().hasExtra("com.swansea.sindiso.owner")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.owner");
        }

        DataBaseHandler dataBaseHandler = new DataBaseHandler(HolderSpace.this);
        space = dataBaseHandler.getHolderSpace(user.getId());

        ownerName = (TextView) findViewById(R.id.holderName_textView);
        spaceSize  =(TextView) findViewById(R.id.availableSpace_textView);
        spaceDescription = (TextView) findViewById(R.id.spaceDescription_textView);

        ownerName.setText(user.getUserName());
        spaceSize.setText(space.getVolume().toString() + "m\u00B3");
        spaceDescription.setText("\" " + space.getDescription() + " \"");

        leaveActivity = (Button) findViewById(R.id.leave_my_space);
        leaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HolderSpace.this, HomePage.class);
                startActivity(intent);
            }
        });

        editDetails = (Button) findViewById(R.id.editDetails_btn);
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HolderSpace.this, SpaceEditor.class);
                intent.putExtra("com.swansea.sindiso.ownerId", user.getId());
                startActivity(intent);
            }
        });


    }
}