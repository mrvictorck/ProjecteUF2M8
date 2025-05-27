package com.example.projecteuf2m8;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private MediaPlayer mediaPlayer;
    private GestureDetector gestureDetector;
    private TextView tvStatus;
    private View gestureArea;
    private Button btnPlay, btnPause, btnStop, btnMap;

    // Estados del reproductor
    private enum PlayerState {
        STOPPED, PLAYING, PAUSED
    }

    private PlayerState currentState = PlayerState.STOPPED;
    private int pausedPosition = 0; // Para guardar la posición cuando se pausa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupGestureDetector();
        setupMediaPlayer();
        setupClickListeners();
    }

    private void initializeViews() {
        tvStatus = findViewById(R.id.tvStatus);
        gestureArea = findViewById(R.id.gestureArea);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnMap = findViewById(R.id.btnMap);
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, this);
        gestureArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void setupMediaPlayer() {
        // Aquí deberías poner tu archivo de audio en la carpeta res/raw/
        // Por ejemplo: R.raw.sample_audio
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_audio);

        if (mediaPlayer == null) {
            // Si no tienes archivo de audio, crear uno silencioso para pruebas
            Toast.makeText(this, "Archivo de audio no encontrado. Agrega sample_audio.mp3 en res/raw/",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                currentState = PlayerState.STOPPED;
                pausedPosition = 0;
                updateStatus();
            }
        });
    }

    private void setupClickListeners() {
        btnPlay.setOnClickListener(v -> playAudio());
        btnPause.setOnClickListener(v -> pauseAudio());
        btnStop.setOnClickListener(v -> stopAudio());
        btnMap.setOnClickListener(v -> openMap());
    }

    private void playAudio() {
        if (mediaPlayer == null) return;

        try {
            switch (currentState) {
                case STOPPED:
                    mediaPlayer.start();
                    currentState = PlayerState.PLAYING;
                    break;
                case PAUSED:
                    mediaPlayer.seekTo(pausedPosition);
                    mediaPlayer.start();
                    currentState = PlayerState.PLAYING;
                    break;
                case PLAYING:
                    // Ya está reproduciendo
                    Toast.makeText(this, "Ya se está reproduciendo", Toast.LENGTH_SHORT).show();
                    break;
            }
            updateStatus();
        } catch (Exception e) {
            Toast.makeText(this, "Error al reproducir audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer == null || currentState != PlayerState.PLAYING) return;

        try {
            pausedPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            currentState = PlayerState.PAUSED;
            updateStatus();
        } catch (Exception e) {
            Toast.makeText(this, "Error al pausar audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAudio() {
        if (mediaPlayer == null) return;

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.prepare(); // Preparar para próxima reproducción
            }
            currentState = PlayerState.STOPPED;
            pausedPosition = 0;
            updateStatus();
        } catch (Exception e) {
            Toast.makeText(this, "Error al detener audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStatus() {
        String status;
        switch (currentState) {
            case PLAYING:
                status = "Estado: Reproduciendo ▶";
                break;
            case PAUSED:
                status = "Estado: Pausado ⏸";
                break;
            case STOPPED:
            default:
                status = "Estado: Detenido ⏹";
                break;
        }
        tvStatus.setText(status);
    }

    private void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    // Implementación de gestos
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Gesto: Toque simple = Pausa
        pauseAudio();
        Toast.makeText(this, "Gesto: Pausa", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) return false;

        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            // Deslizamiento horizontal
            if (diffX > 0) {
                // Deslizar hacia la derecha = Play
                playAudio();
                Toast.makeText(this, "Gesto: Play →", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Deslizamiento vertical
            if (diffY > 0) {
                // Deslizar hacia abajo = Stop
                stopAudio();
                Toast.makeText(this, "Gesto: Stop ↓", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}