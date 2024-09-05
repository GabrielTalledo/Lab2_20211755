package com.example.lab2_20211755;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class JuegoAhorcado extends AppCompatActivity {

    // Variables a utilizar:

    public String nombre;




    // Funciones:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        // Activar botón para atrás:
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Cambiar el titulo del AppBar:
        getSupportActionBar().setTitle("TeleGame");

        // Intent:
        Intent intentPrincipal = getIntent();
        nombre = intentPrincipal.getStringExtra("Nombre");


    }

    // Lógica del juego de Ahorcado:
    public void empezarJuego(){

    }

    // Se crea el App Bar:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_juego,menu);
        return true;
    }

    // Lógica del Menú y PopUp de estadísticas:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.estadisticas) {
            View menuItemView = findViewById(id);
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_estadisticas, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId() == R.id.go_estadisticas) {

                        return true;
                    }
                    return false;
                }
            });

            popupMenu.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}