/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import EstructurasDeDatosTemporales.DescriptorArchivos;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexis
 */
public class VentanaTabla extends javax.swing.JFrame {
    
    DefaultTableModel mt = new DefaultTableModel();

    /**
     * Creates new form VentanaTabla
     */
    public VentanaTabla() {
        initComponents();
        String ids []= {};
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GenerarConsulta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        TextAreaConsulta = new javax.swing.JTextArea();
        HTrabajoEtiqueta = new javax.swing.JLabel();
        ConsultaEtiqueta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        GenerarConsulta.setText("Generar Consulta");
        GenerarConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerarConsultaActionPerformed(evt);
            }
        });

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        Tabla.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(Tabla);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        TextAreaConsulta.setColumns(20);
        TextAreaConsulta.setRows(5);
        jScrollPane3.setViewportView(TextAreaConsulta);

        HTrabajoEtiqueta.setText("Hoja de Trabajo");

        ConsultaEtiqueta.setText("Resultado de la Consulta");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HTrabajoEtiqueta)
                    .addComponent(ConsultaEtiqueta)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(GenerarConsulta)))
                .addGap(13, 13, 13))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(HTrabajoEtiqueta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(GenerarConsulta)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(ConsultaEtiqueta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GenerarConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerarConsultaActionPerformed
        DescriptorArchivos descriptor = new DescriptorArchivos("tabla.txt");
        try {
            
            String[] atributos = descriptor.vaciarContenido();//se manda a llamar al metodo que va a leer el archivo especificado, y luego
            //se recibe de ese mismo metodo un arreglo de Strings, que contiene los nombres de los atributos de la tabla para que se puedan poner
            //en las columnas de la tabla
            if (atributos == null){
                JOptionPane.showMessageDialog(null, "Error al leer el archivo");
                return;
            }
//            for(int i =0;i<11;i++){ Esto es para comprobar que se leyeron correctamente los atributos de la tabla que se quiera leer
//                System.out.println("En el arreglo de atributos posicion "+i+" esta:"+atributos[i]);
//            }
            mt.setColumnIdentifiers(atributos);
            Tabla.setModel(mt);
            Vector<Vector> grande = new Vector<>(); //defino un vector de vectores
            grande = descriptor.contenido(); //voy a guardar dentro de grande el vector de vectores que se obtiene
            //de leer los datos del .txt
            if (grande == null){
                JOptionPane.showMessageDialog(null, "Error al leer el archivo");
                return;
            }
            for(Vector vectorcitos: grande){ //se va a iterar en este ciclo a traves de todos los vectores que esten dentro
                //del vector "grande", dentro de estos "vectorcitos" se encuentran 3 cadenas (recordando del ejemplo que esta
                //explicado en la funcion contenido de la clase DescriptorArchivos), es necesario regresarlo asi, porque
                //el metodo addRow añade las cadenas dentro de un Vector, por eso se tiene que entrar de esta forma
                //en este caso, el vectorcito contiene 3 cadenas, por lo que el metodo pone la primera cadena
                //en la primera columna, la segunda en la segunda columna y de esa manera.
                mt.addRow(vectorcitos);
            }
            
            //descriptor.contenido(); el metodo retorna un vector de String, que contiene la informacion de las lineas leidas
        } catch (IOException ex) {
            Logger.getLogger(VentanaTabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_GenerarConsultaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaTabla().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ConsultaEtiqueta;
    private javax.swing.JButton GenerarConsulta;
    private javax.swing.JLabel HTrabajoEtiqueta;
    private javax.swing.JTable Tabla;
    private javax.swing.JTextArea TextAreaConsulta;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
