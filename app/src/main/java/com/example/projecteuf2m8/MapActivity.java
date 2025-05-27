package com.example.projecteuf2m8;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
    private static final String STATION_NAME = "🎵 Ràdio Barcelona Music";
    private static final String STATION_DESCRIPTION = "📻 Emisora especializada en música independiente y alternativa\n🎶 Transmitiendo desde el corazón de Barcelona\n⏰ Las 24 horas del día\n📍 Plaça de Catalunya, Barcelona";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si Google Play Services está disponible
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(resultCode)) {
                availability.getErrorDialog(this, resultCode, 1).show();
            } else {
                Toast.makeText(this, "Google Play Services no está disponible", Toast.LENGTH_LONG).show();
                finish();
            }
            return;
        }

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
        } else {
            Toast.makeText(this, "Error al cargar el fragmento del mapa", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            if (mMap == null) {
                Toast.makeText(this, "Error: Mapa no disponible", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // Configurar el mapa ANTES de añadir marcadores
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false); // Desactivar para evitar problemas de permisos

            // Coordenadas de la emisora de radio
            LatLng radioStationLocation = new LatLng(RADIO_STATION_LAT, RADIO_STATION_LNG);

            // Crear marcador personalizado (simplificado)
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(radioStationLocation)
                    .title(STATION_NAME)
                    .snippet(STATION_DESCRIPTION)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

            // Añadir el marcador al mapa
            mMap.addMarker(markerOptions);

            // Mover la cámara (con animación más suave)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(radioStationLocation, 15));

            // Listener para clicks en marcadores
            mMap.setOnMarkerClickListener(marker -> {
                marker.showInfoWindow();
                return true;
            });

            Toast.makeText(this, "Mapa cargado correctamente", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error al configurar el mapa: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}