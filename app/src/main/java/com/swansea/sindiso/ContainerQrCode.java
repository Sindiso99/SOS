package com.swansea.sindiso;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ContainerQrCode extends AppCompatActivity {

    private Container container;
    private Intent intent;
    private ImageView qrCode;
    private TextView boxLabel;
    private Button leaveActivity;
    private Button saveQrCode;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private WindowManager windowManager;
    private Display display;
    private Point point;
    private PrintHelper printHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_qr_code);

        qrCode = (ImageView) findViewById(R.id.qr_code_imageView);
        saveQrCode = (Button) findViewById(R.id.print_qr);
        leaveActivity = (Button) findViewById(R.id.leave_qr_code);
        boxLabel = (TextView) findViewById(R.id.box_label_textView);

        if (getIntent().hasExtra("com.swansea.sindiso.container")) {
            container = getIntent().getParcelableExtra("com.swansea.sindiso.container");
            generateQrCode();
        }

        leaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ContainerQrCode.this, StudentLabels.class);
                startActivity(intent);
            }
        });

        saveQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }

    //geeksforgeeks tutorial
    private void generateQrCode(){
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        qrgEncoder = new QRGEncoder(container.getId().toString(), null, QRGContents.Type.TEXT, dimen);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrCode.setImageBitmap(bitmap);
            boxLabel.setText(container.getLabel());
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }
    }

    private void print() {
        printHelper = new PrintHelper(ContainerQrCode.this);
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap("BoxID " + container.getId() + " QR Code.jpg", bitmap);
    }

}