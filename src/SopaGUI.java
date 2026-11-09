import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

// Ventana principal: muestra la sopa ya cargada y permite buscar palabras
public class SopaGUI extends JFrame {

    private SopaDeLetras sopa;
    private ArrayList<String> encontradas = new ArrayList<>();
    private boolean ganado = false;

    private JPanel panelCuadricula = new JPanel();
    private JTextField txtPalabra = new JTextField(14);
    private JLabel lblGanador = new JLabel(" ", SwingConstants.CENTER);
    private JLabel lblResultado = new JLabel("Escribe una palabra y presiona Buscar.");
    private JLabel lblEncontradas = new JLabel("Palabras encontradas (0):");
    private JLabel lblFaltan = new JLabel(" ");

    private static final Color VERDE = new Color(0, 170, 0);
    private static final Color VERDE_CLARO = new Color(210, 255, 210);

    public SopaGUI(SopaDeLetras sopa) {
        super("Sopa de Letras");
        this.sopa = sopa;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));


        lblGanador.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblGanador.setForeground(VERDE);
        lblGanador.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        add(lblGanador, BorderLayout.NORTH);


        JPanel contenedor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedor.add(panelCuadricula);
        add(new JScrollPane(contenedor), BorderLayout.CENTER);


        JPanel abajo = new JPanel(new GridLayout(4, 1, 4, 4));
        JPanel filaBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar marcas");
        filaBuscar.add(new JLabel("Palabra:"));
        filaBuscar.add(txtPalabra);
        filaBuscar.add(btnBuscar);
        filaBuscar.add(btnLimpiar);
        abajo.add(filaBuscar);
        abajo.add(lblResultado);
        abajo.add(lblEncontradas);
        abajo.add(lblFaltan);
        abajo.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(abajo, BorderLayout.SOUTH);


        btnBuscar.addActionListener(e -> buscar());
        txtPalabra.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> limpiar());

        dibujar();
        actualizarEstado();
        setSize(660, 740);
        setLocationRelativeTo(null);
    }

    private void buscar() {
        String palabra = txtPalabra.getText().trim();
        if (palabra.isEmpty()) return;

        boolean acabaDeGanar = false;
        Resultado r = sopa.buscar(palabra);
        if (r == null) {
            lblResultado.setText("La palabra \"" + palabra + "\" NO esta en la cuadricula.");
        } else {

            sopa.marcar(r, palabra.length());
            lblResultado.setText("Encontrada en renglon " + r.getFila() + ", columna "
                    + r.getColumna() + ", direccion " + r.getDireccion());
            if (!encontradas.contains(palabra.toUpperCase())) {
                encontradas.add(palabra.toUpperCase());
            }
            actualizarEstado();
            dibujar();
            if (terminada() && !ganado) {
                ganado = true;
                acabaDeGanar = true;
                lblGanador.setText("FELICIDADES, GANASTE!");
            }
        }
        txtPalabra.setText("");
        txtPalabra.requestFocus();

        if (acabaDeGanar) {
            JOptionPane.showMessageDialog(this,
                    "Felicidades, encontraste las " + sopa.getPalabras().size() + " palabras!",
                    "Ganaste", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiar() {
        sopa.limpiarMarcas();
        encontradas.clear();
        ganado = false;
        lblGanador.setText(" ");
        lblResultado.setText("Marcas borradas.");
        actualizarEstado();
        dibujar();
    }

    private boolean terminada() {
        ArrayList<String> lista = sopa.getPalabras();
        return !lista.isEmpty() && encontradas.containsAll(lista);
    }

    private void actualizarEstado() {
        lblEncontradas.setText("Palabras encontradas (" + encontradas.size() + "): "
                + String.join(", ", encontradas));

        ArrayList<String> lista = sopa.getPalabras();
        if (lista.isEmpty()) {
            lblFaltan.setText(" ");
            return;
        }
        ArrayList<String> faltan = new ArrayList<>();
        for (String p : lista) {
            if (!encontradas.contains(p)) faltan.add(p);
        }
        if (faltan.isEmpty()) {
            lblFaltan.setText("Ya encontraste todas las palabras.");
        } else {
            lblFaltan.setText("Faltan (" + faltan.size() + " de " + lista.size() + "): "
                    + String.join(", ", faltan));
        }
    }

    private JLabel encabezado(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(Color.GRAY);
        return l;
    }

    private void dibujar() {
        panelCuadricula.removeAll();
        int f = sopa.getFilas(), c = sopa.getColumnas();
        panelCuadricula.setLayout(new GridLayout(f + 1, c + 1, 2, 2));
        panelCuadricula.setPreferredSize(new Dimension((c + 1) * 38, (f + 1) * 38));

        panelCuadricula.add(new JLabel(""));
        for (int j = 0; j < c; j++) panelCuadricula.add(encabezado(String.valueOf(j + 1)));

        for (int i = 0; i < f; i++) {
            panelCuadricula.add(encabezado(String.valueOf(i + 1)));
            for (int j = 0; j < c; j++) {
                JLabel celda = new JLabel(String.valueOf(sopa.getLetra(i, j)), SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setFont(new Font("Monospaced", Font.BOLD, 18));
                celda.setForeground(Color.BLACK);
                if (sopa.estaMarcada(i, j)) {
                    celda.setBorder(new LineBorder(VERDE, 3));
                    celda.setBackground(VERDE_CLARO);
                } else {
                    celda.setBorder(new LineBorder(Color.LIGHT_GRAY));
                    celda.setBackground(Color.WHITE);
                }
                panelCuadricula.add(celda);
            }
        }
        panelCuadricula.revalidate();
        panelCuadricula.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }
}