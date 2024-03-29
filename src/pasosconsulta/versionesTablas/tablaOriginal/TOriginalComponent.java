package pasosconsulta.versionesTablas.tablaOriginal;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import EstructurasDeDatosTemporales.DescriptorArchivos;
import EstructurasDeDatosTemporales.MostarEnTabla;
import EstructurasDeDatosTemporales.PasosTablasContenido;
import Vista.ConsultasComponent;
import Vista.VentanaTabla;

public class TOriginalComponent {

    private TOriginalTemplate tOriginalTemplate;
    private ConsultasComponent consultasComponent;

    public TOriginalComponent(ConsultasComponent consultasComponent) {
        this.tOriginalTemplate = new TOriginalTemplate(this);
        this.consultasComponent = consultasComponent;
    }


    public void mostrarTOriginal(){

        
        try {

            PasosTablasContenido datosTabla = consultasComponent.toriginal();

            if(datosTabla == null){
                return;
            }

        
            Vector<Vector> datos = datosTabla.getDatos();//se crea un vector de vectores para poder mostrar 
            //los datos, ya que la tabla reacciona de acuerdo a matrices, en caso de que se le pasen en este caso
            //El vector de vectores actua como si dentro sus vectores pequeños fueran las filas de la tabla
            //y cada elemento de esos vectores pequeños fueran las columnas de la tabla
            String[] columnas = datosTabla.getColumnas();//para poder mostar los nombres de las columnas de la
            //tabla se crea un vector de strings que contendra los nombres de las columnas, y se recuperan todos
            //estos datos de la clase MostarEnTabla que se creo para poder tener organizados los datos que se
            //obtuvieron de la consulta
            
            

            tOriginalTemplate.getMt().setColumnIdentifiers(columnas);

            for(int i = 0; i < datos.size(); i++){
                tOriginalTemplate.getMt().addRow(datos.get(i));
            }
            tOriginalTemplate.getTabla().setModel(tOriginalTemplate.getMt());
            
            //descriptor.contenido(); el metodo retorna un vector de String, que contiene la informacion de las lineas leidas
        } catch (Exception ex) {
            return;
        }
        
    }

    public TOriginalTemplate getTOriginalTemplate(){
        return tOriginalTemplate;
    }

}
