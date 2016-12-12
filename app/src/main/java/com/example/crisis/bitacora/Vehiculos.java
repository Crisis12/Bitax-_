package com.example.crisis.bitacora;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Vehiculos extends AppCompatActivity {

    ProgressDialog progressDialog;
    private String END_POINT_URL = "http://10.0.0.86:1434/Bitacora/Gasolinera.php";
    private String END_POINT_URL1 = "http://10.0.0.86:1434/Bitacora/Vehiculos.php";

    private Spinner p3, p2;
    private List<String> listg =new ArrayList<String>();
    private List<String> listv =new ArrayList<String>();
    private ArrayAdapter<String> dataAdapter;
    private ArrayAdapter<String> dataAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.setCancelable(false);

        p3 = (Spinner) findViewById(R.id.spinnerGas);
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, listg);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        p3.setAdapter(dataAdapter);
        callRegisterSpinnerG();
        dataAdapter.notifyDataSetChanged();


        p2 = (Spinner) findViewById(R.id.spinnerVehiculo);
        dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, listv);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        p2.setAdapter(dataAdapter1);
        callRegisterSpinnerV();
        dataAdapter1.notifyDataSetChanged();
    }

    private void callRegisterSpinnerG()
    {
        progressDialog.show();

        final AsyncHttpClient client = new AsyncHttpClient();

        client.post(END_POINT_URL, new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();


                try {

                    JSONObject obj = new JSONObject(content);
                    final JSONArray jsonArray = obj.getJSONArray("users");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++)
                    {
                        JSONObject user = jsonArray.getJSONObject(i);
                        listg.add(user.getString("Usuario"));

                        dataAdapter.notifyDataSetChanged();


                    }
                }catch(JSONException e){
                    Toast.makeText(getApplicationContext(), "Error al enviar su solicitud, Verifique el Json" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();

                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "No se envio la solicitud", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Algo ocurrio al final", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error: El dispositivo tal vez no este conectado a la red o el server se encuentra sin funcionar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void callRegisterSpinnerV()
    {
        progressDialog.show();

        final AsyncHttpClient client = new AsyncHttpClient();

        client.post(END_POINT_URL1, new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();


                try {

                    JSONObject obj = new JSONObject(content);
                    final JSONArray jsonArray = obj.getJSONArray("users");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++)
                    {
                        JSONObject user = jsonArray.getJSONObject(i);
                        listv.add(user.getString("Usuario"));

                        dataAdapter1.notifyDataSetChanged();


                    }
                }catch(JSONException e){
                    Toast.makeText(getApplicationContext(), "Error al enviar su solicitud, Verifique el Json" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();

                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "No se envio la solicitud", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Algo ocurrio al final", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error: El dispositivo tal vez no este conectado a la red o el server se encuentra sin funcionar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
