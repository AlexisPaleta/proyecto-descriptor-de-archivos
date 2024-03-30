package Services;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class ObjGraficosService {

    private JPanel panel;
    private JLabel label;
    private JTextArea textArea;
    private JButton boton;

    static private ObjGraficosService servicio;

    public static ObjGraficosService getService() {
        if (servicio == null) {
            servicio = new ObjGraficosService();
        }
        return servicio;
    }

    private ObjGraficosService() {} // Constructor vacio

    

    public JPanel construirJPanel(int x, int y, int ancho, int alto, Color colorFondo, Border borde) {
        panel = new JPanel();
        panel.setBounds(x, y, ancho, alto);
        panel.setBackground(colorFondo);
        panel.setBorder(borde);
        panel.setLayout(null);
        panel.setBackground(colorFondo);
        panel.setBorder(borde);
        return panel;
    }

    public JLabel construirJLabel(String texto, int x, int y, int ancho, int alto, Color colorFondo, Color colorFuente,Font fuente) {
        label = new JLabel(texto);
        label.setBounds(x, y, ancho, alto);
        label.setForeground(colorFuente);
        label.setFont(fuente);
        label.setBackground(colorFondo);
        return label;
    }

    public JTextArea construirJTextArea(String texto, int x, int y, int ancho, int alto, Color colorFondo, Color colorFuente, Font fuente , boolean wrap) {
        textArea = new JTextArea(texto);
        textArea.setBounds(x, y, ancho, alto);
        textArea.setForeground(colorFuente);
        textArea.setFont(fuente);
        textArea.setBackground(colorFondo);
        textArea.setLineWrap(wrap);
        return textArea;
    }

    public JButton construirJButton(String texto, int x, int y, int ancho, int alto, Color colorBoton, Color colorFuente, Font fuente, Cursor cursor, Border borde, boolean focusable) {
        boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setForeground(colorFuente);
        boton.setFont(fuente);
        boton.setBackground(colorBoton);
        boton.setCursor(cursor);
        boton.setBorder(borde);
        boton.setFocusable(focusable);
        return boton;
    }


    

}
