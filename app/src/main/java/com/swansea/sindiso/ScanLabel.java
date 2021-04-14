package com.swansea.sindiso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;

public class ScanLabel extends AppCompatActivity {

    private PreviewView previewView;
    private TextView qrCodeFound;
    private TextView containerOwnerName;
    private ProcessCameraProvider processCameraProvider;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private User user;
    private Preview preview;
    private CameraSelector cameraSelector;
    private Button leaveActivity;
    private Intent intent;
    private Integer qrCode;
    private Integer alreadyFound = -1;
    private DataBaseHandler dataBaseHandler;
    private Container container;
    private ListView containerListView;
    private User containerOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_label);
        leaveActivity = (Button) findViewById(R.id.leave_scan_label);
        previewView = (PreviewView) findViewById(R.id.scan_label_preview);
        qrCodeFound = (TextView) findViewById(R.id.qr_found_textView);
        containerListView = (ListView) findViewById(R.id.container_scanned);
        containerOwnerName = (TextView) findViewById(R.id.owner_name);
        qrCodeFound.setVisibility(View.INVISIBLE);

        if (getIntent().hasExtra("com.swansea.sindiso.User")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.User");
        }

        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(ScanLabel.this);
        dataBaseHandler = new DataBaseHandler(ScanLabel.this);
        initiateCamera();

        leaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ScanLabel.this, StudentLabels.class);
                startActivity(intent);
                overridePendingTransition(R.anim.swipe_out_left, R.anim.swipe_in_right);
            }
        });

    }

    private void initiateCamera() {
        cameraProviderListenableFuture.addListener(() -> {
            try {
                processCameraProvider = cameraProviderListenableFuture.get();
                bindPreview(processCameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(ScanLabel.this, "Couldn't start camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(ScanLabel.this));
    }

    private void bindPreview(ProcessCameraProvider processCameraProvider) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);
        preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(cameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ScanLabel.this), new QRLabelAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String foundQRCode) {
                try {
                    qrCode = parseInt(foundQRCode);
                    qrCodeFound.setVisibility(View.VISIBLE);
                    if (qrCode != alreadyFound){
                        alreadyFound = qrCode;
                        getContainerDetails();
                    }
                } catch (NumberFormatException e) {
                }
            }

            @Override
            public void qrCodeNotFound() {
                qrCodeFound.setVisibility(View.INVISIBLE);
            }
        }));
        Camera camera = processCameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageAnalysis, preview);
    }

    private void getContainerDetails() {
        container = dataBaseHandler.getContainerDetails(qrCode);
        ContainerAdapter containerAdapter = new ContainerAdapter(this, container);
        containerListView.setAdapter(containerAdapter);
        containerOwner = dataBaseHandler.getUserDetails(container.getOwnerId());
        containerOwnerName.setText(containerOwner.getUserName());
    }
}