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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class JuegoAhorcadoActivity extends AppCompatActivity {

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
    public ArrayList<String> letrasAcertadas = new ArrayList<>();
    public ArrayList<String> letrasPresionadas = new ArrayList<>();

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

        // Primer seteo del Stickman:
        partesStickman[0] = findViewById(R.id.cabeza);
        partesStickman[1] = findViewById(R.id.torso);
        partesStickman[2] = findViewById(R.id.brazo_derecho);
        partesStickman[3]= findViewById(R.id.brazo_izquierdo);
        partesStickman[4] = findViewById(R.id.pierna_izquierda);
        partesStickman[5] = findViewById(R.id.pierna_derecha);

        // Activar botón de nuevo juego:
        findViewById(R.id.button_nuevo_juego).setOnClickListener(l -> {
            // Registramos a estadísticas:
            if(!juegoTerminado){
                estadisticas = estadisticas + (numJuego==1?"":"\n") + "Juego " + numJuego + ": Canceló";
            }
            empezarJuego();
        });

        // Control del Intent de la pantalla principal:
        Intent intentPrincipal = getIntent();
        nombre = intentPrincipal.getStringExtra("Nombre");
        if(nombre != null){
            empezarJuego();
        }

    }

    // Obtener la data guardada:
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar estado guardado:
        nombre = savedInstanceState.getString("nombre");
        estadisticas = savedInstanceState.getString("estadisticas");
        palabraElegida = savedInstanceState.getString("palabraElegida");
        numJuego = savedInstanceState.getInt("numJuego");
        numIntento = savedInstanceState.getInt("numIntento");
        numCaracteres = savedInstanceState.getInt("numCaracteres");
        numCaracteresEncontrados = savedInstanceState.getInt("numCaracteresEncontrados");
        tiempoInicio = savedInstanceState.getLong("tiempoInicio");
        juegoTerminado = savedInstanceState.getBoolean("juegoTerminado");
        letrasAcertadas = savedInstanceState.getStringArrayList("letrasAcertadas");
        letrasPresionadas = savedInstanceState.getStringArrayList("letrasPresionadas");
        ((TextView)findViewById(R.id.text_mensaje_juego)).setText(savedInstanceState.getString("mensajeJuego"));

        restaurarLetras(letrasAcertadas,palabraElegida);
        desactivarBotones(letrasPresionadas);
        restaurarStickman(numIntento,partesStickman);
    }


    // Manejar el estado guardado:
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nombre",nombre);
        outState.putString("estadisticas",estadisticas);
        outState.putString("palabraElegida",palabraElegida);
        Log.d("TAG", "AIUDA WEEE");
        outState.putInt("numJuego",numJuego);
        outState.putInt("numIntento",numIntento);
        outState.putInt("numCaracteres",numCaracteres);
        outState.putInt("numCaracteresEncontrados",numCaracteresEncontrados);
        outState.putLong("tiempoInicio",tiempoInicio);
        outState.putBoolean("juegoTerminado",juegoTerminado);
        outState.putStringArrayList("letrasAcertadas",letrasAcertadas);
        outState.putStringArrayList("letrasPresionadas",letrasPresionadas);
        outState.putString("mensajeJuego",((TextView)findViewById(R.id.text_mensaje_juego)).getText().toString());
    }

    // Función auxiliar para desactivar botones:
    public void desactivarBotones(ArrayList<String> letrasPresionadas){
        for(String letra: letrasPresionadas){
            int resourceId = getResources().getIdentifier("letra_" + letra, "id", getPackageName());
            ((Button)findViewById(resourceId)).setEnabled(false);
        }
    }

    // Función auxiliad para restaurar las letras del juego:
    public void restaurarLetras(ArrayList<String> letrasAcertadas,String palabraElegida){
        for(int i=0;i<palabraElegida.length();i++){
            String letraStr = String.valueOf(palabraElegida.charAt(i));
            LinearLayout layoutPalabra = findViewById(R.id.layout_palabra);
            layoutPalabra.removeAllViews();
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
            letra.setText(letraStr);;
            letra.setGravity(Gravity.CENTER);
            letra.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            letra.setTypeface(null, Typeface.BOLD);
            if(letrasAcertadas.contains(letraStr)){
                letra.setTextColor(Color.BLACK);
            }else{
                letra.setTextColor(Color.TRANSPARENT);
            }
            // Finalmente, añadimos el Textview al layout:
            layoutPalabra.addView(letra);
        }
    }

    // Función auxiliar para restaurar al Stickman uwu:
    public void restaurarStickman(int numIntento, ImageView[] partesStickman){
        for (int i=0;i<partesStickman.length;i++) {
            if(i<=numIntento){
                partesStickman[i].setVisibility(View.VISIBLE);
            }else {
                partesStickman[i].setVisibility(View.INVISIBLE);
            }
        }
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
        for (ImageView imageView : partesStickman) {
            imageView.setVisibility(View.INVISIBLE);
        }

        //Seteo de letras presionadas y acertadas:
        letrasAcertadas.clear();
        letrasPresionadas.clear();

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
            // Se guarda la letra presionada:
            letrasPresionadas.add(letra);
            // Se verifica si la letra está en la palabra:
            boolean letraEncontrada = false;
            LinearLayout layoutPalabra = findViewById(R.id.layout_palabra);
            for(int i = 0;i<numCaracteres;i++){
                if(String.valueOf(palabraElegida.charAt(i)).equals(letra)){
                    ((TextView)layoutPalabra.getChildAt(i)).setTextColor(Color.BLACK);
                    letraEncontrada = true;
                    numCaracteresEncontrados++;
                    letrasAcertadas.add(letra);
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
                        Intent intent = new Intent(JuegoAhorcadoActivity.this, EstadisticasActivity.class);
                        intent.putExtra("nombre",nombre);
                        intent.putExtra("estadisticas",estadisticas);
                        startActivity(intent);
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