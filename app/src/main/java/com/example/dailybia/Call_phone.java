package com.example.dailybia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Call_phone extends AppCompatActivity {

    EditText edtSDT;
    ImageButton imbCall;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_phone);

        edtSDT=findViewById(R.id.edtSDT);
        imbCall=findViewById(R.id.imbCall);
        btnBack=findViewById(R.id.btnBack);

        imbCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+edtSDT.getText().toString()));
                if (ActivityCompat.checkSelfPermission(Call_phone.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Call_phone.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                startActivity(callIntent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}