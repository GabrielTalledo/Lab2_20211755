package com.example.lab2_20211755;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Objects;

public class EstadisticasActivity extends AppCompatActivity {

    public Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Animación:
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setEnterTransition(new Slide(Gravity.TOP).setDuration(700));
        getWindow().setReturnTransition(new Slide(Gravity.BOTTOM).setDuration(700));

        //Vista:
        setContentView(R.layout.activity_estadisticas);

        // Cambiar el titulo del AppBar:
        getSupportActionBar().setTitle("TeleGame");

        // Controlar el intent del Juego
        Intent intentJuego = getIntent();
        data = intentJuego.getExtras();
        String nombre = intentJuego.getStringExtra("nombre");
        String estadisticas = intentJuego.getStringExtra("estadisticas");
        TextView textNombre = findViewById(R.id.text_jugador);
        textNombre.setText("Jugador: "+nombre);
        TextView textEstadisticas = findViewById(R.id.text_estadisticas);
        if(estadisticas.isEmpty()){
            textEstadisticas.setText("Aún no hay estadísticas :(");
        }else{
            textEstadisticas.setText(estadisticas);
        }

        Button botonJuegoNuevo = findViewById(R.id.button_nuevo_juego2);
        botonJuegoNuevo.setOnClickListener(v -> {
            YoYo.with(Techniques.Hinge)
                    .duration(2000)
                    .playOn(botonJuegoNuevo);
            Intent resultIntent = new Intent();
            resultIntent.putExtras(data);
            resultIntent.putExtra("NuevoJuego","Este texto no importa XD");
            setResult(RESULT_OK, resultIntent);
            supportFinishAfterTransition();
        });
    }

    // Gestionamos el back button:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            // GoodBye xD:
            Intent resultIntent = new Intent();
            resultIntent.putExtras(data);
            setResult(RESULT_OK, resultIntent);
            supportFinishAfterTransition();
            return true;
        }else{
            return false;
        }

    }

}