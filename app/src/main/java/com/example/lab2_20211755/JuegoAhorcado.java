package com.example.lab2_20211755;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import com.example.lab2_20211755.R;

import org.w3c.dom.Text;

public class JuegoAhorcado extends AppCompatActivity {

    // Variables a utilizar:

    public String nombre = "";
    public String estadisticas = "";
    public final String[] palabras = {"FIBRA","REDES","ANTENA","PROPA","CLOUD","TELECO"};
    public String palabraElegida;
    public int numIntento;
    public int numJuego = 0;
    public long tiempoInicio;
    public int numCaracteres;
    public int numCaracteresEncontrados;
    public int tiempoJuego;
    public boolean juegoTerminado = false;

    // Stickman:
    ImageView[] partesStickman = new ImageView[6];

    // Funciones:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        // Activar botón para atrás:
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Cambiar el titulo del AppBar:
        getSupportActionBar().setTitle("TeleGame");

        // Control del Intent de la pantalla principal:
        Intent intentPrincipal = getIntent();
        nombre = intentPrincipal.getStringExtra("Nombre");

        // Activar botón de nuevo juego:
        findViewById(R.id.button_nuevo_juego).setOnClickListener(l -> {
            // Registramos a estadísticas:
            if(!juegoTerminado){
                estadisticas = estadisticas + (numJuego==1?"":"\n") + "Juego " + numJuego + ": Canceló";
            }
            empezarJuego();
        });

        // Bienvenida :D
        Toast.makeText(this,"Bienvenido a TeleGame, " + nombre+"!", Toast.LENGTH_SHORT).show();

        empezarJuego();

    }

    // Inicio del Juego del Ahorcado:
    public void empezarJuego(){

        // Número de juego
        numJuego++;

        // Elección de la palabra:
        palabraElegida = palabras[new Random().nextInt(palabras.length)];
        Log.d("LOGXD","Palabra elegida: " + palabraElegida);
        // Creación de los campos de cada letra:
        numCaracteres = palabraElegida.length();
        LinearLayout layoutPalabra = findViewById(R.id.layout_palabra);
        layoutPalabra.removeAllViews();

        for(int i = 0;i<numCaracteres;i++){
            TextView letra = new TextView(this); // Creamos el TextView
            // Le asignamos un ID
            letra.setId(View.generateViewId());
            // Parámetros del layout:
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 0;
            letra.setLayoutParams(params);
            // Modificamos sus atributos:
            letra.setCompoundDrawablePadding(10);
            letra.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.linea_letra);
            letra.setPadding(10, 0, 10, 0); // Padding horizontal
            letra.setText(String.valueOf(palabraElegida.charAt(i)));;
            letra.setGravity(Gravity.CENTER);
            letra.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            letra.setTypeface(null, Typeface.BOLD);
            letra.setTextColor(Color.TRANSPARENT);
            // Finalmente, añadimos el Textview al layout:
            layoutPalabra.addView(letra);
        }


        // Seteo del texto:
        TextView mensajeJuego = findViewById(R.id.text_mensaje_juego);
        mensajeJuego.setText("Empiece a jugar!");


        // Seteo del stickman:

        partesStickman[0] = findViewById(R.id.cabeza);
        partesStickman[1] = findViewById(R.id.torso);
        partesStickman[2] = findViewById(R.id.brazo_derecho);
        partesStickman[3]= findViewById(R.id.brazo_izquierdo);
        partesStickman[4] = findViewById(R.id.pierna_izquierda);
        partesStickman[5] = findViewById(R.id.pierna_derecha);

        for (ImageView imageView : partesStickman) {
            imageView.setVisibility(View.INVISIBLE);
        }

        // Seteo de botones:
        for (int i = 0; i < 26; i++) {
            int resourceId = getResources().getIdentifier("letra_" + (char)('A' + i), "id", getPackageName());
            Button button = findViewById(resourceId);
            if(button != null){ // Por la ñ xd
                button.setEnabled(true);
            }
        }

        // Inicio de tiempo:
        tiempoInicio = System.currentTimeMillis();

        // Número de intento (Máximo es 5):
        numIntento = 0;
        numCaracteresEncontrados = 0;
        juegoTerminado = false;

        Log.d("TAG", estadisticas);

    }

    // Lógica del juego del ahorcado (cuando se pulsa una letra):

    public void clickLetra(View view) {
        if(!juegoTerminado){
            Button btnLetra = (Button) view;
            btnLetra.setEnabled(false);
            String letra = String.valueOf(btnLetra.getText());
            boolean letraEncontrada = false;
            LinearLayout layoutPalabra = findViewById(R.id.layout_palabra);
            for(int i = 0;i<numCaracteres;i++){
                if(String.valueOf(palabraElegida.charAt(i)).equals(letra)){
                    ((TextView)layoutPalabra.getChildAt(i)).setTextColor(Color.BLACK);
                    letraEncontrada = true;
                    numCaracteresEncontrados++;
                }
            }
            if(!letraEncontrada){
                partesStickman[numIntento].setVisibility(View.VISIBLE);
                numIntento++;
                // Se pierde:
                if(numIntento == partesStickman.length){
                    TextView mensajeJuego = findViewById(R.id.text_mensaje_juego);
                    mensajeJuego.setText("Perdiste! La palabra era: " + palabraElegida);
                    // Registramos las estadísticas:
                    tiempoJuego = (int) Math.floor((double) (System.currentTimeMillis() - tiempoInicio) /1000);
                    estadisticas = estadisticas + (numJuego==1?"":"\n") + "Juego " + numJuego + ": Perdió en " + tiempoJuego + "s";
                    juegoTerminado = true;
                }else{
                    TextView mensajeJuego = findViewById(R.id.text_mensaje_juego);
                    mensajeJuego.setText("Incorrecto! Te quedan " + (6 - numIntento) + " intentos.");
                }
            }else{
                if(numCaracteresEncontrados == numCaracteres){
                    // Se gana:
                    tiempoJuego = (int) Math.floor((double) (System.currentTimeMillis() - tiempoInicio) /1000);
                    TextView mensajeJuego = findViewById(R.id.text_mensaje_juego);
                    mensajeJuego.setText("Ganaste/terminaste en "+tiempoJuego+"s.");
                    // Registramos las estadísticas:
                    estadisticas = estadisticas + (numJuego==1?"":"\n") + "Juego " + numJuego + ": Terminó en " + tiempoJuego + "s";
                    juegoTerminado = true;
                }else{
                    TextView mensajeJuego = findViewById(R.id.text_mensaje_juego);
                    mensajeJuego.setText("Correcto! Aun tienes " + (6 - numIntento) + " intentos.");
                }

            }
        }

    }

    // Se crea el App Bar:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_juego,menu);
        return true;
    }

    // Lógica del App Bar y PopUp de estadísticas:
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