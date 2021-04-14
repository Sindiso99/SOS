package com.swansea.sindiso;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private Button logout;
    private Button help;
    private Button myContainersBtn;
    private Button qrCodeBtn;
    private Button editLocation;
    private Button matchUsers;
    private TextView notificationHeader;
    private User user;
    private Intent startIntent;
    private DataBaseHandler dataBaseHandler;
    private boolean matchMe;
    private static final int CAMERA_PERMISSION_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        myContainersBtn = (Button) findViewById(R.id.manage_containers_btn);
        logout = (Button) findViewById(R.id.logoff_btn);
        qrCodeBtn = (Button) findViewById(R.id.manage_labels);
        matchUsers = (Button) findViewById(R.id.match_users);
        notificationHeader = (TextView) findViewById(R.id.notification_textView);
        editLocation = (Button) findViewById(R.id.manage_details);

        if (getIntent().hasExtra("com.swansea.sindiso.takeUser")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.takeUser");
        }

        if (!user.isStudent()) {
            Drawable icon = getApplicationContext().getResources().getDrawable(R.drawable.house_icon);
            icon.setBounds(0, 0, 100, 100);
            myContainersBtn.setCompoundDrawables(icon, null, null, null);
            myContainersBtn.setText("My Space");
            qrCodeBtn.setText("Boxes for me");
        }
        manageCompletion(user);
        if (!matchMe) {
            matchUsers.setBackgroundColor(getResources().getColor(R.color.grey_theme));
            qrCodeBtn.setBackgroundColor(getResources().getColor(R.color.grey_theme));
            notificationHeader.setText(getResources().getString(R.string.not_ready));
        } else if (!user.isReady()) {
            matchUsers.setText("Find My Match");
        }

        qrCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isReady()) {
                    if (manageCameraPermission()) {
                        if (user.isStudent()) {
                            startIntent = new Intent(HomePage.this, StudentLabels.class);
                            startIntent.putExtra("com.swansea.sindiso.student", user);
                        } else {

                        }
                        startActivity(startIntent);
                    }
                } else {
                    Toast.makeText(HomePage.this, "Please complete the above actions before proceeding", Toast.LENGTH_SHORT).show();
                }
            }
        });

        matchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!matchMe) {
                    Toast.makeText(HomePage.this, "Please complete the above actions before proceeding", Toast.LENGTH_SHORT).show();
                } else {
                    if (matchMe && !user.isReady()) {
                        askIfReady();
                    }
                }
            }
        });

        myContainersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isReady()) {
                    Toast.makeText(HomePage.this, "Details cannot be changed at the moment", Toast.LENGTH_SHORT).show();
                } else {
                    if (!user.isStudent()) {
                        startIntent = new Intent(HomePage.this, HolderSpace.class);
                        startIntent.putExtra("com.swansea.sindiso.owner", user);
                    } else {
                        startIntent = new Intent(HomePage.this, StudentContainers.class);
                        startIntent.putExtra("com.swansea.sindiso.student", user);
                    }
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(HomePage.this, LoginActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });


        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isReady()) {
                    Toast.makeText(HomePage.this, "Details cannot be changed at the moment", Toast.LENGTH_SHORT).show();
                } else {
                    startIntent = new Intent(HomePage.this, MapsActivity.class);
                    startIntent.putExtra("com.swansea.sindiso.user", user);
                    startActivity(startIntent);
                }
            }
        });
    }

    private void askIfReady() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomePage.this);
        alertDialogBuilder.setMessage(R.string.ask_to_match)
                .setTitle(R.string.match_dialog_title);
        alertDialogBuilder.setPositiveButton(R.string.yes_option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                user.setReady(true);
                dataBaseHandler = new DataBaseHandler(HomePage.this);
                dataBaseHandler.addUser(user, true);
                manageCompletion(user);
                matchUsers.setText("Manage My Storage");
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no_option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void manageCompletion(User user) {
        matchMe = true;
        dataBaseHandler = new DataBaseHandler(HomePage.this);
        if (user.isStudent()) {
            user.setContainers(dataBaseHandler.getAllContainers(user.getId()));
            if ((user.getContainers().get(0).getLabel().equals("no Containers"))) {
                matchMe = false;
            }
        } else {
            Space holderSpace = dataBaseHandler.getHolderSpace(user.getId());
            if (holderSpace.getOwnerId() == -1) {
                matchMe = false;
            }
        }
        user = dataBaseHandler.getAddressData(user);
        if (user.getEmail() == null || user.getFullAddress() == null) {
            matchMe = false;
        }
        if (dataBaseHandler.matchAlreadyFound(user.getId())) {
            notificationHeader.setText(getResources().getString(R.string.match_found));
            if (user.isStudent()) {
                matchUsers.setText("Manage my Storage");
            } else {
                matchUsers.setText("Manage my Students");
            }

            matchMe = true;
        } else {
            if (user.isReady()) {
                if (user.isStudent()) {
                    BinPacker binPacker = new BinPacker(HomePage.this, user, buildSpaces(user));
                    if (binPacker.pack()) {
                        notificationHeader.setText(getResources().getString(R.string.match_found));
                    } else {
                        notificationHeader.setText(getResources().getString(R.string.ready));
                    }
                } else {
                    notificationHeader.setText(getResources().getString(R.string.ready));
                }
            }
        }
        if (user.isReady()) {
            myContainersBtn.setBackgroundColor(getResources().getColor(R.color.grey_theme));
            editLocation.setBackgroundColor(getResources().getColor(R.color.grey_theme));
        }
    }

    private List<Space> buildSpaces(User user) {
        dataBaseHandler = new DataBaseHandler(HomePage.this);
        List<Space> spaces = new ArrayList<>();
        List<Integer> possibleMatchIds = dataBaseHandler.getAvailableUserIds(user.isStudent());
        for (Integer id : possibleMatchIds) {
            spaces.add(dataBaseHandler.getHolderSpace(id));
        }
        Toast.makeText(HomePage.this, spaces.get(0).getDescription(), Toast.LENGTH_SHORT).show();
        return spaces;
    }

    private boolean manageCameraPermission() {
        boolean permission = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
        } else {
            ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                permission = true;
            } else {
                Toast.makeText(HomePage.this, "Camera Permission Required for this action", Toast.LENGTH_SHORT).show();
            }
        }
        return permission;
    }
}