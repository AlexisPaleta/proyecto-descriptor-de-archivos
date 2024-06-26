package Vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import Services.ObjGraficosService;
import Services.RecursosService;

import java.awt.Color;



public class ConsultasTemplate extends JFrame{

    private ObjGraficosService sObjGraficos;
    private RecursosService sRecursos;

    private JPanel pDebajo, pIzquierda,pDerecha,pOpciones;
    private JLabel lTituloVentana;
    private JTextArea tAreaConsulta;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable Tabla;
    private DefaultTableModel mt ;

    private ConsultasComponent consultasComponent;
    

    public ConsultasTemplate(ConsultasComponent consultasComponent) {
        super("Generador de consultas");
        
        this.consultasComponent = consultasComponent;
        sObjGraficos = ObjGraficosService.getService();
        sRecursos = RecursosService.getService();

        mt = new DefaultTableModel();
        

        this.crearJpanels();

        this.crearJlabels();

        this.crearJtextAreas();
        
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,500);
        setLocationRelativeTo(this);
        setVisible(true);

        this.crearJTable();

    }

    public void crearJTable(){


        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Tabla de consultas"
            }
        ));
        jScrollPane1.setViewportView(Tabla);
        jScrollPane1.setBounds(0, 0, 1000, 300);
        pDebajo.add(jScrollPane1);
        

        //tabla = new JTable(mt);
        
    }

    private void crearJpanels(){
        pDebajo = sObjGraficos.construirJPanel(0, 200, 1000, 300, null, null);
        this.add(pDebajo);

        pIzquierda = sObjGraficos.construirJPanel(0, 0, 700, 200, sRecursos.getColorFondo(), null);
        this.add(pIzquierda);

        pDerecha = sObjGraficos.construirJPanel(700, 0, 300, 200, Color.red, null);
        this.add(pDerecha);

        //pOpciones = sObjGraficos.construirJPanel(700, 130, 300, 70, null, null);
        //this.add(pOpciones);
    }

    private void crearJlabels(){
        lTituloVentana = sObjGraficos.construirJLabel("Generador de consultas", 25, 10, 200, 30, Color.white, Color.white, sRecursos.getFontTitulo());
        pIzquierda.add(lTituloVentana);
        
        //lTablaProceso = sObjGraficos.construirJLabel("Tabla de proceso", 25, 10, 200, 30, Color.white, Color.white, sRecursos.getFontTitulo());
        //pDebajo.add(lTablaProceso);

        //lAcciones = sObjGraficos.construirJLabel("Acciones", 110, 10, 200, 30, Color.white, Color.white, sRecursos.getFontTitulo());
        //pDerecha.add(lAcciones);

    }

    private void crearJtextAreas(){
        tAreaConsulta = sObjGraficos.construirJTextArea("SELECT  employee_id, first_name,salary  ,manager_id FROM tabla WHERE salary between 2500 AND 15000;", 25, 40, 650, 145, Color.white, Color.black, sRecursos.getFontArial(), true);
        pIzquierda.add(tAreaConsulta);

    }


    public JPanel getpDebajo() {
        return pDebajo;
    }

    public JPanel getpIzquierda() {
        return pIzquierda;
    }

    public JPanel getpDerecha() {
        return pDerecha;
    }

    public JPanel getpOpciones() {
        return pOpciones;
    }


    public JTextArea getTAreaConsulta() {
        return tAreaConsulta;
    }

    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public javax.swing.JTable getTabla() {
        return Tabla;
    }

    public DefaultTableModel getMt() {
        return mt;
    }


    public static void main(String[] args) {
        Runnable runApplication = new Runnable() {
            public void run() {
                ConsultasComponent consulta = new ConsultasComponent();
                consulta.getClass();
            }
        };
        SwingUtilities.invokeLater(runApplication);
    }

}
