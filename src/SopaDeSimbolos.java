import java.io.*;
import java.util.*;
import java.lang.reflect.Array;

public abstract class SopaDeSimbolos {
    protected char[][] cuadricula;
    protected boolean[][] marcadas;
    protected int filas, columnas;
    protected ArrayList<String> palabras = new ArrayList<>();

    public void cargarDesdeArchivo(String nombre) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        ArrayList<String> lista = new ArrayList<>();
        boolean enPalabras = false;
        try (BufferedReader br = new BufferedReader(new FileReader(nombre))) {
            String l;
            while ((l = br.readLine()) != null) {
                l = l.trim();
                if (l.isEmpty()) continue;
                if (l.equals("---")) {
                    enPalabras = true;
                    continue;
                }
                if (enPalabras) lista.add(l.toUpperCase());
                else lineas.add(l.toUpperCase());
            }
        }
        if (lineas.isEmpty()) throw new IOException("El archivo esta vacio");
        int nf = lineas.size();
        int nc = lineas.get(0).length();
        char[][] nueva = new char[nf][];
        for (int i = 0; i < nf; i++) {
            if (lineas.get(i).length() != nc) {
                throw new IOException("Todos los renglones deben medir lo mismo (renglon " + (i + 1) + ")");
            }
            nueva[i] = lineas.get(i).toCharArray();
        }
        cuadricula = nueva;
        filas = nf;
        columnas = nc;
        palabras = lista;
        iniciarMarcas();
    }

    public void guardarEnArchivo(String nombre) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombre))) {
            for (int f = 0; f < filas; f++) pw.println(new String(cuadricula[f]));
        }
    }

    protected void iniciarMarcas() {
        marcadas = new boolean[filas][columnas];
        for (boolean[] fila : marcadas) Arrays.fill(fila, false);
    }

    public void limpiarMarcas() { iniciarMarcas(); }

    // Resalta las letras de la palabra encontrada
    public void marcar(Resultado r, int longitud) {
        Direccion d = r.getDireccion();
        for (int i = 0; i < longitud; i++) {
            marcadas[r.getFila() - 1 + i * d.df][r.getColumna() - 1 + i * d.dc] = true;
        }
    }

    // Version de consola (en minusculas se ven las letras marcadas)
    public void mostrar() {
        System.out.print("   ");
        for (int c = 0; c < columnas; c++) System.out.print((c + 1) + " ");
        System.out.println();
        for (int f = 0; f < totalRenglones(); f++) {
            System.out.print((f + 1) + "  ");
            for (int c = 0; c < columnas; c++) {
                char ch = cuadricula[f][c];
                System.out.print((marcadas[f][c] ? Character.toLowerCase(ch) : ch) + " ");
            }
            System.out.println();
        }
    }

    // Usa la clase Array (java.lang.reflect)
    public int totalRenglones() { return Array.getLength(cuadricula); }

    public boolean estaCargada() { return cuadricula != null; }

    // Getters que usa la GUI
    public ArrayList<String> getPalabras() { return palabras; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public char getLetra(int f, int c) { return cuadricula[f][c]; }
    public boolean estaMarcada(int f, int c) { return marcadas[f][c]; }

    public abstract Resultado buscar(String palabra);
}