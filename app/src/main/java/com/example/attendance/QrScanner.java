package com.example.attendance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.attendance.Service.SF;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.Calendar;

public class QrScanner extends AppCompatActivity {

    private AppCompatButton etDob;
    private EditText inputText;
    private Button generateButton,go_to_afterscanned;
    private ImageView qrCodeImage;

    int userId;
    int courseId;
    String qrText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        // Initialize UI components
        inputText = findViewById(R.id.input_text);
        generateButton = findViewById(R.id.generate_button);
        qrCodeImage = findViewById(R.id.qr_code_image);
        go_to_afterscanned = findViewById(R.id.to_after_scanned);
        etDob = findViewById(R.id.etdob);

        userId = SF.getLoginSF(QrScanner.this).getInt(SF.LOGIN_USER_ID, 0);
        courseId = SF.getLoginSF(QrScanner.this).getInt(SF.COURSE_CODE_FOR_QR_SCANNER, 0);
        // Concatenate values or format as needed for QR code
        qrText = generateStudentJson(courseId, userId);
        inputText.setText(qrText);
        generateQRCode(qrText);
        Log.e("QRClass", qrText);

        go_to_afterscanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrScanner.this,AfterScanned.class);
                startActivity(intent);
            }
        });

        generateButton.setOnClickListener(v -> {
            userId = SF.getLoginSF(QrScanner.this).getInt(SF.LOGIN_USER_ID, 0);
            courseId = SF.getLoginSF(QrScanner.this).getInt(SF.COURSE_CODE_FOR_QR_SCANNER, 0);
                // Concatenate values or format as needed for QR code
            qrText = generateStudentJson(courseId, userId);
            inputText.setText(qrText);
            generateQRCode(qrText);
        });
        // Set click listener for the date picker
        etDob.setOnClickListener(v -> showDatePicker());
    }

    @Nullable
    public static String generateStudentJson(int courseCode, int facultyId) {
        try {
            // Create a JSON object
            JSONObject jsonObject = new JSONObject();

            // Add key-value pairs
            jsonObject.put("courseCode", courseCode);
            jsonObject.put("facultyId", facultyId);

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle error
        }
    }

    // Method to generate QR Code from text input
    private void generateQRCode(String text) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to show DatePickerDialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // Show DatePickerDialog
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etDob.setText(selectedDate); // Update the EditText with the selected date
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
