package com.example.pm1e10018;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1e10018.transacciones.Transacciones;

public class ActivityActualizar extends AppCompatActivity {

    String nombre, nota, Pais, pais;
    int id;
    int telefono;
    EditText nom, tel, note;
    Spinner spinner;
    Button btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        recibirDatos();

        /*Intent i = getIntent();
        //id  = getIntent().getExtras().getString("id");
        Pais  = getIntent().getExtras().getString("pais");
        nombre  = getIntent().getExtras().getString("nombre");
        telefono  = getIntent().getExtras().getInt("telefono");
        nota = getIntent().getExtras().getString("nota");

        spinner = (Spinner) findViewById(R.id.sppaices);
        nom = (EditText)findViewById(R.id.txtnombre);
        tel = (EditText)findViewById(R.id.txttelefono);
        note = (EditText)findViewById(R.id.txtnota);
        btnEditar = (Button) findViewById(R.id.btnEditar);


        nom.setText(nombre);
        tel.setText(telefono);
        note.setText(nota);*/


        //ObjImagen = (ImageView) findViewById(R.id.fotografia);
        //btnfoto = (Button) findViewById(R.id.btnfoto);

        /*btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                abrirCamara();
            }
        });*/

       /* btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
                Actualizar();

            }
        });*/

    }

    // ----- INICIO DE METODOS -----

    // ** P E N D I E N T E PAIS **
    // -- METODO PARA RECIBIR LOS DATOS DEL ActivityListView
    public void recibirDatos(){
        Bundle extras = getIntent().getExtras();

        //Pais  = extras.getString("pais");
        id  = extras.getInt("id");
        nombre  = extras.getString("nombre");
        telefono  = extras.getInt("telefono");
        nota = extras.getString("nota");

        nom = (EditText) findViewById(R.id.txtnombre);
        tel = (EditText) findViewById(R.id.txttelefono);
        note = (EditText) findViewById(R.id.txtnota);

        nom.setText(nombre);
        tel.setText(""+telefono);
        note.setText(nota);


    }

    // ** P E N D I E N T E PAIS**
    // -- METODO PARA ACTUALIZAR EL CONTACTO EN LA BASE DE DATOS
    private void Actualizar() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        String [] params = {String.valueOf(id)};

        ContentValues valores =  new ContentValues();
        //valores.put(Transacciones.pais, pais.getSelectedItem().toString());
        valores.put(Transacciones.nombre, nom.getText().toString());
        valores.put(Transacciones.telefono, tel.getText().toString());
        valores.put(Transacciones.nota, note.getText().toString());

        if (validar() == true){
            db.update(Transacciones.tablausuarios, valores, Transacciones.id + "=?", params);
            Toast.makeText(getApplicationContext(), "Dato Actualizado", Toast.LENGTH_LONG).show();
            db.close();
        }

    }

    // -- METODO PARA VALIDAR CAMPOS VACIOS Y MOSTRAR ALERTAS --
    public boolean validar(){
        boolean retorno= true;

        String name= nom.getText().toString();
        String cel= tel.getText().toString();
        String nt= note.getText().toString();

        if(name.isEmpty()){
            nom.setError("DEBE INGRESAR EL NOMBRE");
            retorno = false;
        }
        if(cel.isEmpty()){
            tel.setError("DEBE INGRESAR EL NUMERO TELEFONICO");
            retorno = false;
        }
        if(nt.isEmpty()){
            note.setError("DEBE INGRESAR UNA NOTA DE RECORDATORIO");
            retorno = false;
        }

        return retorno;
    }


}