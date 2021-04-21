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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ExtrayendoDatosUsuario extends AppCompatActivity {
    // Realizamos una referencia a la base de datos
    DatabaseReference mRootReference;
    private ImageView FotoArticulo2;
    private TextView Articulo,Descripcions,precio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrayendo_datos_usuario);
        FotoArticulo2   =   (ImageView)findViewById(R.id.ImagenGALERIA);
        Articulo        =   (TextView)findViewById(R.id.TxtArticulo);
        Descripcions     =   (TextView)findViewById(R.id.textView2);
        precio          =   (TextView)findViewById(R.id.textView3);
        /*Bundle : Los paquetes se utilizan generalmente para pasar datos entre varias actividades de Android*/
        Bundle bundle = getIntent().getExtras();//creando un objeto bundle para traer los datos de la actividad mendiante Intent
        //Esto es para referenciar al nodo principal
        mRootReference  = FirebaseDatabase.getInstance().getReference();
        /*Query ref = FirebaseDatabase.getInstance().getReference().
                child("Articulos").orderByChild("ID").equalTo(bundle.getInt("ID"));*/

        mRootReference.child("Articulos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 :snapshot.getChildren())
                {
                    pojo articulos  =   snapshot1.getValue(pojo.class);
                    if(bundle.getInt("ID") == articulos.getID() )
                    {
                        String ArticuloNombre   =   articulos.getArticulos();
                        String Imagen           =   articulos.getImagen();
                        String Descripcion      =   articulos.getDescripcion();
                        String Precio           =   articulos.getPrecio();
                        Articulo.setText(ArticuloNombre);
                        Descripcions.setText(Descripcion);
                        precio.setText(Precio);
                        Glide.with(ExtrayendoDatosUsuario.this)
                                .load(Imagen).fitCenter().centerCrop().into(FotoArticulo2);
                        Log.e("Datos",""+snapshot1.getValue());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}