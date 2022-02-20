package com.example.pm1e10018;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e10018.transacciones.Transacciones;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText nombre, telefono, nota;
    Spinner pais;
    ImageView imagen;
    Button btnTomarFoto, btnSubirFoto, btnsalvar, btncontactos;

    private Spinner spinner;
    private String[] arraycontenido;
    private AdaptadorSpinner adapter;

    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.sppaices);
        arraycontenido = new String[]{"Honduras(504)", "Costa Rica(506)", "Guatemala(502)", "El Salvador(503)"};
        adapter = new AdaptadorSpinner(this, arraycontenido);
        spinner.setAdapter(adapter);

        btnTomarFoto = (Button) findViewById(R.id.btnTomarFoto);
        btnSubirFoto = (Button) findViewById(R.id.btnSubirFoto);
        btnsalvar = (Button)findViewById(R.id.btnEditar);
        btncontactos = (Button)findViewById(R.id.btncontactos);
        pais = (Spinner)findViewById(R.id.sppaices);
        nombre = (EditText)findViewById(R.id.txtnombre);
        telefono = (EditText)findViewById(R.id.txttelefono);
        nota = (EditText)findViewById(R.id.txtnota);
        imagen = (ImageView) findViewById(R.id.imagen);

        // -- BOTON PARA AGRESAR CONTACTO --
        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarUsuario();
            }
        });

        // -- BOTON PARA VER LA LISTA DE CONTACTOS --
        btncontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);
            }
        });

        // -- BOTON PARA TOMAR LA FOTOGRAFIA --
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

    }

    // ----- INICIO DE LOS METODOS -----

    // -- METODO PARA AGREGAR UN CONTACTO --
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

    // -- METODO PARA VALIDAR CAMPOS VACIOS Y MOSTRAR ALERTAS --
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

    // -- METODO PARA LIMPIAR PANTALLA --
    private void ClearScreen() {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }

    // -- METODO PARA DAR PERMISOS PARA USAR LA CAMARA --
    private void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    // -- METODO PARA LA RESPUESTA AL PERMISO A LA CAMARA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAM){
            tomarFoto();
        }else{
            Toast.makeText(getApplicationContext(),"Necesita permiso de acceso a la camara",Toast.LENGTH_LONG).show();
        }
    }

    // -- METODO PARA IR A CAPTURAR LA FOTOGRAFIA --
    private void tomarFoto(){
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takepic.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    // -- METODO PARA OBTENER LA FOTOGRAFIA Y MOSTRARLA EN LA APP --
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Byte[] arreglo;

        if (requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            imagen.setImageBitmap(img);
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PIC_REQUEST);
            }
        }
    }


}