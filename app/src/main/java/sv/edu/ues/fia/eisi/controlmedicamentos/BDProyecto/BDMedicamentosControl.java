package sv.edu.ues.fia.eisi.controlmedicamentos.BDProyecto;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Contacto;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Dosis;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Enfermedad;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Establecimiento;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Medicamento;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Medico;
import sv.edu.ues.fia.eisi.controlmedicamentos.Clases.Usuario;

public class BDMedicamentosControl {

        private static final String[]camposUsuarioconsulta=new String []{"nombre","correo","contraseña"};
        private static final String[]camposUsuario = new String []{"idUsuario","nombre","apellido","edad","genero","contraseña","correo"};
        private static final String[]camposMedico = new String [] {"idMedico","idUsuario","nombre","especialidad"};
        private static final String[]camposEstablecimiento = new String [] {"idEstablecimiento","nombre","direccion","telefono","idUsuario"};
        private static final String[]camposCitaMedica = new String [] {"idCitaMedica","idMedico","titulo","telefono","idUsuario"};
        private static final String[]camposContacto = new String [] {"idContacto","idMedico","direccion","telefono"};


    private final Context context;
        private DatabaseHelper DBHelper;
        private SQLiteDatabase db;
        public BDMedicamentosControl(Context ctx) {this.context = ctx; DBHelper = new DatabaseHelper(context); }

