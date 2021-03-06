package com.cursoandroid.testefb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ListaLeitosActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayList<Leito> leitos = new ArrayList<Leito>();
    ListView listV_leitos;
    private String grupo;
    private String mudar;
    private String editar;
    private String excluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_leitos);
        Intent intent = getIntent();
        Setor setor = intent.getParcelableExtra("setor");
        grupo = intent.getStringExtra("grupo");
        mudar = intent.getStringExtra("mudar");
        editar = intent.getStringExtra("editar");
        excluir = intent.getStringExtra("excluir");


        //Toast.makeText(ListaLeitosActivity.this, "Grupo: " +grupo, Toast.LENGTH_SHORT).show();
        Log.i("mudar", "ListaLeitosActivity");
        final String idSetor = setor.getUid();
        String nomeSetor = setor.getNome();
        TextView id = (TextView) findViewById(R.id.idSetor);
        id.setText(idSetor);
        TextView nome = (TextView) findViewById(R.id.nomeSetorId);
        nome.setText(nomeSetor);
        listV_leitos = (ListView) findViewById(R.id.listV_leitos);

        eventoDatabase();

    }


    private void eventoDatabase() {
        Intent intent = getIntent();
        Setor setor = intent.getParcelableExtra("setor");
        //String grupo = intent.getStringExtra("grupo");
        String idSetor = setor.getUid();
        databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("Leitos").orderByChild("sid").equalTo(idSetor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listV_leitos = (ListView) findViewById(R.id.listV_leitos);
                    leitos = new ArrayList<Leito>();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Leito l = objSnapshot.getValue(Leito.class);
                        leitos.add(l);
                    }
                    ArrayAdapter adapter = new LeitoAdapter(getApplicationContext(), leitos);
                    listV_leitos.setAdapter(adapter);
                    listV_leitos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(Teste.this, "Leito: "+leitos.get(i).getSituacao(), Toast.LENGTH_SHORT).show();
                            if(mudar != null){
                                Intent it = new Intent(ListaLeitosActivity.this, DetalheLeitoActivity.class);
                                it.putExtra("leito", leitos.get(i));
                                it.putExtra("grupo", grupo);
                                it.putExtra("mudar", mudar);
                                startActivity(it);
                            }
                            else if(editar != null){
                                Intent it = new Intent(ListaLeitosActivity.this, EditarLeitoActivity.class);
                                it.putExtra("leito", leitos.get(i));
                                it.putExtra("grupo", grupo);
                                it.putExtra("editar", editar);
                                startActivity(it);
                            }
                            else if(excluir != null){
                                Intent it = new Intent(ListaLeitosActivity.this, ExcluirLeitoActivity.class);
                                it.putExtra("leito", leitos.get(i));
                                it.putExtra("grupo", grupo);
                                it.putExtra("excluir", excluir);
                                it.putExtra("grupo", grupo);
                                startActivity(it);
                            }
                            else if(grupo != null){
                                Intent it = new Intent(ListaLeitosActivity.this, DetalheLeitoActivity.class);
                                it.putExtra("leito", leitos.get(i));
                                it.putExtra("grupo", grupo);
                                startActivity(it);
                            }

                        }
                    });
                } else {
                    Toast.makeText(ListaLeitosActivity.this, "Este setor não possui leito cadastrado.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}