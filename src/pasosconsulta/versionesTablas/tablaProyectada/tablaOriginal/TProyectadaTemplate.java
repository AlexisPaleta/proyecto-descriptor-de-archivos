package pasosconsulta.versionesTablas.tablaProyectada.tablaOriginal;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class TProyectadaTemplate extends JPanel{

    private TProyectadaComponent tProyectadaComponent;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable Tabla;
    private DefaultTableModel mt;

    public TProyectadaTemplate(TProyectadaComponent tProyectadaComponent) {
        this.tProyectadaComponent = tProyectadaComponent;

        mt = new DefaultTableModel();
        

        this.crearJTable();
        setSize(1000,300);
        setLayout(null);
        setVisible(true);
    }


    public void crearJTable(){


        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.Tabla = new javax.swing.JTable();

        this.Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {//tabla vacia
            },
            new String [] {
                "Tabla de consultas"
            }
        ));
        
        
        this.jScrollPane1.setViewportView(Tabla);
        this.jScrollPane1.setBounds(0, 0, 1000, 300);
        this.add(jScrollPane1);
        

        //tabla = new JTable(mt);
        
        
    }

    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public javax.swing.JTable getTabla() {
        return this.Tabla;
    }

    public DefaultTableModel getMt() {
        return this.mt;
    }

}
