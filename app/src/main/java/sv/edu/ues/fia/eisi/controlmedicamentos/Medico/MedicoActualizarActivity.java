package sv.edu.ues.fia.eisi.controlmedicamentos.Medico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import sv.edu.ues.fia.eisi.controlmedicamentos.BDProyecto.BDMedicamentosControl;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Medico;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Usuario;
import sv.edu.ues.fia.eisi.controlmedicamentos.ContactoMedico.MenuContactoActivity;
import sv.edu.ues.fia.eisi.controlmedicamentos.Enfermedades.MenuEnfermedadesActivity;
import sv.edu.ues.fia.eisi.controlmedicamentos.Medicamento.MenuMedicamentoActivity;
import sv.edu.ues.fia.eisi.controlmedicamentos.MenuPrincipalActivity;
import sv.edu.ues.fia.eisi.controlmedicamentos.R;
import sv.edu.ues.fia.eisi.controlmedicamentos.Usuarios.MenuUsuarioActivity;
import sv.edu.ues.fia.eisi.controlmedicamentos.Usuarios.UsuarioRegistrarActivity;

public class MedicoActualizarActivity extends AppCompatActivity {

    private EditText ediNombreM,EdiEspecialidadM;
    private String NombreMA,EspecialidadMA,usuarioidA,idMedicox;
    private Spinner comboUsuario;
    private ArrayList<String> listaPersonas;
    private ArrayList<Medico> PersonasList;
    private BDMedicamentosControl.DatabaseHelper conn;
    private BDMedicamentosControl helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_actualizar);
        comboUsuario=(Spinner) findViewById(R.id.comboMedico);
        ediNombreM=(EditText)findViewById(R.id.ediMANombre);
        EdiEspecialidadM=(EditText)findViewById(R.id.ediMAEspecialidad);
        helper=new BDMedicamentosControl(this);
        conn = new BDMedicamentosControl.DatabaseHelper(getApplicationContext());

        consultarListaPersonas();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaPersonas);
        comboUsuario.setAdapter(adaptador);
        comboUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0){
                    usuarioidA=PersonasList.get(i-1).getIdUsuariom();
                    idMedicox=PersonasList.get(i-1).getIdMedico();
                    ediNombreM.setText(PersonasList.get(i-1).getNombre());
                    EdiEspecialidadM.setText(PersonasList.get(i-1).getEspecialidad());
                }
                else{
                    ediNombreM.setText("");
                    EdiEspecialidadM.setText("");

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void consultarListaPersonas() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Medico persona=null;
        PersonasList=new ArrayList<Medico>();
        Cursor cursor=db.rawQuery("select idMedico,idUsuario,nombre,especialidad from medico ", null);

        while (cursor.moveToNext()){
            Medico medico = new Medico();
            medico.setIdMedico(cursor.getString(0));
            medico.setIdUsuariom(cursor.getString(1));
            medico.setNombre(cursor.getString(2));
            medico.setEspecialidad(cursor.getString(3));
            PersonasList.add(medico);
        }
        obtenerLista();

    }

    private void obtenerLista() {
        listaPersonas = new ArrayList<String>();
        listaPersonas.add("Seleccione");
        for (int i=0; i<PersonasList.size();i++){
            listaPersonas.add("Nombre :"+PersonasList.get(i).getNombre()+"--"+
                                         PersonasList.get(i).getEspecialidad()+"\n");
        }
    }

     public void Actualizar_Medico(View view) {
         if (ediNombreM.getText().toString().equals("")){

             Toast.makeText(this,"seleccione un medico" , Toast.LENGTH_SHORT).show();

         }
         else {


             NombreMA= ediNombreM.getText().toString();
             EspecialidadMA=EdiEspecialidadM.getText().toString();
             String regInsertados;
             Medico medicos  =new Medico();
             medicos.setNombre(NombreMA);
             medicos.setEspecialidad(EspecialidadMA);

             Medico medico =new Medico();
             medico.setIdMedico(idMedicox);
        try {


            helper.abrir();
            regInsertados=helper.actualizarMedico(medicos,medico);
            helper.cerrar();
            Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
            Toast.makeText(this,"Error/algun campo esta vacio" , Toast.LENGTH_SHORT).show();
        }
         }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuopciones, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.medicamento:

                return true;
            case R.id.menuPrincipal:
                intent= new Intent(MedicoActualizarActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuMedicamento:
                intent = new Intent(MedicoActualizarActivity.this, MenuMedicamentoActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuUsuario:
                intent = new Intent(MedicoActualizarActivity.this, MenuUsuarioActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuEnfermedad:
                intent = new Intent(MedicoActualizarActivity.this, MenuEnfermedadesActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuMedico:
                intent = new Intent(MedicoActualizarActivity.this, MenuMedicoActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuContacto:
                intent = new Intent(MedicoActualizarActivity.this, MenuContactoActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}