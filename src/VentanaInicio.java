import javax.swing.*;
import java.awt.*;


public class VentanaInicio extends JFrame {

    private JTextField txtArchivo = new JTextField("sopa.txt", 20);

    public VentanaInicio() {
        super("Sopa de Letras");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Escribe el nombre del archivo de la sopa:", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centro.add(txtArchivo);
        add(centro, BorderLayout.CENTER);

        JButton btnAbrir = new JButton("Abrir sopa");
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        abajo.add(btnAbrir);
        abajo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(abajo, BorderLayout.SOUTH);

        btnAbrir.addActionListener(e -> abrir());
        txtArchivo.addActionListener(e -> abrir());   // Enter tambien abre

        pack();
        setLocationRelativeTo(null);
    }

    private void abrir() {
        String archivo = txtArchivo.getText().trim();
        if (archivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del archivo.");
            return;
        }
        SopaDeLetras sopa = new SopaDeLetras();
        try {
            sopa.cargarDesdeArchivo(archivo);
            new SopaGUI(sopa).setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}