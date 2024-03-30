package pasosconsulta.versionesTablas.TablaEjecucion;

import java.util.Vector;

import javax.swing.JOptionPane;

import ControlConsultas.LogicaSQL;
import EstructurasDeDatosTemporales.MostarEnTabla;
import Vista.ConsultasComponent;

public class TEjecutadaComponent {

    private TEjecutadaTemplate tEjecutadaTemplate;
    private ConsultasComponent consultasComponent;

    public TEjecutadaComponent(ConsultasComponent consultasComponent) {
        this.tEjecutadaTemplate = new TEjecutadaTemplate(this);
        this.consultasComponent = consultasComponent;
    }

    private void reiniciarTabla(){
        tEjecutadaTemplate.getTabla().setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Tabla de consultas"
            }
        ));
        tEjecutadaTemplate.getMT().setRowCount(0);//se reinicia el modelo de la tabla
    }



    public void mostrarTEjecutada(){

        try{

            //tEjecutadaTemplate.getMT().setRowCount(0);//se reinicia el modelo de la tabla
            //se manda a llamar a la clase logicaSQL para que ejecute la consulta que el usuario ingreso en 
            //el textArea
            reiniciarTabla();//se reinicia el modelo de la tabla
            MostarEnTabla datosTabla = consultasComponent.tEjecutada();

        if(datosTabla == null){//en caso de que la clase LogicaSQL retorne nulo significa que hubo error a la hora 
            //de tratar de realizar la consulta
            JOptionPane.showMessageDialog(null, "Error al procesar la consulta");
        }

            Vector<Vector> datos = datosTabla.getDatos();//se crea un vector de vectores para poder mostrar 
            //los datos, ya que la tabla reacciona de acuerdo a matrices, en caso de que se le pasen en este caso
            //El vector de vectores actua como si dentro sus vectores pequeños fueran las filas de la tabla
            //y cada elemento de esos vectores pequeños fueran las columnas de la tabla
            String[] columnas = datosTabla.getColumnas();//para poder mostar los nombres de las columnas de la
            //tabla se crea un vector de strings que contendra los nombres de las columnas, y se recuperan todos
            //estos datos de la clase MostarEnTabla que se creo para poder tener organizados los datos que se
            //obtuvieron de la consulta
            
            tEjecutadaTemplate.getMT().setColumnIdentifiers(columnas);

            for(int i = 0; i < datos.size(); i++){
                tEjecutadaTemplate.getMT().addRow(datos.get(i));
            }
            tEjecutadaTemplate.getTabla().setModel(tEjecutadaTemplate.getMT());

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al procesar la consulta");
        }
        
    }

    public TEjecutadaTemplate getTEjecutadaTemplate() {
        return tEjecutadaTemplate;
    }

}
