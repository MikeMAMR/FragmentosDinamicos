package net.ivanvega.fragmentosdinamicos;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private AdaptadorLirbosFiltro adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Barar de acciones
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //BOTON FLOTANTE
        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Todos"));
        tabs.addTab(tabs.newTab().setText("Nuevos"));
        tabs.addTab(tabs.newTab().setText("Leidos"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: //Todos
                        //adaptador.setNovedad(false);
                        //adaptador.setLeido(false);
                        break;
                    case 1:
                        //adaptador.setNovedad(true);
                        //adaptador.setLeido(false);
                        break;
                    case 2:
                        //adaptador.setNovedad(false);
                        //adaptador.setLeido(true);
                        break;

                }
                //adaptador.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        if (findViewById(R.id.contenedor_pequeno) != null &&
                getSupportFragmentManager()
                        .findFragmentById(R.id.contenedor_pequeno) == null
        ) {
            getSupportFragmentManager().beginTransaction().
                    setReorderingAllowed(true)
                    .add(
                            R.id.contenedor_pequeno,
                            SelectorFragment.class, null).commit();
        }


    }



    public void mostrarDetalle(int pos) {
        DetalleFragment detalleFragment =
                (DetalleFragment) getSupportFragmentManager().
                        findFragmentById(R.id.detalle_fragment);
        if (detalleFragment != null) {
            detalleFragment.setInfoLibro(pos);
        } else {

            detalleFragment =
                    new DetalleFragment();
            Bundle bundle = new Bundle();

            bundle.putInt(DetalleFragment.ARG_INDEX_LIBRO,
                    pos
            );

            detalleFragment.setArguments(
                    bundle
            );

            getSupportFragmentManager().beginTransaction().
                    setReorderingAllowed(true)
                    .replace(R.id.contenedor_pequeno, detalleFragment)
                    .addToBackStack(null)
                    .commit();
        }

        //PAGE 57
       SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ultimo", pos);
        editor.commit();

    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.menu_preferencias==item.getItemId()){
            Toast.makeText(this, "Preferencias", Toast.LENGTH_LONG).show();
            return true;
        }/*else if(R.id.menu_ultimo==item.getItemId()){
            irUltimoVisitado();
            return true;
        }else if(R.id.menu_buscar2==item.getItemId()){
            return true;
        }*/else if(R.id.menu_acerca==item.getItemId()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mensaje de Acerca de");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void irUltimoVisitado(){
        SharedPreferences pref = getSharedPreferences(
                "com.example.audiolibros_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if(id >= 0){
            mostrarDetalle(id);
        }else{
            Toast.makeText(this, "Sin última vista", Toast.LENGTH_LONG).show();
        }
    }
}