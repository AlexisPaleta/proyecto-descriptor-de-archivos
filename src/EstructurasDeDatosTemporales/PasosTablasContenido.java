package EstructurasDeDatosTemporales;

import java.util.Vector;

public class PasosTablasContenido {

    private Vector<Vector> datos;
    private String[] columnas;
    private String nombreTabla;
    
    public PasosTablasContenido(Vector<Vector> datos, String[] columnas, String nombreTabla){
        this.datos = datos;
        this.columnas = columnas;
        this.nombreTabla = nombreTabla;
    }

    public Vector<Vector> getDatos() {
        return datos;
    }

    public String[] getColumnas() {
        return columnas;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

}
