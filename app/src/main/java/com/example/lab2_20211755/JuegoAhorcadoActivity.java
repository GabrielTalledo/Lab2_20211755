package com.example.lab2_20211755;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidanimations.library.Techniques;


public class JuegoAhorcadoActivity extends AppCompatActivity {

    // Variables a utilizar:

    public String nombre = "";
    public String estadisticas = "";
    public final String[] palabras = {"FIBRA","REDES","ANTENA","PROPA","CLOUD","TELECO","TRAFI","TELITO","GTICS","IWEB","SQL"};
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

        // Animación:
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setEnterTransition(new Explode().setDuration(1500));
        getWindow().setExitTransition(new Slide(Gravity.END).setDuration(700));

        // Vista:
        setContentView(R.layout.activity_juego);

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
        Button botonJuegoNuevo = findViewById(R.id.button_nuevo_juego);
        botonJuegoNuevo.setOnClickListener(l -> {
            // Registramos a estadísticas:
            if(!juegoTerminado){
                estadisticas = estadisticas + (numJuego==1?"":"\n") + "Juego " + numJuego + ": Canceló";
            }
            // Volvemos a empezar:
            YoYo.with(Techniques.Tada)
                    .duration(1000)
                    .playOn(botonJuegoNuevo);
            empezarJuego();
        });

        // Control del Intent de la pantalla principal:
        Intent intentPrincipal = getIntent();
        nombre = intentPrincipal.getStringExtra("Nombre");
        if(nombre != null){
            // Bienvenida :D
            Toast.makeText(this,"Bienvenido a TeleGame, " + nombre+"!", Toast.LENGTH_SHORT).show();
            empezarJuego();
        }

    }


    // Launcher y manejar el callback:
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent resultIntent = result.getData();
                    // Restaurar estado guardado:
                    nombre = resultIntent.getStringExtra("nombre");
                    estadisticas = resultIntent.getStringExtra("estadisticas");
                    palabraElegida = resultIntent.getStringExtra("palabraElegida");
                    numJuego = resultIntent.getIntExtra("numJuego",0);
                    numIntento = resultIntent.getIntExtra("numIntento",0);
                    numCaracteres = resultIntent.getIntExtra("numCaracteres",0);
                    numCaracteresEncontrados = resultIntent.getIntExtra("numCaracteresEncontrados",0);
                    tiempoInicio = resultIntent.getLongExtra("tiempoInicio",0);
                    juegoTerminado = resultIntent.getBooleanExtra("juegoTerminado",false);
                    letrasAcertadas = resultIntent.getStringArrayListExtra("letrasAcertadas");
                    letrasPresionadas = resultIntent.getStringArrayListExtra("letrasPresionadas");
                    ((TextView)findViewById(R.id.text_mensaje_juego)).setText(resultIntent.getStringExtra("mensajeJuego"));

                    if(resultIntent.getStringExtra("NuevoJuego") != null){
                        if(!juegoTerminado){
                            estadisticas = estadisticas + (numJuego==1?"":"\n") + "Juego " + numJuego + ": Canceló";
                        }
                        empezarJuego();
                    }else{
                        restaurarLetras(letrasAcertadas,palabraElegida);
                        desactivarBotones(letrasPresionadas);
                        restaurarStickman(numIntento,partesStickman);
                        restaurarLetras(letrasAcertadas,palabraElegida);
                    }
                }
            }
    );


    // Función auxiliar para desactivar botones:
    public void desactivarBotones(ArrayList<String> letrasPresionadas){
        for(String letra: letrasPresionadas){
            int resourceId = getResources().getIdentifier("letra_" + letra, "id", getPackageName());
            ((Button)findViewById(resourceId)).setEnabled(false);
        }
    }

    // Función auxiliad para restaurar las letras del juego:
    public void restaurarLetras(ArrayList<String> letrasAcertadas,String palabraElegida){
        LinearLayout layoutPalabra = findViewById(R.id.layout_palabra);
        layoutPalabra.removeAllViews();
        for(int i=0;i<palabraElegida.length();i++){
            String letraStr = String.valueOf(palabraElegida.charAt(i));
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
            if(i<=numIntento-1){
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
        getSupportActionBar().setTitle("TeleGame - Juego N° "+numJuego);

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
            // Pequeña animación:
            YoYo.with(Techniques.ZoomIn)
                    .duration(1000)
                    .playOn(btnLetra);
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
                    if(numIntento == 5){
                        mensajeJuego.setText("Incorrecto! Te queda solo 1 intento.");
                    }else{
                        mensajeJuego.setText("Incorrecto! Te quedan " + (6 - numIntento) + " intentos.");
                    }
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
                        intent.putExtra("palabraElegida",palabraElegida);
                        intent.putExtra("numJuego",numJuego);
                        intent.putExtra("numIntento",numIntento);
                        intent.putExtra("numCaracteres",numCaracteres);
                        intent.putExtra("numCaracteresEncontrados",numCaracteresEncontrados);
                        intent.putExtra("tiempoInicio",tiempoInicio);
                        intent.putExtra("juegoTerminado",juegoTerminado);
                        intent.putStringArrayListExtra("letrasAcertadas",letrasAcertadas);
                        intent.putStringArrayListExtra("letrasPresionadas",letrasPresionadas);
                        intent.putExtra("mensajeJuego",((TextView)findViewById(R.id.text_mensaje_juego)).getText().toString());
                        launcher.launch(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(JuegoAhorcadoActivity.this));
                        return true;
                    } else if (menuItem.getItemId() == R.id.go_juego_secreto) {
                        // Esto por motivos de práctica para saber más funciones del intent y los popups:
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=xMHJGd3wwZk"));
                        startActivity(intent);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            popupMenu.show();
            return true;
        }else {
            if(item.getItemId() == android.R.id.home){
                // GoodBye xD:
                supportFinishAfterTransition();
                return true;
            }else{
                return super.onOptionsItemSelected(item);
            }
        }
    }

}