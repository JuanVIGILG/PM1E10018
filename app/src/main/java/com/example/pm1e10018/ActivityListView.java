package com.example.pm1e10018;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1e10018.tablas.Contactos;
import com.example.pm1e10018.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityListView extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listausuarios;
    ArrayList<Contactos> lista;
    ArrayList<String> ArregloUsuarios;
    EditText id,nombre, telefono,nota, buscar;
    public String Pais,Nombre,Nota;
    public int ID,Telefono;
    Spinner pais;
    public String global = " ";
    public Boolean SelectedRow = false;
    private Boolean isFirstTime = true;
    Button btnactualizar, btncompartir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Button btnatras = (Button)findViewById(R.id.btnatras);
        Button btneliminar = (Button)findViewById(R.id.btneliminar);
        btnactualizar = (Button)findViewById(R.id.btnactualizar);
        Button btnllamar = (Button)findViewById(R.id.btnllamar);
        btncompartir = (Button) findViewById(R.id.btncompartir);

        conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        listausuarios = (ListView)findViewById(R.id.listausuarios);

        ObtenerListaUsuarios();

        buscar = (EditText)findViewById(R.id.txtbusqueda);
        listausuarios = (ListView)findViewById(R.id.listausuarios);

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloUsuarios);
        listausuarios.setAdapter(adp);

        /*listausuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

                obtenerobjeto(i);
            }
        });*/

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

        listausuarios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listausuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ID = lista.get(position).getId();
                Nombre = lista.get(position).getNombre();
                Telefono = lista.get(position).getTelefono();
                Nota = lista.get(position).getNota();

                //System.out.println("Choosen Country = : " + ID);
                System.out.println("Choosen Country = : " + Nombre);
                System.out.println("Choosen Country = : " + Telefono);
                System.out.println("Choosen Country = : " + Nota);
                /*view.setSelected(true);

                if (isFirstTime){
                    isFirstTime = true;
                }
                if (SelectedRow == true){
                    ID = lista.get(position).toString();
                    Nombre = lista.get(position).getNombre();
                    Telefono = lista.get(position).getTelefono();
                    Nota = lista.get(position).getNota();

                    System.out.println("Choosen Country = : " + ID);
                    System.out.println("Choosen Country = : " + Nombre);
                    System.out.println("Choosen Country = : " + Telefono);
                    System.out.println("Choosen Country = : " + Nota);

                }*/

            }
        });



        /*listausuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Choosen Country = : " + ArregloUsuarios);
            }});*/

        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Compartir();
            }
        });

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Nombre.isEmpty()){
                    Toast.makeText(ActivityListView.this, "Seleccione un registro", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ActivityActualizar.class);
                    intent.putExtra("nombre", Nombre);
                    intent.putExtra("telefono", Telefono);
                    intent.putExtra("nota", Nota);
                    //intent.putExtra("Dato4",correo);
                    startActivity(intent);

                    System.out.println("Choosen Country = : " + Nombre);
                }

                /*if(Nombre.isEmpty()){
                    Toast.makeText(ActivityListView.this, "Seleccione un registro", Toast.LENGTH_SHORT).show();
                }else{

                    Intent intent = new Intent(getApplicationContext(), ActivityActualizar.class);
                    //intent.putExtra("id", ID);
                    //intent.putExtra("pais", Pais);
                    intent.putExtra("nombre", Nombre);
                    intent.putExtra("telefono", Telefono);
                    intent.putExtra("nota", Nota);
                    startActivity(intent);
                }*/
                //view.setSelected(true);

                /*if (isFirstTime){
                    isFirstTime = true;
                }
                if (SelectedRow==true) {
                    Intent intent = new Intent(getApplicationContext(), ActivityActualizar.class);
                    intent.putExtra("id", ID);
                    //intent.putExtra("pais", Pais);
                    intent.putExtra("nombre", Nombre);
                    intent.putExtra("telefono", Telefono);
                    intent.putExtra("nota", Nota);
                    startActivity(intent);
                    // finish();
                }
                else {
                    Toast.makeText(ActivityListView.this, "Seleccione un registro", Toast.LENGTH_SHORT).show();
                }*/
            }
        });



        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar();
            }
        });


    }

    public void Compartir(){
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,Nombre+" "+Telefono);
        Intent eleccion = Intent.createChooser(intent,"Compartir usando");
        startActivity(eleccion);
    }

    /*private void obtenerobjeto(int id) {
        Contactos cont = lista.get(id);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.putExtra("nombre",cont.getId());
        intent.putExtra("telefono",cont.getTelefono());
        intent.putExtra("nota",cont.getNota());

        startActivity(intent);

    }*/

    private void ObtenerListaUsuarios() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        Contactos listContactos = null;
        lista = new ArrayList<Contactos>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablausuarios, null);
        while (cursor.moveToNext()){
            listContactos = new Contactos();
            //listContactos.setId(cursor.getInt(1));
            listContactos.setNombre(cursor.getString(2));
            listContactos.setTelefono(cursor.getInt(3));
            listContactos.setNota(cursor.getString(4));
            lista.add(listContactos);
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


    private void ClearScreen() {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }
}