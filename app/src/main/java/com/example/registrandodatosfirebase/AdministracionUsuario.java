package com.example.registrandodatosfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdministracionUsuario extends AppCompatActivity {
    // Realizamos una referencia a la base de datos
    DatabaseReference mRootReference;
    // Realizamos la referencia al Storage
    private StorageReference mStorage;
    private static final int GALLERY_INTENT =1;//creamos un codigo
    //:::::::::::::::::::::::::::::: Creacion de Variables para mis componenetes :::::::::::::::::::
    private EditText mEditTextArticulo,mEditTextDescripcion,mEditTextPrecio;
    private Button mGuardarDatos,SubirImagen;
    private ImageView FotoArticulo;
    public String Articulo="",Descripcion="",Precio="",FotoImagen="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion_usuario);
        //Referenciando mis componentes
        mEditTextArticulo       =   (EditText)findViewById(R.id.editArticulo);
        mEditTextDescripcion    =   (EditText)findViewById(R.id.editDescripcion);
        mEditTextPrecio         =   (EditText)findViewById(R.id.editPrecio);
        mGuardarDatos           =   (Button)findViewById(R.id.guardarcambiosbtn);
        SubirImagen             =   (Button)findViewById(R.id.subirfotobtn);
        FotoArticulo            =   (ImageView) findViewById(R.id.ArticuloImge);
        //Esto es para referenciar a la base de datos FIREBASE
        mRootReference  = FirebaseDatabase.getInstance().getReference();
        //Metodo para acceder a la galeria de mi cel :::::::..........
        referencenciaStoreFirebase();
        //Al darle click al boton guardar mandaria todo esto al metodo
        mGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Articulo     =   mEditTextArticulo.getText().toString();
                Descripcion  =   mEditTextDescripcion.getText().toString();
                Precio       =   mEditTextPrecio.getText().toString();
                //Guardo todo en FIREBASE ::::::::::::::
                SubiendoDatos(Articulo, Descripcion, Precio,FotoImagen);
            }
        });
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    }
    //:::::::::::::::::::::::::: Metodo para entrar a la galeria :::::::::::::::::::::::
    private void referencenciaStoreFirebase() {
        //Referenciamos el Store de firebase
        mStorage    = FirebaseStorage.getInstance().getReference();
        SubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//Para que agarre todos los formatos
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }
    //:::::::::::::::::::::::::: Metodo para subir la informacion a la BD :::::::::::::::::::::::::
    private void SubiendoDatos(String Articulo, String Descripcion, String Precio, String Foto) {
        //Creando ID randon
        Random rand = new Random();
        int n = rand.nextInt(1000000000); // Gives n such that 0 <= n < 1,000,000,000
        //Para cargar estos valores
        Map<String, Object> datosUsuario   =   new HashMap<>();
        datosUsuario.put("Articulos",Articulo);
        datosUsuario.put("Descripcion",Descripcion);
        datosUsuario.put("Precio",Precio);
        datosUsuario.put("Imagen",Foto);
        datosUsuario.put("ID",n);
        mRootReference.child("Articulos").push().setValue(datosUsuario);
    }
    //::::::::::::::::::::::: Metodo para Subir la imagen ::::::::::::::::::::::::::::::::::::
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==GALLERY_INTENT && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            //Aqui creamos la carpeta dentro del storage
            StorageReference destinofoto = mStorage.child("Articulos").child(uri.getLastPathSegment());
            destinofoto.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    destinofoto.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri downloadUrl = uri;

                                Glide.with(AdministracionUsuario.this)
                                        .load(downloadUrl).fitCenter().centerCrop().into(FotoArticulo);
                            FotoImagen  =   downloadUrl.toString();
                            /*Glide.with(subir_imagen.this)
                                    .load(downloadUrl).fitCenter().centerCrop().into(botonimagen);*/
                            Toast.makeText(AdministracionUsuario.this,"Se subio la foto ",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }

    public void Cambiando(View view) {
        Intent intent = new Intent(this,Sacandoarticulos.class);
        startActivity(intent);
    }
}