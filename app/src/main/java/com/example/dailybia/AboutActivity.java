package com.example.dailybia;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mgoogleMap;
    Button btnCall;
    TextView textView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView2=findViewById(R.id.textView2);
        btnCall=findViewById(R.id.btnCall);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callAbout=new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+textView2.getText().toString()));
                if (ActivityCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AboutActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                startActivity(callAbout);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this,Call_phone.class);
                startActivity(intent);
            }
        });
        if (googleServiceAvailable()){
            initMap();

        }else {
            //No google map layout
            Toast.makeText(this,"no layout", Toast.LENGTH_LONG).show();
        }
    }

    private void initMap() {
        MapFragment mapFragemt = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mapFragemt.getMapAsync(AboutActivity.this);
    }


    public boolean googleServiceAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(AboutActivity.this);
        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        }else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(AboutActivity.this, isAvailable, 0);
            dialog.show();
        }else{
            Toast.makeText(AboutActivity.this,AboutActivity.this.getText(R.string.simple_errorServices), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mgoogleMap = googleMap;
        goToLocaltionZoom(10.738178, 106.677940, 17);
    }

    private void goToLocaltionZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        this.mgoogleMap.moveCamera(update);

        MarkerOptions options = new MarkerOptions().title((String) AboutActivity.this.getText(R.string.simple_stu)).position(ll);
        this.mgoogleMap.addMarker(options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