        public static class DatabaseHelper extends SQLiteOpenHelper {
            private static final String BASE_DATOS = "ControlMedicamentoss.s3db";
            private static final int VERSION = 1;
            public DatabaseHelper(Context context) {
                super(context, BASE_DATOS, null, VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                try{
                    db.execSQL("CREATE TABLE usuario(idUsuario INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,nombre VARCHAR(25),apellido VARCHAR(25),edad Integer,genero VARCHAR(25),contraseña VARCHAR(15),correo VARCHAR(100));");
                    db.execSQL("CREATE TABLE medico(idMedico INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idUsuario INTEGER,nombre VARCHAR(25),especialidad VARCHAR(25));");
                    db.execSQL("CREATE TABLE medicoContacto(idContacto INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idUsuario INTEGER,idMedico INTEGER,direccion VARCHAR(75),Telefono VARCHAR(25));");
                    db.execSQL("CREATE TABLE enfermedad(idEnfermedad INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idMedico INTEGER,idUsuario INTEGER,nombreEnfermedad VARCHAR(75),fecha VARCHAR(25),tipo VARCHAR(25));");
                    db.execSQL("CREATE TABLE medicamento(idMedicamento INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idMedico INTEGER,idEnfermedad INTEGER,idUsuario INTEGER,nombre VARCHAR(50),tipo VARCHAR(25));");
                    db.execSQL("CREATE TABLE establecimiento(idEstablecimiento INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idUsuario INTEGER,nombre VARCHAR(50),direccion VARCHAR(100),telefono VARCHAR(50));");
                    db.execSQL("CREATE TABLE cita(idCita INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idUsuario INTEGER,idEstablecimiento INTEGER,fecha VARCHAR(50),descripcion VARCHAR(100));");
                    db.execSQL("CREATE TABLE dosis(idDosis INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,idMedico INTEGER,idEnfermedad INTEGER,idUsuario INTEGER,idMedicamento INTEGER,tipo VARCHAR(100),cada VARCHAR(25),fechaInicio VARCHAR(50),fechaFin VARCHAR(50));");

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
            }
        }
        public void abrir() throws SQLException{
            db = DBHelper.getWritableDatabase();
            return;
        }
        public void cerrar(){
            DBHelper.close();
        }

    public String InicioUsuario(Usuario usuario){
        if(verificarIntegridad(usuario, 3)){
           return "autorizado";
        }else{
            return "denegado";
        }
    }
//--------------Inicio de usuario-------------------------------------------------
//--------------Inicio de usuario-------------------------------------------------
//--------------Inicio de usuario-------------------------------------------------

    public String insertar(Usuario usuario){
            String regInsertados="Usuario registrado Nº= ";
            long contador=0;
            //,,,,,,
            if(verificarIntegridad(usuario,1)){
                regInsertados= "Error al Insertar el registro, Correo Duplicado. Verificar inserción";
            }
            else{
                ContentValues cond = new ContentValues();
                cond.put("idUsuario",usuario.getIdUsuario());
                cond.put("nombre",usuario.getNombre());
                cond.put("apellido",usuario.getApellido());
                cond.put("edad",usuario.getEdad());
                cond.put("genero",usuario.getGenero());
                cond.put("contraseña",usuario.getContraseña());
                cond.put("correo",usuario.getCorreo());

                contador=db.insert("usuario", null, cond);
                if(contador==-1 || contador==0)
                {
                    regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
                }
                else {
                    regInsertados=regInsertados+contador;
                }

            }
            return regInsertados;
        }

    public String actualizar(Usuario usuario,Usuario usuario2){
        if(verificarIntegridad(usuario2, 2)){
            String[] id = {usuario2.getCorreo()};
            ContentValues cond = new ContentValues();
            cond.put("nombre",usuario.getNombre());
            cond.put("apellido",usuario.getApellido());
            cond.put("edad",usuario.getEdad());
            cond.put("genero",usuario.getGenero());
            cond.put("correo",usuario.getCorreo());
            db.update("usuario", cond, "correo = ?", id);
            return "Usuario Actualizado Correctamente";
        }else{
            return "No Existe el usuario";
        }
    }
    public Usuario consultarUsuario(String correo){

        String[] id = {correo};
        Cursor cursor = db.query("usuario", camposUsuarioconsulta, "correo = ?"
                , id, null, null, null);
        if(cursor.moveToFirst()){
            Usuario usuario = new Usuario();
            usuario.setNombre(cursor.getString(0));
            usuario.setCorreo(cursor.getString(1));
            usuario.setContraseña(cursor.getString(2));
            return usuario;
        }else{
            return null;
        }
    }

    public String eliminarUsuario(Usuario usuario){

        String regAfectados="Usuario eliminado = ";
        int contador=0;

        if (verificarIntegridad(usuario,4))
        {
            contador+=db.delete("medico", "idUsuario='"+usuario.getIdUsuario()+"'", null);

        }
        if (verificarIntegridad(usuario,8))
        {
            contador+=db.delete("medicoContacto", "idUsuario='"+usuario.getIdUsuario()+"'", null);
        }
        contador+=db.delete("usuario", "idUsuario='"+usuario.getIdUsuario()+"'", null);
        contador+=db.delete("enfermedad", "idUsuario='"+usuario.getIdUsuario()+"'", null);
        contador+=db.delete("medicamento", "idUsuario='"+usuario.getIdUsuario()+"'", null);
        contador+=db.delete("establecimiento", "idUsuario='"+usuario.getIdUsuario()+"'", null);
        contador+=db.delete("medicoContacto", "idUsuario='"+usuario.getIdUsuario()+"'", null);
        contador+=db.delete("medico", "idUsuario='"+usuario.getIdUsuario()+"'", null);


        if (contador>=1){
            regAfectados+=contador+"/nSe elimino el registro "+usuario.getCorreo();
        }
        else{
            regAfectados="No se elimino ningun Usuario";
        }

        return regAfectados;
    }

    //--------------fin de usuario-------------------------------------------------
    //--------------fin de usuario-------------------------------------------------
    //--------------fin de usuario-------------------------------------------------


    //--------------Inicio de Medico-------------------------------------------------
    //--------------Inicio de Medico-------------------------------------------------
    //--------------Inicio de Medico-------------------------------------------------

    public String insertarMedico(Medico medico){

        String regInsertados="Medico insertado Nº= ";
        long contador=0;

        ContentValues conn = new ContentValues();

        conn.put("idMedico", medico.getIdMedico());
        conn.put("idUsuario", medico.getIdUsuariom());
        conn.put("nombre", medico.getNombre());
        conn.put("especialidad", medico.getEspecialidad());

        contador=db.insert("medico", null, conn);

        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else {
            regInsertados=regInsertados+contador;
        }
        return regInsertados;

    }


    public String eliminarMedico(Medico medico){
        String regAfectados="Medicos eliminados = ";
        int contador=0;

        contador+=db.delete("medico", "idMedico='"+medico.getIdMedico()+"'", null);
        contador+=db.delete("medicamento", "idMedico='"+medico.getIdMedico()+"'", null);
        contador+=db.delete("enfermedad", "idMedico='"+medico.getIdMedico()+"'", null);
        contador+=db.delete("medicoContacto", "idMedico='"+medico.getIdMedico()+"'", null);



        if (contador>=1){
            regAfectados+=contador+"/nSe elimino el registro "+medico.getIdMedico();
        }
        else{
            regAfectados="No se elimino ningun Medico";
        }

        return regAfectados;
    }
    public String actualizarMedico(Medico medico,Medico medico2){
        if(verificarIntegridad(medico2, 5)){
            String[] id = {medico2.getIdMedico()};
            ContentValues cond = new ContentValues();
            cond.put("nombre",medico.getNombre());
            cond.put("especialidad",medico.getEspecialidad());
            db.update("medico", cond, "idMedico= ?",id);
            return "Registro Actualizado Correctamente";
        }else{
            return "Registro no Existe";
        }
    }
    //--------------Fin de Medico-------------------------------------------------
    //--------------Fin de Medico-------------------------------------------------
    //--------------Fin de Medico-------------------------------------------------

    //--------------Inicio de contacto-------------------------------------------------
    //--------------Inicio de contactoo-------------------------------------------------
    //--------------Inicio de contacto-------------------------------------------------

    public String insertarContacto(Contacto contacto){

        String regInsertados="Contacto insertado Nº= ";
        long contador=0;

        if(verificarIntegridad(contacto,7)){
            regInsertados= "Error al Insertar el el contacto, el  contacto ya existe";
        }
        else{
        ContentValues conn = new ContentValues();
        conn.put("idContacto", contacto.getIdContacto());
        conn.put("idUsuario",contacto.getIdUsuario());
        conn.put("idMedico", contacto.getIdMedico());
        conn.put("direccion", contacto.getDireccion());
        conn.put("telefono", contacto.getTelefono());

        contador=db.insert("medicoContacto", null, conn);

        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else {
            regInsertados=regInsertados+contador;
        }
        }
        return regInsertados;

    }

    public String actualizarContacto(Contacto contacto){
        if(verificarIntegridad(contacto, 6)){
            String[] id = {contacto.getIdMedico()};
            ContentValues cond = new ContentValues();
            cond.put("idMedico",contacto.getIdMedico());
            cond.put("direccion",contacto.getDireccion());
            cond.put("telefono",contacto.getTelefono());
            db.update("medicoContacto", cond, "idMedico = ?", id);
            return "Registro Actualizado Correctamente";
        }else{
            return "Registro no Existe";
        }
    }

    //--------------Fin de contacto-------------------------------------------------
    //--------------Fin de contacto-------------------------------------------------
    //--------------Fin de contacto-------------------------------------------------

    public String insertarEnfermedad(Enfermedad enfermedad){

        String regInsertados="Insertar Enfermedad Nº= ";
        long contador=0;

        ContentValues conn = new ContentValues();
        conn.put("idEnfermedad", enfermedad.getIdEnfermedad());
        conn.put("idMedico", enfermedad.getIdMedico());
        conn.put("idUsuario", enfermedad.getIdUsuario());
        conn.put("nombreEnfermedad", enfermedad.getNombre());
        conn.put("fecha", enfermedad.getFecha());
        conn.put("tipo", enfermedad.getTipo());

        contador=db.insert("enfermedad", null, conn);

        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else {
            regInsertados=regInsertados+contador;
        }
        return regInsertados;

    }
    public String ActualizarEnfermedad(Enfermedad enfermedad){
        String regAfectados=" ";
        int contador=0;

        String[] id = {enfermedad.getIdEnfermedad()};
        ContentValues cond = new ContentValues();
        cond.put("nombreEnfermedad",enfermedad.getNombre());
        cond.put("fecha",enfermedad.getFecha());
        cond.put("tipo",enfermedad.getTipo());
        contador+=db.update("enfermedad", cond, "idEnfermedad= ?",id);

        if (contador>=1){
            regAfectados+=contador+"Se actualizo el registro "+enfermedad.getIdEnfermedad();
        }
        else{
            regAfectados="No se actualizo ningun Medico";
        }
        return regAfectados;


    }
    public String eliminarEnfermedad(Enfermedad enfermedad){
        String regAfectados="Enfermedades eliminados = ";
        int contador=0;
        contador+=db.delete("enfermedad", "idEnfermedad='"+enfermedad.getIdEnfermedad()+"'", null);
        contador+=db.delete("medicamento", "idEnfermedad='"+enfermedad.getIdEnfermedad()+"'", null);
        if (contador>=1){
            regAfectados+=contador+"/nSe elimino el registro "+enfermedad.getIdEnfermedad();
        }
        else{
            regAfectados="No se elimino ningun Medico";
        }

        return regAfectados;
    }

    public String insertarMedicamento(Medicamento medicamento){

        String regInsertados="Insertar medicamento Nº= ";
        long contador=0;

        ContentValues conn = new ContentValues();
        conn.put("idMedicamento",medicamento.getIdMedicamento());
        conn.put("idMedico", medicamento.getIdMedico());
        conn.put("idEnfermedad", medicamento.getIdEnfermedad());
        conn.put("idUsuario", medicamento.getIdUsuario());
        conn.put("nombre", medicamento.getNombreEnf());
        conn.put("tipo",medicamento.getTipo());

        contador=db.insert("medicamento", null, conn);

        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else {
            regInsertados=regInsertados+contador;
        }
        return regInsertados;

    }
    public String ActualizarMedicamento(Medicamento medicamento){
        String regAfectados=" ";
        int contador=0;

            String[] id = {medicamento.getIdMedicamento()};
            ContentValues cond = new ContentValues();
            cond.put("nombre",medicamento.getNombreEnf());
            cond.put("tipo",medicamento.getTipo());
            contador+=db.update("medicamento", cond, "idMedicamento= ?",id);

        if (contador>=1){
            regAfectados+=contador+"Se actualizo el registro "+medicamento.getIdMedicamento();
        }
        else{
            regAfectados="No se actualizo ningun Medico";
        }
        return regAfectados;


    }
    public String eliminarMedicamento(Medicamento medicamento){
        String regAfectados=" ";
        int contador=0;
        contador+=db.delete("medicamento", "idMedicamento='"+medicamento.getIdMedicamento()+"'", null);

        if (contador>=1){
            regAfectados+=contador+"Se elimino el registro "+medicamento.getIdMedicamento();
        }
        else{
            regAfectados="No se elimino ningun Medico";
        }

        return regAfectados;
    }

    public String insertarEstablecimiento(Establecimiento establecimiento){

        String regInsertados="Insertardo establecimiento Nº= ";
        long contador=0;

        ContentValues conn = new ContentValues();
        conn.put("idEstablecimiento",establecimiento.getIdEstablecimiento());
        conn.put("idUsuario", establecimiento.getIdUsuario());
        conn.put("nombre", establecimiento.getNombre());
        conn.put("direccion", establecimiento.getDireccion());
        conn.put("telefono", establecimiento.getTelefono());

        contador=db.insert("establecimiento", null, conn);

        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else {
            regInsertados=regInsertados+contador;
        }
        return regInsertados;

    }
    public String eliminarEstablecimiento(Establecimiento establecimiento){
        String regAfectados=" ";
        int contador=0;
        contador+=db.delete("establecimiento", "idEstablecimineto='"+establecimiento.getIdEstablecimiento()+"'", null);

        if (contador>=1){
            regAfectados+=contador+"Se elimino el registro "+establecimiento.getIdEstablecimiento();
        }
        else{
            regAfectados="No se elimino ningun Establecimiento";
        }

        return regAfectados;
    }

    public String insertarDosis(Dosis dosis){

        String regInsertados="Dosis insertada Nº= ";
        long contador=0;

        ContentValues conn = new ContentValues();
        conn.put("idDosis",dosis.getIdDosis());
        conn.put("idMedico", dosis.getIdMedico());
        conn.put("idEnfermedad", dosis.getIdEnfermedad());
        conn.put("idUsuario", dosis.getIdUsuario());
        conn.put("idMedicamento", dosis.getIdMedicamento());
        conn.put("tipo", dosis.getUnidad());
        conn.put("cada", dosis.getSecuencia());
        conn.put("fechaInicio", dosis.getFechaInicio());
        conn.put("fechaFin", dosis.getFechaFin());

        contador=db.insert("dosis", null, conn);

        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else {
            regInsertados=regInsertados+contador;
        }
        return regInsertados;

    }
    private boolean verificarIntegridad(Object dato, int relacion) throws SQLException{
        switch(relacion){

            case 1://verificar que al insertar usuario no exista el correo
            {
                Usuario usuario = (Usuario) dato;
                String[] id1 = {usuario.getCorreo()};
                //abrir();
                Cursor cursor1 = db.query("usuario", null, "correo = ?",
                        id1, null,null, null);
                 if(cursor1.moveToFirst()){
                //Se encontraron datos
                    return true;
                }
                return false;
        }
        case 2:
        {
//verificar que al modificar nota exista carnet del alumno, el    codigo de materia y el ciclo
            Usuario usuario1 = (Usuario) dato;
            String[] ids = {usuario1.getCorreo()};
            abrir();
            Cursor c = db.query("usuario", null, "correo = ?", ids,
                    null, null, null);
            if(c.moveToFirst()){
//Se encontraron datos
                return true;
            }
            return false;
        }
            case 3:
            {
//verificar que al modificar nota exista carnet del alumno, el    codigo de materia y el ciclo
                Usuario usuario1 = (Usuario) dato;
                String[] ids = {usuario1.getCorreo(),usuario1.getContraseña()};
                abrir();
                Cursor c = db.query("usuario", null, "correo = ? AND contraseña = ?", ids,
                        null, null, null);
                if(c.moveToFirst()){
            //Se encontraron datos
                    return true;
                }
                return false;
            }
            case 4:
            {   Usuario usuario = (Usuario) dato;
                Cursor c=db.query(true, "medico", new String[] {
                                "idUsuario" }, "idUsuario='"+usuario.getIdUsuario()+"'",null,
                        null, null, null, null);
                if(c.moveToFirst())
                    return true;
                else
                    return false;
            }

            case 5:
            {
//verificar que al modificar nota exista carnet del alumno, el    codigo de materia y el ciclo
                Medico medico1 = (Medico) dato;
                String[] ids = {medico1.getIdMedico()};
                abrir();
                Cursor c = db.query("medico", null, "idMedico = ?", ids,
                        null, null, null);
                if(c.moveToFirst()){
//Se encontraron datos
                    return true;
                }
                return false;
            }

            case 6:
            {
//verificar que al modificar nota exista carnet del alumno, el    codigo de materia y el ciclo
               Contacto contacto = (Contacto) dato;
                String[] ids = {contacto.getIdMedico()};
                abrir();
                Cursor c = db.query("medico", null, "idMedico = ?", ids,
                        null, null, null);
                if(c.moveToFirst()){
//Se encontraron datos
                    return true;
                }
                return false;
            }

            case 7://verificar que al insertar usuario no exista el correo
            {
                Contacto contacto = (Contacto) dato;
                String[] id1 = {contacto.getIdMedico()};
                //abrir();
                Cursor cursor1 = db.query("medicoContacto", null, "idMedico = ?",
                        id1, null,null, null);
                if(cursor1.moveToFirst()){
                    //Se encontraron datos
                    return true;
                }
                return false;
            }
            case 8:
            {   Usuario usuario = (Usuario) dato;
                Cursor c=db.query(true, "medicoContacto", new String[] {
                                "idUsuario" }, "idUsuario='"+usuario.getIdUsuario()+"'",
                        null,null, null, null, null);
                if(c.moveToFirst())
                    return true;
                else
                    return false;
            }

        default:
        return false;
    }

    }
}

