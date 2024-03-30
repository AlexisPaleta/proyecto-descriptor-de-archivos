package EstructurasDeDatosTemporales;

import java.util.Vector;

public class MostarEnTabla {

    private Vector<Vector> datos;
    private String[] columnas;
    private String nombreTabla;

    public MostarEnTabla(Vector<Vector> datos, String[] columnas, String nombreTabla) {
        this.datos = datos;
        this.columnas = columnas;
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
