/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EstructurasDeDatosTemporales;

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Alexis
 */
public class ParametrosConsulta {

    private String nombreTabla;
    private String[] atributosOriginales;//este arreglo de cadenas va a servir el metodo de recuperar la
    //tabla original, pues trae los nombres de los atributos en sus posiciones originales
    private HashMap<String,Object[]> parametrosConsulta;
    private HashMap<String,Integer> posicionesAtributos;
    private Vector<Vector> grande;
    private String[] ordenImpresion;
    
    public ParametrosConsulta(String nombreTabla,String[] atributosOriginales,HashMap<String,Object[]> parametrosConsulta,HashMap<String,Integer> posicionesAtributos,Vector<Vector> grande,String[] ordenImpresion){
        this.nombreTabla = nombreTabla;
        this.atributosOriginales = atributosOriginales;
        this.parametrosConsulta = parametrosConsulta;
        this.posicionesAtributos = posicionesAtributos;
        this.grande = grande;
        this.ordenImpresion = ordenImpresion;  
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public String[] getAtributosOriginales() {
        return atributosOriginales;
    }

    public HashMap<String, Object[]> getParametrosConsulta() {
        return parametrosConsulta;
    }


    public HashMap<String, Integer> getPosicionesAtributos() {
        return posicionesAtributos;
    }


    public Vector<Vector> getGrande() {
        return grande;
    }


    public String[] getOrdenImpresion() {
        return ordenImpresion;
    }

    
}
