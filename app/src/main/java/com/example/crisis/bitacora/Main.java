package com.example.crisis.bitacora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAsistencia();
        btnVehiculos();
    }

    public void btnAsistencia() {

        Button entry = (Button) findViewById(R.id.btnAsistencia);

        entry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent actAsistencia = new Intent(getApplicationContext(), Asistencia.class);
                startActivity(actAsistencia);
            }
        });
    }

    public void btnVehiculos() {

        Button entry = (Button) findViewById(R.id.btnVehiculo);

        entry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent actVehiculo= new Intent(getApplicationContext(), Vehiculos.class);
                startActivity(actVehiculo);
            }
        });
    }
}
