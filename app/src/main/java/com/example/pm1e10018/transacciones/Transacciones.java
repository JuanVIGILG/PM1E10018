package com.example.pm1e10018.transacciones;

public class Transacciones {

    public static final String tablausuarios = "usuarios";

    public static final String id = "id";
    public static final String pais = "pais";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";

    public static final String CreateTableUsuarios = "CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT,pais TEXT, nombre TEXT, telefono INTEGER, nota TEXT)";

    public static final String DropTableUsuarios = "DROP TABLE IF EXISTS usuarios";

    public static final String NameDataBase = "DBExamenPM";

}
