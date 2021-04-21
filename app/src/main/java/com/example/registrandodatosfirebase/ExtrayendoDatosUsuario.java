package com.example.registrandodatosfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExtrayendoDatosUsuario extends AppCompatActivity {
    // Realizamos una referencia a la base de datos
    DatabaseReference mRootReference;
    private ImageView FotoArticulo2;
    private TextView Articulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrayendo_datos_usuario);
        FotoArticulo2   =   (ImageView)findViewById(R.id.ImagenGALERIA);
        Articulo        =   (TextView)findViewById(R.id.TxtArticulo);
        //Esto es para referenciar al nodo principal
        mRootReference  = FirebaseDatabase.getInstance().getReference();
        mRootReference.child("Articulos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 :snapshot.getChildren())
                {
                    pojo articulos  =   snapshot1.getValue(pojo.class);
                    String ArticuloNombre   =   articulos.getArticulos();
                    String Imagen           =   articulos.getImagen();
                    Articulo.setText(ArticuloNombre);
                    Glide.with(ExtrayendoDatosUsuario.this)
                            .load(Imagen).fitCenter().centerCrop().into(FotoArticulo2);
                    Log.e("Datos",""+snapshot1.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}