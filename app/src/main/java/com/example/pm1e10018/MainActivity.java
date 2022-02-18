package com.example.pm1e10018;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e10018.transacciones.Transacciones;

public class MainActivity extends AppCompatActivity {

    EditText nombre, telefono, nota;
    Spinner pais;

    private Spinner spinner;
    private String[] arraycontenido;
    private AdaptadorSpinner adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.sppaices);
        arraycontenido = new String[]{"Honduras(504)", "Costa Rica(506)", "Guatemala(502)", "El Salvador(503)"};
        adapter = new AdaptadorSpinner(this, arraycontenido);
        spinner.setAdapter(adapter);

        Button btnsalvar = (Button)findViewById(R.id.btnsalvar);
        Button btncontactos = (Button)findViewById(R.id.btncontactos);
        pais = (Spinner)findViewById(R.id.sppaices);
        nombre = (EditText)findViewById(R.id.txtnombre);
        telefono = (EditText)findViewById(R.id.txttelefono);
        nota = (EditText)findViewById(R.id.txtnota);

        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarUsuario();
            }
        });

        btncontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);
            }
        });
    }

    private void AgregarUsuario() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores =  new ContentValues();
        valores.put(Transacciones.pais, pais.getSelectedItem().toString());
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());

        if (validar() == true){
            Long resultado = db.insert(Transacciones.tablausuarios, Transacciones.nombre, valores);
            Toast.makeText(getApplicationContext(), "Registro Ingresado Correctamente: " + resultado.toString(), Toast.LENGTH_LONG).show();
            db.close();
            ClearScreen();
        }

    }

    public boolean validar(){
        boolean retorno= true;

        String nom= nombre.getText().toString();
        String tel= telefono.getText().toString();
        String nt= nota.getText().toString();

        if(nom.isEmpty()){
            nombre.setError("DEBE INGRESAR EL NOMBRE");
            retorno = false;
        }
        if(tel.isEmpty()){
            telefono.setError("DEBE INGRESAR EL NUMERO TELEFONICO");
            retorno = false;
        }
        if(nt.isEmpty()){
            nota.setError("DEBE INGRESAR UNA NOTA DE RECORDATORIO");
            retorno = false;
        }

        return retorno;
    }

    private void ClearScreen() {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }

}