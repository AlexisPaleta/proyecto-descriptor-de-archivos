package pasosconsulta;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

import Services.ObjGraficosService;
import Services.RecursosService;

public class pasosTemplate extends JPanel{

    private pasosComponent pasosComponent;
    private ObjGraficosService sObjGraficos;
    private RecursosService sRecursos;

    private JButton bOriginal,bFinal,bWhere;

    public pasosTemplate(pasosComponent pasosComponent) {

        this.pasosComponent = pasosComponent;
        sObjGraficos = ObjGraficosService.getService();
        sRecursos = RecursosService.getService();

        this.crearBotones();

        setSize(300,70);
        setBackground(Color.WHITE);//color de este panel
        setLayout(null);
        setVisible(true);
    }

    public void crearBotones(){

        bOriginal = sObjGraficos.construirJButton("TOriginal", 20, 15, 50, 25, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(), false);
        bOriginal.addActionListener(pasosComponent);
        this.add(bOriginal);

        bFinal = sObjGraficos.construirJButton("TFinal",110, 15, 50, 25, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(),  false);
        bFinal.addActionListener(pasosComponent);
        this.add(bFinal);

        bWhere = sObjGraficos.construirJButton("TWhere", 200, 15, 50, 25, sRecursos.getColorBoton(), Color.white, sRecursos.getFontBotones(), sRecursos.getcMano(), sRecursos.getBordeBoton(), false);
        bWhere.addActionListener(pasosComponent);
        this.add(bWhere);
        

    }

    public JButton getBOriginal(){
        return bOriginal;
    }

    public JButton getBFinal(){
        return bFinal;
    }

    public JButton getBWhere(){
        return bWhere;
    }

}
