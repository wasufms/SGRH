package com.was.led;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.was.controller.TokenActivity;

import java.util.ArrayList;
import java.util.List;

import maps.GeofenceReceiver;

import model.*;
import model.Cisterna;
import persistence.CisternaDAO;
import persistence.CurrentEntrega;
import persistence.CurrentEntregaDAO;
import persistence.EntregaDAO;
import persistence.ManancialDAO;
import services.*;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapLongClickListener
{


    private static final int REQUEST_PERMISSIONS=3;
    private static final String TAG="map";




    private static final int REQUEST_ERRO_PLAY_SERVICES = 1;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LatLng mOrigem;

    PendingIntent mGeofencePendingIntent;
    private List<Geofence> mGeofenceList;


    private Entrega entrega;
    private Cisterna cisterna;
    private Manancial manancial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGeofenceList = new ArrayList<Geofence>();


        //Intent it = getIntent();
        //entrega = (Entrega) it.getSerializableExtra("entrega");
        //cisterna = (Cisterna) it.getSerializableExtra("cisterna");
        //manancial = (Manancial) it.getSerializableExtra("manancial");

        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        CurrentEntrega currentEntrega=currentEntregaDAO.getById(1);
        Log.d("DAO-1",currentEntrega.toString() );

        entrega=currentEntregaDAO.getCurrentEntrega(getApplicationContext());
        Log.d("DAO-1",entrega.toString());
        cisterna=currentEntregaDAO.getCurrentCisterna(getApplicationContext());
        Log.d("DAO-1",cisterna.toString());
        manancial=currentEntregaDAO.getCurrentManancial(getApplicationContext());
        Log.d("DAO-1",manancial.toString());

        /*EntregaDAO entregaDAO=new EntregaDAO(getApplication());
        List<Entrega>entregas=entregaDAO.getAll();
        for(int i=0;i<entregas.size();i++){
            Log.d("DAO-1",entregas.get(i).toString() );
        }*/



      /*  EntregaDAO entregaDAO=new EntregaDAO(getApplicationContext());
        //entrega=entregaDAO.getById(entrega.getId());
        entrega=entregaDAO.getById(currentEntrega.getEntregaId());

        CisternaDAO cisternaDAO=new CisternaDAO(getApplicationContext());
        cisterna=cisternaDAO.getById(entrega.getCisterna());
        Log.d("DAO",cisterna.toString());

        ManancialDAO manancialDAO=new ManancialDAO(getApplicationContext());
        manancial=manancialDAO.getById(entrega.getManancial());
        Log.d("DAO",manancial.toString());*/

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if (mGoogleApiClient.isConnected()) {

            PendingIntent pit = PendingIntent.getBroadcast(
                    this, 0, new Intent(this, GeofenceReceiver.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            List<Geofence> geofences = new ArrayList<Geofence>();

            Geofence geofence = new Geofence.Builder()
                    .setRequestId("2")
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setCircularRegion(latLngs.get(0).latitude, latLngs.get(0).longitude, 200)
                   // .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setExpirationDuration(24*60*60*1000)
                    .build();
            geofences.add(geofence);

            geofence = new Geofence.Builder()
                    .setRequestId("1")
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setCircularRegion(latLngs.get(1).latitude, latLngs.get(1).longitude, 200)
                    // .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setExpirationDuration(24*60*60*1000)
                    .build();
            geofences.add(geofence);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},3);
            }

            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofences, pit)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Toast.makeText(MapsActivity.this, "Entrei", Toast.LENGTH_SHORT).show();
                                Log.d("MAPA", "ATE AQUI FILE");
                                atualizarMap();
                            }
                        }
                    });

        }else{
            Log.d("MAPA","Achei o ERRO");
        }
        Log.d("MAPA","Deveria cria a roda"+latLngs.get(0).toString());
    }

  /*  private void iniciarDeteccaoDeLocal() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},3);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }*/

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        obterUltimaLocalizacao();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_ERRO_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            exibirMensagemDeErro(this, connectionResult.getErrorCode());
        }
    }

    private void obterUltimaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            mOrigem = new LatLng(location.getLatitude(), location.getLongitude());
            atualizarMap();
        }
    }

    private void atualizarMap() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 12.0f));
        mMap.clear();
        addMarcadores();
        //iniciarDeteccaoDeLocal();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.addCircle(new CircleOptions()
                .strokeWidth(2)
                .fillColor(0x990000FF)
                .center(latLngs.get(0))
                .radius(200)

        );
        mMap.addCircle(new CircleOptions()
                .strokeWidth(2)
                .fillColor(0x990000FF)
                .center(latLngs.get(1))
                .radius(200)

        );
    }

    private void exibirMensagemDeErro(FragmentActivity activity, final int codigoDoErro) {
        final String TAG = "DIALOG_ERRO_PLAY_SERVICES";
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            DialogFragment errorFragment = new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    return GooglePlayServicesUtil.getErrorDialog(codigoDoErro, getActivity(), REQUEST_ERRO_PLAY_SERVICES);
                }
            };
            errorFragment.show(activity.getSupportFragmentManager(), TAG);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        // Add a marker in Sydney and move the camera
        //Cin String latLong="-8.055861, -34.951083";
        //Santo Aleixo String latLong="-8.100651, -35.017939";
        //Boa Viagem String latLong="-8.052833942276106,-34.91650644689798";
        String latLong = "-8.100651, -35.017939";
        String[] localizacao = latLong.split(",");
        LatLng latlng = new LatLng(Double.parseDouble(localizacao[0]), Double.parseDouble(localizacao[1]));


        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(latlng).title("Cisterna"));
        addMarcadores();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)
                .zoom(11)
                .build();

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        mMap.setMyLocationEnabled(true);

    }
    private List<LatLng>latLngs;
    private void addMarcadores(){
        latLngs=new ArrayList<LatLng>();
        String[] localizacao=cisterna.getPosition().split(",");
        LatLng latlng=new LatLng(Double.parseDouble(localizacao[0]),Double.parseDouble(localizacao[1]));
        latLngs.add(latlng);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Cisterna")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );
        Log.d("was","cisterna: "+latlng);
        localizacao=manancial.getPosition().split(",");
        latlng=new LatLng(Double.parseDouble(localizacao[0]),Double.parseDouble(localizacao[1]));
        latLngs.add(latlng);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Manancial")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        Log.d("was","Manancial: "+latlng);
    }


}
