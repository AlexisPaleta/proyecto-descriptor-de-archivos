package pasosconsulta.versionesTablas.tablaOriginal;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class TOriginalTemplate extends JPanel{

    private TOriginalComponent tOriginalComponent;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable Tabla;
    private DefaultTableModel mt = new DefaultTableModel();

    public TOriginalTemplate(TOriginalComponent tOriginalComponent) {
        this.tOriginalComponent = tOriginalComponent;

        this.crearJTable();
        setSize(1000,300);
        setLayout(null);
        setVisible(true);
    }


    public void crearJTable(){

        jScrollPane1 = null;
        Tabla = null;

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Tabla de consultas"
            }
        ));
        
        
        jScrollPane1.setViewportView(Tabla);
        jScrollPane1.setBounds(0, 0, 1000, 300);
        this.add(jScrollPane1);
        

        //tabla = new JTable(mt);
        Tabla.setBounds(0, 200, 1000, 300);
        
        
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

}
