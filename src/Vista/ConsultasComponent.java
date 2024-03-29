package Vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import ControlConsultas.LogicaSQL;
import EstructurasDeDatosTemporales.MostarEnTabla;

public class ConsultasComponent implements ActionListener{//en esta clase se maneja la logica cuando se realizan
    //las acciones de los botones, o sea las consultas que pueda ingresar el usuario

    private ConsultasTemplate consultasTemplate;

    public ConsultasComponent(){
        this.consultasTemplate = new ConsultasTemplate(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        

        if(e.getSource() == consultasTemplate.getBEjecutar()){
            ejecutar();
        
    }
        if(e.getSource() == consultasTemplate.getBLimpiar()){
            limpiar();
        
    }
        if(e.getSource() == consultasTemplate.getBSiguiente()){
            System.out.println("Siguiente");
        
    }
    }

    public void limpiar(){

        consultasTemplate.crearJTable();

        consultasTemplate.getTAreaConsulta().setText("");
        consultasTemplate.getMt().setRowCount(0);
        
    }

    public void ejecutar(){

        try{

            consultasTemplate.crearJTable();//se manda a llamar al metodo que crea la tabla para que se reinicie
            consultasTemplate.getMt().setRowCount(0);//se reinicia el modelo de la tabla
            //se manda a llamar a la clase logicaSQL para que ejecute la consulta que el usuario ingreso en 
            //el textArea
            var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText(),"tabla");
            MostarEnTabla datosTabla = sLogicaSQL.ejecutarConsulta();//se manda a llamar a la clase que
            //ayudara a tener organizados los datos que se obtuvieron de la consulta para poder mostrarlos

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
            
            consultasTemplate.getMt().setColumnIdentifiers(columnas);

            for(int i = 0; i < datos.size(); i++){
                consultasTemplate.getMt().addRow(datos.get(i));
            }
            consultasTemplate.getTabla().setModel(consultasTemplate.getMt());

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al procesar la consulta");
        }

        

    }

    public ConsultasTemplate getConsultasTemplate(){
        return this.consultasTemplate;
    }

}
