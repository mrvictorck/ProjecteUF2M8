package com.example.projecteuf2m8;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Coordenadas de ejemplo para una emisora de radio en Barcelona
    private static final double RADIO_STATION_LAT = 41.3851;
    private static final double RADIO_STATION_LNG = 2.1734;
    private static final String STATION_NAME = "Ràdio Barcelona Music";
    private static final String STATION_DESCRIPTION = "Emisora especializada en música independiente y alternativa. Transmitiendo desde el corazón de Barcelona las 24 horas del día.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Configurar la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ubicación de la Emisora");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Obtener el SupportMapFragment y configurar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Coordenadas de la emisora de radio
        LatLng radioStationLocation = new LatLng(RADIO_STATION_LAT, RADIO_STATION_LNG);

        // Crear marcador personalizado
        MarkerOptions markerOptions = new MarkerOptions()
                .position(radioStationLocation)
                .title(STATION_NAME)
                .snippet(STATION_DESCRIPTION)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        // Si tienes un icono personalizado, descomenta esta línea:
        // .icon(BitmapDescriptorFactory.fromResource(R.drawable.radio_icon));

        // Añadir el marcador al mapa
        mMap.addMarker(markerOptions);

        // Mover la cámara a la ubicación de la emisora
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(radioStationLocation, 15));

        // Configurar el tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Habilitar controles de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Configurar el info window (ventana de información del marcador)
        mMap.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return true;
        });

        // Personalizar la ventana de información
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public android.view.View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
                return null; // Usar el diseño por defecto
            }

            @Override
            public android.view.View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
                // Aquí podrías personalizar el contenido si quisieras
                return null; // Usar el contenido por defecto
            }
        });
    }
}