package com.example.registrandodatosfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sacandoarticulos extends AppCompatActivity {
    private LinearLayout layout,contenedorBoton;
    private ImageButton botonimagen;
    int i=0;
    // Realizamos una referencia a la base de datos
    DatabaseReference mRootReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacandoarticulos);
        layout      =   (LinearLayout)findViewById(R.id.llBotonera2);
        mRootReference  = FirebaseDatabase.getInstance().getReference();
        mRootReference.child("Articulos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 :snapshot.getChildren())
                {
                    pojo articulos  =   snapshot1.getValue(pojo.class);
                    int ID                  =   articulos.getID();
                    String ArticuloNombre   =   articulos.getArticulos();
                    String Imagen           =   articulos.getImagen();
                    //Creando Botones Dinamicos :::::::::::::::::::::::::::
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(234, 223);
                        botonimagen = new ImageButton(Sacandoarticulos.this);
                        //Asignamos propiedades de layout al boton
                        botonimagen.setLayoutParams(lp);
                        botonimagen.setMaxWidth(234);
                        botonimagen.setMaxHeight(223);
                        botonimagen.setId(ID);
                        botonimagen.setOnClickListener(misEventos);
                        //Asignamos Texto al botón
                        //boton.setText("Boton "+String.format("%02d", i ));
                        //Añadimos el botón a la botonera

                        layout.addView(botonimagen);
                        Glide.with(Sacandoarticulos.this)
                                .load(Imagen).fitCenter().centerCrop().into(botonimagen);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public View.OnClickListener misEventos = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Bundle bundle = new Bundle();
            bundle.putInt("ID",view.getId());
            Intent intent = new Intent(Sacandoarticulos.this, ExtrayendoDatosUsuario.class);
            intent.putExtras(bundle);
            startActivity(intent);
            //Toast.makeText(Sacandoarticulos.this,"Hola Mundo"+ view.getId(),Toast.LENGTH_SHORT).show();
        }
    };


}