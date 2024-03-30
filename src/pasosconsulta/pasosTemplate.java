package pasosconsulta;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Services.ObjGraficosService;
import Services.RecursosService;

public class pasosTemplate extends JPanel{

    private pasosComponent pasosComponent;
    private ObjGraficosService sObjGraficos;
    private RecursosService sRecursos;

    private JButton bOriginal,bFinal,bWhere,bEjecutar,bLimpiar;
    private JLabel lAcciones;

    public pasosTemplate(pasosComponent pasosComponent) {

        this.pasosComponent = pasosComponent;
        sObjGraficos = ObjGraficosService.getService();
        sRecursos = RecursosService.getService();

        this.crearBotones();
        this.crearJlabels();

        setSize(300,200);
        setBackground(Color.red);//color de este panel
        setLayout(null);
        setVisible(true);
    }

    public void crearBotones(){

        bEjecutar = sObjGraficos.construirJButton("Ejecutar", 85, 30, 100,35 , sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(), false);
        bEjecutar.addActionListener(pasosComponent);
        this.add(bEjecutar);

        bLimpiar = sObjGraficos.construirJButton("Limpiar", 85, 70, 100, 35, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(), false);
        bLimpiar.addActionListener(pasosComponent);
        this.add(bLimpiar);

        bOriginal = sObjGraficos.construirJButton("TOriginal", 85, 110, 100, 35, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(), false);
        bOriginal.addActionListener(pasosComponent);
        this.add(bOriginal);

        //bFinal = sObjGraficos.construirJButton("TFinal",85, 122, 100, 25, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(),  false);
        //bFinal.addActionListener(pasosComponent);
        //this.add(bFinal);

        bWhere = sObjGraficos.construirJButton("TProyectada", 85, 150, 100, 35, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(), false);
        bWhere.addActionListener(pasosComponent);
        this.add(bWhere);
        

    }

    public void crearJlabels(){
        lAcciones = sObjGraficos.construirJLabel("Acciones", 100, 5, 200, 30, Color.white, Color.white, sRecursos.getFontTitulo());
        this.add(lAcciones);
    }

    public JButton getBOriginal(){
        return bOriginal;
    }

   // public JButton getBFinal(){
   //     return bFinal;
   // }

    public JButton getBWhere(){
        return bWhere;
    }

}
