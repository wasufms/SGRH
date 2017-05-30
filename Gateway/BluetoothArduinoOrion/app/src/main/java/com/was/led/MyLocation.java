package com.was.led;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wasuf on 21/10/2016.
 */
public class MyLocation extends AppCompatActivity {
    private static final int REQUEST_PERMISSOES=3;
    private static final String TAG= "mapaa";

    private Location location;
    private LocationManager locationManager;





    public String getMyLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            //return " was";


            permissoesHabilitadas();
            Toast.makeText(this,"Porr",Toast.LENGTH_SHORT).show();

        }else {
            locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        double longitude=location.getLongitude();
        double latitude=location.getLatitude();
        //String local=""++","+;
        String local=""+latitude+","+longitude;
        return local;
    }


    private boolean permissoesHabilitadas(){
        List<String> permissoes=new ArrayList<String>();

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissoes.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(!permissoes.isEmpty()){
            String[] array=new String[permissoes.size()];
            permissoes.toArray(array);
            ActivityCompat.requestPermissions(this,array,3);

        }
        return permissoes.isEmpty();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode,  permissions,  grantResults);
        boolean sucesso=true;
        for(int i=0;i<permissions.length;i++){
            if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                sucesso=false;
                break;
            }
        }

        if(!sucesso){
            Toast.makeText(this,"Deve permitir",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}
