package Vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import ControlConsultas.LogicaSQL;
import EstructurasDeDatosTemporales.MostarEnTabla;
import pasosconsulta.pasosComponent;
import pasosconsulta.versionesTablas.TablaEjecucion.TEjecutadaComponent;
import pasosconsulta.versionesTablas.tablaOriginal.TOriginalComponent;
import pasosconsulta.versionesTablas.tablaProyectada.tablaOriginal.TProyectadaComponent;

public class ConsultasComponent {//en esta clase se maneja la logica cuando se realizan
    //las acciones de los botones, o sea las consultas que pueda ingresar el usuario

    private ConsultasTemplate consultasTemplate;
    private pasosComponent pasosCompon;
    private TOriginalComponent tOriginalComponent;
    private TProyectadaComponent tProyectadaComponent;
    private TEjecutadaComponent tEjecutadaComponent;

    public ConsultasComponent(){
        this.consultasTemplate = new ConsultasTemplate(this);
        this.pasosCompon = new pasosComponent(this);


        consultasTemplate.getpDerecha().add(pasosCompon.getpasosTemplate());
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
            case "TProyectada"://en caso de que se presione el boton de Tabla proyectada se mostrara la tabla proyectada
                if(tProyectadaComponent == null){
                    tProyectadaComponent = new TProyectadaComponent(this);
                }
                tProyectadaComponent.mostrarTProyectada();
                consultasTemplate.getpDebajo().add(tProyectadaComponent.getTProyectadaTemplate());
            break;
            case "Limpiar":
                limpiar();
            break;
            case "Ejecutar":
                if(tEjecutadaComponent == null){
                    tEjecutadaComponent = new TEjecutadaComponent(this);
                }
                tEjecutadaComponent.mostrarTEjecutada();
                consultasTemplate.getpDebajo().add(tEjecutadaComponent.getTEjecutadaTemplate());

        }

        consultasTemplate.repaint();
    }

    public MostarEnTabla ejecutarConsulta(){//este metodo lo van a usar las clases que realizan las tablas

        var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText());
        MostarEnTabla datosTabla = sLogicaSQL.ejecutarConsulta();//se manda a llamar a la clase que
        
        if(datosTabla == null){//en caso de que la clase LogicaSQL retorne nulo significa que hubo error a la hora 
            //de tratar de realizar la consulta
            JOptionPane.showMessageDialog(null, "Error al procesar la consulta");
            return null;
        }

        return datosTabla;
    }

    public MostarEnTabla tOriginal(){//este metodo lo va a usar la clase TOriginalComponent
        var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText());

        return sLogicaSQL.recuperarTablaOriginal();

    }

    public MostarEnTabla tProyectada(){//este metodo lo va a usar la clase TProyectadaComponent
        var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText());

        return sLogicaSQL.tablaProyectada();
    }

    public MostarEnTabla tEjecutada(){//este metodo lo va a usar la clase TEjecutadaComponent
        var sLogicaSQL = new LogicaSQL(consultasTemplate.getTAreaConsulta().getText());

        return sLogicaSQL.ejecutarConsulta();
    }

    public String getConsulta(){//este metodo lo van a usar las clases que realizan las tablas
        return consultasTemplate.getTAreaConsulta().getText();
    }

    public ConsultasTemplate getConsultasTemplate(){
        return this.consultasTemplate;
    }

}
