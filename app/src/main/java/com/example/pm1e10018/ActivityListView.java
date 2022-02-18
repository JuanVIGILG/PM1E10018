package com.example.pm1e10018;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e10018.tablas.Usuarios;
import com.example.pm1e10018.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityListView extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listausuarios;
    ArrayList<Usuarios> lista;
    ArrayList<String> ArregloUsuarios;
    EditText id,nombre, telefono,nota, buscar;
    Spinner pais;
    public String global = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Button btnatras = (Button)findViewById(R.id.btnatras);
        Button btneliminar = (Button)findViewById(R.id.btneliminar);
        Button btnactualizar = (Button)findViewById(R.id.btnactualizar);
        Button btnllamar = (Button)findViewById(R.id.btnllamar);

        conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        listausuarios = (ListView)findViewById(R.id.listausuarios);

        ObtenerListaUsuarios();

        buscar = (EditText)findViewById(R.id.txtbusqueda);
        listausuarios = (ListView)findViewById(R.id.listausuarios);

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloUsuarios);
        listausuarios.setAdapter(adp);

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adp.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        listausuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String informacion = "Nombre: " + lista.get(position).getNombre();
                global = informacion;
                Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_SHORT).show();
            }
        });

        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actualizar();
            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar();
            }
        });
    }

    private void ObtenerListaUsuarios() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        Usuarios listUsuarios = null;
        lista = new ArrayList<Usuarios>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablausuarios, null);
        while (cursor.moveToNext()){
            listUsuarios = new Usuarios();
            listUsuarios.setNombre(cursor.getString(2));
            listUsuarios.setTelefono(cursor.getInt(3));

            lista.add(listUsuarios);
        }
        cursor.close();
        fillList();
    }

    private void fillList() {
        ArregloUsuarios = new ArrayList<String>();
        for (int i = 0; i<lista.size(); i++){
            ArregloUsuarios.add(lista.get(i).getNombre() +" | "+
                    lista.get(i).getTelefono());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setTitle("Accion");
        myBuild.setMessage("Desea realizar la llamada?");
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ActivityLLamada.class);
                startActivity(intent);
            }
        });
        myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = myBuild.create();
        dialog.show();
    }

    private void Eliminar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String [] params = {id.getText().toString()};
        String wherecond = Transacciones.id + "=?";
        db.delete(Transacciones.tablausuarios, wherecond, params);
        Toast.makeText(getApplicationContext(), "Dato Eliminado", Toast.LENGTH_LONG).show();
        ClearScreen();
    }

    private void Actualizar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String [] params = {id.getText().toString()};

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.pais, pais.getSelectedItem().toString());
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());

        db.update(Transacciones.tablausuarios, valores, Transacciones.id + "=?", params);
        Toast.makeText(getApplicationContext(), "Dato Actualizado", Toast.LENGTH_LONG).show();
        ClearScreen();

    }

    private void ClearScreen() {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }
}