package Vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import ControlConsultas.LogicaSQL;
import EstructurasDeDatosTemporales.MostarEnTabla;
import EstructurasDeDatosTemporales.PasosTablasContenido;
import pasosconsulta.pasosComponent;
import pasosconsulta.versionesTablas.tablaOriginal.TOriginalComponent;

public class ConsultasComponent implements ActionListener{//en esta clase se maneja la logica cuando se realizan
    //las acciones de los botones, o sea las consultas que pueda ingresar el usuario

    private ConsultasTemplate consultasTemplate;
    private pasosComponent pasosCompon;
    private TOriginalComponent tOriginalComponent;

    public ConsultasComponent(){
        this.consultasTemplate = new ConsultasTemplate(this);
        this.pasosCompon = new pasosComponent(this);


        consultasTemplate.getpOpciones().add(pasosCompon.getpasosTemplate());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        

        if(e.getSource() == consultasTemplate.getBEjecutar()){
            ejecutar();
        
    }
        if(e.getSource() == consultasTemplate.getBLimpiar()){
            limpiar();
        
    }
    }

    public void limpiar(){

        consultasTemplate.crearJTable();

        consultasTemplate.getTAreaConsulta().setText("");
        consultasTemplate.getMt().setRowCount(0);
        
    }

    public void mostrarTablas(String tipoTabla){//este metodo va a ser invocado en pasosComponent pues ahi
        //se encuentran los metodos de los botones para cambiar entre las tablas a mostrar
        consultasTemplate.getpDebajo().removeAll();
        switch(tipoTabla){
            case "TOriginal"://en caso de que se presione el boton de Tabla original se mostrara la tabla original
                if(tOriginalComponent == null){
                    tOriginalComponent = new TOriginalComponent(this);
                }
                tOriginalComponent.mostrarTOriginal();
                consultasTemplate.getpDebajo().add(tOriginalComponent.getTOriginalTemplate());
            break;
        }

        consultasTemplate.repaint();
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

    public MostarEnTabla ejecutarConsulta(){//este metodo lo van a usar las clases que realizan las tablas

        var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText(),"tabla");
        MostarEnTabla datosTabla = sLogicaSQL.ejecutarConsulta();//se manda a llamar a la clase que
        
        if(datosTabla == null){//en caso de que la clase LogicaSQL retorne nulo significa que hubo error a la hora 
            //de tratar de realizar la consulta
            JOptionPane.showMessageDialog(null, "Error al procesar la consulta");
            return null;
        }

        return datosTabla;
    }

    public PasosTablasContenido toriginal(){//este metodo lo va a usar la clase TOriginalComponent
        var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText(),"tabla");

        return sLogicaSQL.recuperarTablaOriginal();

    }

    public String getConsulta(){//este metodo lo van a usar las clases que realizan las tablas
        return consultasTemplate.getTAreaConsulta().getText();
    }

    public ConsultasTemplate getConsultasTemplate(){
        return this.consultasTemplate;
    }

}
