package sv.edu.ues.fia.eisi.controlmedicamentos.Medicamento;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sv.edu.ues.fia.eisi.controlmedicamentos.BDProyecto.BDMedicamentosControl;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Medicamento;
import sv.edu.ues.fia.eisi.controlmedicamentos.R;

public class MedicamentosEditarActivity extends AppCompatActivity {
    private ArrayList<Medicamento> listaMedicamentos;
    private ArrayList<String> listaInformacionMedicamento;
    private BDMedicamentosControl.DatabaseHelper DBH;
    private BDMedicamentosControl controlhelper;
    private Spinner comboMedicamento;
    private String idMedicamento,nombre,tipo;
    private EditText edinombre,ediTipo;
    private TextView txtusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos_editar);
        DBH = new BDMedicamentosControl.DatabaseHelper(getApplicationContext());
        comboMedicamento=(Spinner)findViewById(R.id.comboActualizarMedicamento) ;
        edinombre=(EditText) findViewById(R.id.actMediNombre) ;
        ediTipo=(EditText) findViewById(R.id.actediTipo);
        txtusuario=(TextView) findViewById(R.id.actMediNombreU);

        controlhelper=new BDMedicamentosControl (this);
        consultarUsuarios2();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacionMedicamento);
        comboMedicamento.setAdapter(adaptador);
        comboMedicamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0){
                    idMedicamento=listaMedicamentos.get(i-1).getIdMedicamento();
                    edinombre.setText(listaMedicamentos.get(i-1).getNombreEnf());
                    ediTipo.setText(listaMedicamentos.get(i-1).getTipo());
                }
                else{
                    edinombre.setText("");
                    ediTipo.setText("");
                    txtusuario.setText("");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void consultarUsuarios2() {
        try {
            SQLiteDatabase db=DBH.getReadableDatabase();
            listaMedicamentos= new ArrayList<Medicamento>();
            /*this.idMedicamento = idMedicamento;
        this.idMedico = idMedico;
        this.idEnfermedad = idEnfermedad;
        this.idUsuario = idUsuario;
        this.nombreEnf = nombreEnf;
        this.tipo = tipo;*/
            Cursor cursor = db.rawQuery(
                    "select idMedicamento,nombre,tipo from medicamento ", null);

            while (cursor.moveToNext()){
                Medicamento medicamento= new Medicamento();
                medicamento.setIdMedicamento(cursor.getString(0));
                medicamento.setNombreEnf(cursor.getString(1));
                medicamento.setTipo(cursor.getString(2));
                listaMedicamentos.add(medicamento);
            }
            obtenerLista();
        }catch(Exception e){
            Toast.makeText(this,"Error/algun campo esta vacio" , Toast.LENGTH_SHORT).show();
        }
    }
    private void obtenerLista() {
        listaInformacionMedicamento = new ArrayList<String>();
        listaInformacionMedicamento.add("Seleccione");
        for (int i=0; i<listaMedicamentos.size();i++){
            listaInformacionMedicamento.add(
                    "Medicamento :"+listaMedicamentos.get(i).getNombreEnf()+"\n"+
                            "Tipo :"+listaMedicamentos.get(i).getTipo()+"\n"+
                            "-------------------------------------------");
        }
    }

    public void Actualizar(View view) {
        try {
            if (listaMedicamentos.size()!=0){
                nombre=edinombre.getText().toString();
                tipo=ediTipo.getText().toString();
                String regEliminadas;
                Medicamento medicamento = new Medicamento();
                medicamento.setIdMedicamento(idMedicamento);
                medicamento.setNombreEnf(nombre);
                medicamento.setTipo(tipo);
                controlhelper.abrir();
                regEliminadas=controlhelper.ActualizarMedicamento(medicamento);
                controlhelper.cerrar();
                Toast.makeText(this, regEliminadas, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"no hay registro que eliminar" , Toast.LENGTH_SHORT).show();

            }
        }catch(Exception e){
            Toast.makeText(this,"Error/algun campo esta vacio" , Toast.LENGTH_SHORT).show();
        }
    }
}