package pasosconsulta.versionesTablas.tablaProyectada.tablaOriginal;

import java.util.Vector;

import EstructurasDeDatosTemporales.MostarEnTabla; 
import Vista.ConsultasComponent;

public class TProyectadaComponent {

    private TProyectadaTemplate tProyectadaTemplate;
    private ConsultasComponent consultasComponent;

    public TProyectadaComponent(ConsultasComponent consultasComponent) {
        this.tProyectadaTemplate = new TProyectadaTemplate(this);
        this.consultasComponent = consultasComponent;
    }

    private void reiniciarTabla(){
        tProyectadaTemplate.getTabla().setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Tabla de consultas"
            }
        ));

        tProyectadaTemplate.getMt().setRowCount(0);//se reinicia el modelo de la tabla
    }


    public void mostrarTProyectada(){
        
        try {

            //tProyectadaTemplate.getMt().setRowCount(0);//se reinicia el modelo de la tabla
            //se manda a llamar a la clase logicaSQL para que ejecute la consulta que el usuario ingreso en 
            //el textArea
            reiniciarTabla();//se reinicia el modelo de la tabla
            MostarEnTabla datosTabla = consultasComponent.tProyectada();
            

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
            
            

            tProyectadaTemplate.getMt().setColumnIdentifiers(columnas);

            for(int i = 0; i < datos.size(); i++){
                tProyectadaTemplate.getMt().addRow(datos.get(i));
            }
            tProyectadaTemplate.getTabla().setModel(tProyectadaTemplate.getMt());
            
            //descriptor.contenido(); el metodo retorna un vector de String, que contiene la informacion de las lineas leidas
        } catch (Exception ex) {
            return;
        }
        
    }

    public TProyectadaTemplate getTProyectadaTemplate(){
        return tProyectadaTemplate;
    }

}
