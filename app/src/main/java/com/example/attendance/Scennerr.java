package com.example.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.attendance.Service.Response.ErrorResponse;
import com.example.attendance.Service.Response.LoginResponse;
import com.example.attendance.Service.Response.ScannerResponse;
import com.example.attendance.Service.Response.Service;
import com.example.attendance.Service.Response.Student;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Service.SF;
import com.example.attendance.Student.StudScannedsuccessfully;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scennerr extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 200;
    private Button scanQR;
    private TextView showQRtext;
    private SurfaceView surfaceView;
    private TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Dialog dialog;
    private String intentData = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scennerr);

        scanQR = findViewById(R.id.idScanQr);
        showQRtext = findViewById(R.id.showQRtext);

        scanQR.setOnClickListener(v -> initialiseDetectorsAndSources());
    }

    private void setupRegisterApiCall(int courseId, int facultyId) {
        int studentId = SF.getLoginSF(Scennerr.this).getInt(SF.LOGIN_USER_ID, 0);
        String regNo = SF.getLoginSF(Scennerr.this).getString(SF.LOGIN_USER_REG_NO, "");
        String name = SF.getLoginSF(Scennerr.this).getString(SF.LOGIN_USER_NAME,"");
        Student student = new Student(regNo, name, facultyId, courseId, studentId);

        RestClient.makeAPI().registerStudent(student).enqueue(new Callback<ScannerResponse>() {
            @Override
            public void onResponse(Call<ScannerResponse> call, Response<ScannerResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Scennerr.this, "Student registered successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    handleErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ScannerResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(Scennerr.this, "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleErrorResponse(ResponseBody responseBody) {
        try {
            if (responseBody != null) {
                ErrorResponse errorResponse = new Gson().fromJson(responseBody.charStream(), ErrorResponse.class);
                Toast.makeText(Scennerr.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Scennerr.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Scenner", "Error parsing error response", e);
            Toast.makeText(Scennerr.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void initialiseDetectorsAndSources() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.scan_qr_code_dialog_layout);
        dialog.show();

        txtBarcodeValue = dialog.findViewById(R.id.txtBarcodeValue);
        surfaceView = dialog.findViewById(R.id.surfaceView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Scennerr.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Scennerr.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks, barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(() -> {
                        intentData = barcodes.valueAt(0).displayValue;
                        txtBarcodeValue.setText(intentData);
                        showQRtext.setText(intentData);
                        try {
                            QRData data = new Gson().fromJson(intentData, QRData.class);
                            setupRegisterApiCall(data.getCourseCode(), data.facultyId);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            Toast.makeText(Scennerr.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });
    }

    private static class QRData {
        private int courseCode;
        private int facultyId;

        public int getCourseCode() {
            return courseCode;
        }

        public int getFacultyId() {
            return facultyId;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialiseDetectorsAndSources();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void submit(View v) {
        Intent i = new Intent(this, StudScannedsuccessfully.class);
        startActivity(i);
    }
}
