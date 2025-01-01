// SplashScreen.java
package com.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.attendance.Service.SF;
import com.example.attendance.Student.StudentHome;

public class SplashScreen extends AppCompatActivity {
    FragmentActivity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Find the button by its ID
        Button btnGoToLogin = findViewById(R.id.btn_go_to_login);

        // Set an OnClickListener on the button
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to FacultyLogin activity
                Intent intent = new Intent(SplashScreen.this, FacultyLogin.class);
                startActivity(intent);
                finish();
            }
        });
        try {
            context = SplashScreen.this;
            activity = SplashScreen.this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(SF.getLoginSF(activity).getBoolean(SF.LOGIN_USER_LOGGED_IN, false)) {
            int userType = SF.getLoginSF(activity).getInt(SF.LOGIN_USER_TYPE, 0);
            if (userType == 100) {
                startActivity(new Intent(context, StudentHome.class));
            }else if(userType == 110) {
                startActivity(new Intent(context, Courses.class));
            }
        }
    }
}
