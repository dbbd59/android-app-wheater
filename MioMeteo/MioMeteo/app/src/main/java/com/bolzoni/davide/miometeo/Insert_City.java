package com.bolzoni.davide.miometeo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Insert_City extends AppCompatActivity {
    ListView lv;
    private ListView listView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__city);
        listView = (ListView) findViewById(R.id.listView2);
        editText = (EditText) findViewById(R.id.città_inserita);



        Intent intent = getIntent();
        try {
            if (intent.getStringExtra("flag").equals("a")) {
                finish();
            }
            if (intent.getStringExtra("flag").equals("f")) {
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.putExtra("flag", "s");
                intent1.putExtra("città", intent.getStringExtra("Gps"));
                startActivity(intent1);
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        try {
            DataBase db = new DataBase(this);
            db.open();
            db.close();
            if (intent.getStringExtra("flag").equals("b")) {
                Cancellarecord(intent.getStringExtra("città"));
            }
        } catch (Exception e) {
        }

        String[] dati = LeggiDati();
        lv = (ListView) findViewById(R.id.listView2);
        lv.setAdapter(new AdapterDB(this, dati));
        for (int i = 0; i < dati.length; i++) {
            Log.d("DATI", dati[i]);
        }

    }

    public void InviaMessaggio(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        String message = editText.getText().toString();
        intent.putExtra("città", message);
        intent.putExtra("flag", "noGps");

        startActivity(intent);
    }

    public void InviaMessaggioGps(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("flag", "siGps");

        startActivity(intent);
    }

    private String[] LeggiDati() {
        ArrayList<String> lstD = new ArrayList<String>();

        try {
            DataBase db = new DataBase(this);
            db.open();

            String strQuery = "SELECT distinct nome FROM tabella1;";
            Cursor c = db.Execute(strQuery, DataBase.TipoQuery.Selezione);

            if (c != null) {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    lstD.add(c.getString(c.getColumnIndex("nome")));
                }
            }

        } catch (Exception e) {
        }
        String[] citta = lstD.toArray(new String[lstD.size()]);
        return citta;
    }

    private void Cancellarecord(String citta) {
        try {
            DataBase db = new DataBase(this);
            db.open();

            String strQuery = "DELETE FROM tabella1 WHERE (nome)=";
            strQuery += "'" + citta + "';";
            Log.d("query", strQuery);

            db.Execute(strQuery, DataBase.TipoQuery.Comando);
            db.close();
        } catch (Exception e) {
        }
    }

}


