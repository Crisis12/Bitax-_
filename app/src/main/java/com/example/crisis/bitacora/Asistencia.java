package com.example.crisis.bitacora;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Asistencia extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ProgressDialog progressDialog;
    private String END_POINT_URL = "http://192.168.0.6:1434/Bitacora/Usuarios.php";
    private String END_POINT_URL1 = "http://192.168.0.6:1434/Bitacora/Asistencia.php";
    private Spinner p1;
    private List<String> listu =new ArrayList<String>();
    private List<Integer> listt =new ArrayList<Integer>();
    private String[] listu1 = new String [100];
    private ArrayAdapter<String>dataAdapter;
    final Context context1 = this;
    private int opciones  ;
    private String macAddress;
    EditText Ob;

    private RadioButton E, S ;
    private RadioGroup RG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.setCancelable(false);

        Ob = (EditText) findViewById(R.id.etxtMotivos);

        p1 = (Spinner) findViewById(R.id.spinnerUsuario);

       RG = (RadioGroup) findViewById(R.id.RG);


        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, listu);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        p1.setAdapter(dataAdapter);





        btnAceptar();
        btnCancelar();

        WifiManager wifiMan = (WifiManager) this.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        macAddress = wifiInf.getMacAddress();

        RequestParams PMAC = new RequestParams();
        PMAC.put("PMAC", macAddress);


        callRegisterSpinnerU(PMAC);
        p1.setOnItemSelectedListener(this);
        p1.setEnabled(false);
        p1.setClickable(false);
        dataAdapter.notifyDataSetChanged();



    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbtnEntrada:
                if (checked)
                    opciones = 1;
                    break;
            case R.id.rbtnSalida:
                if (checked)
                    opciones = 0;
                    break;
        }
    }

    public void btnAceptar() {

        ImageView entry = (ImageView) findViewById(R.id.aceptar);

        entry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);

                // set title
                alertDialogBuilder.setTitle("Aviso");

                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Desea enviar su reporte?")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();

                                int Item = p1.getSelectedItemPosition();
                                Log.d("PROBANDO RPT:", listt.get(Item).toString());



                            }
                        })
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close


                                String Obser = Ob.getText().toString();
                               // String Item = p1.getSelectedItem().toString();

                                int Item = p1.getSelectedItemPosition();


                                RequestParams params = new RequestParams();
                                params.put("Radio", String.valueOf(opciones));
                                params.put("RPT", listt.get(Item).toString());
                                params.put("Observacion", Obser);
                                params.put("dispositivo",macAddress);
                                InsertarAsistencia(params);
                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



            }
        });
    }


    public void btnCancelar() {

        ImageView entry = (ImageView) findViewById(R.id.btncancelar);

        entry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);

                // set title
                alertDialogBuilder.setTitle("Aviso");

                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Desea enviar su reporte?")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Ob.setText("");
                                RG.clearCheck();


                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }


    @Override public void onItemSelected(AdapterView parent, View view, int position, long id) {

        p1.setSelection(position);

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void callRegisterSpinnerU(RequestParams PMAC)
    {
        progressDialog.show();

        final AsyncHttpClient client = new AsyncHttpClient();

        client.post(END_POINT_URL, PMAC ,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();


                try {

                    JSONObject obj = new JSONObject(content);
                    final JSONArray jsonArray = obj.getJSONArray("users");
                    final JSONArray jsonArray1 = obj.getJSONArray("RPTJ");

                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++)
                    {
                        JSONObject user = jsonArray.getJSONObject(i);
                        listu.add(user.getString("Usuario"));
                        dataAdapter.notifyDataSetChanged();

                        JSONObject user1 = jsonArray1.getJSONObject(i);
                        listt.add(user1.getInt("RPT"));
                        Log.d("Probando", listt.toString());


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

    private void InsertarAsistencia(RequestParams params) {
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(END_POINT_URL1, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();

                try {
                    JSONObject jsonResponse = new JSONObject(content);
                    String msg = "";
                    if(jsonResponse.getInt("status") == 1){
                        msg = jsonResponse.getString("msg");
                        finish();
                    }else{
                        msg = jsonResponse.getString("msg");
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
