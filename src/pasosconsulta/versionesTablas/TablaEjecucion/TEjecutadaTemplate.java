package pasosconsulta.versionesTablas.TablaEjecucion;

import javax.swing.JPanel;

public class TEjecutadaTemplate extends JPanel{

    private TEjecutadaComponent tEjecutadaComponent;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable Tabla;
    private javax.swing.table.DefaultTableModel mt;
    
    public TEjecutadaTemplate(TEjecutadaComponent tEjecutadaComponent) {
        this.tEjecutadaComponent = tEjecutadaComponent;

        mt = new javax.swing.table.DefaultTableModel();

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

    }

    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public javax.swing.JTable getTabla() {
        return Tabla;
    }


    public javax.swing.table.DefaultTableModel getMT() {
        return mt;
    }

}
