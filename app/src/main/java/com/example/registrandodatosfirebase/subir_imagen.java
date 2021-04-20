package com.example.registrandodatosfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class subir_imagen extends AppCompatActivity {
    private Button mButton, boton;
    private StorageReference mStorage;
    private ImageButton botonimagen,Imagen2;
    private LinearLayout layout,contenedorBoton;
    private static final int GALLERY_INTENT =1;//creamos un codigo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_imagen);
        layout      =   (LinearLayout)findViewById(R.id.llBotonera);
        mButton     =   (Button)findViewById(R.id.botonSubir);
        Imagen2     =   (ImageButton)findViewById(R.id.imageButton2);
        botonimagen =   (ImageButton)findViewById(R.id.imageButton2);
        referencenciaStoreFirebase();
    }

    private void referencenciaStoreFirebase() {
        //Referenciamos el Store de firebase
        mStorage    = FirebaseStorage.getInstance().getReference();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//Para que agarre todos los formatos
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==GALLERY_INTENT && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            //Aqui creamos la carpeta dentro del storage
            StorageReference destinofoto = mStorage.child("foto").child(uri.getLastPathSegment());
            destinofoto.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    destinofoto.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri downloadUrl = uri;
                            //Creamos las propiedades de layout que tendrán los botones.
                            //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(134, 123);
                            for (int i=0; i<6; i++){
                                botonimagen = new ImageButton(subir_imagen.this);
                                //Asignamos propiedades de layout al boton
                                botonimagen.setLayoutParams(lp);
                                botonimagen.setMaxWidth(134);
                                botonimagen.setMaxHeight(123);
                                botonimagen.setId(i);
                                botonimagen.setImageURI(downloadUrl);
                                botonimagen.setOnClickListener(misEventos);
                                //Asignamos Texto al botón
                                //boton.setText("Boton "+String.format("%02d", i ));
                                //Añadimos el botón a la botonera

                                layout.addView(botonimagen);
                                Glide.with(subir_imagen.this)
                                        .load(downloadUrl).fitCenter().centerCrop().into(botonimagen);
                            }
                            Glide.with(subir_imagen.this)
                                    .load(downloadUrl).fitCenter().centerCrop().into(Imagen2);
                            Toast.makeText(subir_imagen.this,"Se subio la foto man",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }
    public View.OnClickListener misEventos = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(subir_imagen.this,"Hola Mundo"+ view.getId(),Toast.LENGTH_SHORT).show();
        }
    };


}