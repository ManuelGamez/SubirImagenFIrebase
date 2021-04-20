package com.example.registrandodatosfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Realizamos una referencia a la base de datos
    DatabaseReference mRootReference;
    Button mSubirDatos;
    EditText mEditTextNombre,mEditTextApellido,mEditTextTelefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSubirDatos         =   (Button)findViewById(R.id.enviarBTN);
        mEditTextNombre     =   (EditText)findViewById(R.id.etNombre);
        mEditTextApellido   =   (EditText)findViewById(R.id.etApellido);
        mEditTextTelefono   =   (EditText)findViewById(R.id.etTelefono);
        //Esto es para referenciar al nodo principal
        mRootReference  = FirebaseDatabase.getInstance().getReference();

        mRootReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Log.e("Datos: ",""+snapshot1.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mSubirDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre       =   mEditTextNombre.getText().toString();
                String apellido     =   mEditTextApellido.getText().toString();
                String telefono     =   mEditTextTelefono.getText().toString();
                SubiendoDatos(nombre, apellido, telefono);

            }
        });
    }

    private void SubiendoDatos(String nombre, String apellido, String telefono) {
        //Para cargar estos valores
        Map<String, Object> datosUsuario   =   new HashMap<>();
        datosUsuario.put("Nombre",nombre);
        datosUsuario.put("Apellido",apellido);
        datosUsuario.put("Telefono",telefono);
        mRootReference.child("Usuarios").push().setValue(datosUsuario);
    }
}