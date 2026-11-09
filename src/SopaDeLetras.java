import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SopaDeLetras extends SopaDeSimbolos {

    @Override
    public Resultado buscar(String palabra) {
        char[] buscada = palabra.toUpperCase().toCharArray();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                for (Direccion d : Direccion.values()) {
                    if (coincide(buscada, f, c, d)) {
                        return new Resultado(f + 1, c + 1, d);
                    }
                }
            }
        }
        return null;
    }

    private boolean coincide(char[] buscada, int f, int c, Direccion d) {
        char[] leida = new char[buscada.length];
        Arrays.fill(leida, ' ');
        for (int i = 0; i < buscada.length; i++) {
            int nf = f + i * d.df, nc = c + i * d.dc;
            if (nf < 0 || nf >= filas || nc < 0 || nc >= columnas) return false;
            leida[i] = cuadricula[nf][nc];
        }
        return Arrays.equals(leida, buscada);
    }

    // Genera una sopa aleatoria escondiendo las palabras. Regresa false si no cupieron.
    public boolean generar(int filas, int columnas, ArrayList<String> palabras) {
        if (filas <= 0 || columnas <= 0) return false;
        char[][] respaldo = cuadricula;
        int filasAnt = this.filas, colsAnt = this.columnas;

        this.filas = filas;
        this.columnas = columnas;
        cuadricula = new char[filas][columnas];
        for (char[] fila : cuadricula) Arrays.fill(fila, ' ');

        Random rnd = new Random();
        Direccion[] dirs = Direccion.values();

        for (String p : palabras) {
            char[] pal = p.toUpperCase().toCharArray();
            boolean colocada = false;
            for (int intento = 0; intento < 300 && !colocada; intento++) {
                Direccion d = dirs[rnd.nextInt(dirs.length)];
                int f = rnd.nextInt(filas), c = rnd.nextInt(columnas);
                if (cabe(pal, f, c, d)) {
                    for (int i = 0; i < pal.length; i++) {
                        cuadricula[f + i * d.df][c + i * d.dc] = pal[i];
                    }
                    colocada = true;
                }
            }
            if (!colocada) {
                // no cupo: se regresa la sopa anterior sin cambios
                cuadricula = respaldo;
                this.filas = filasAnt;
                this.columnas = colsAnt;
                return false;
            }
        }


        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (cuadricula[f][c] == ' ') {
                    cuadricula[f][c] = (char) ('A' + rnd.nextInt(26));
                }
            }
        }
        iniciarMarcas();
        return true;
    }

    private boolean cabe(char[] pal, int f, int c, Direccion d) {
        for (int i = 0; i < pal.length; i++) {
            int nf = f + i * d.df, nc = c + i * d.dc;
            if (nf < 0 || nf >= filas || nc < 0 || nc >= columnas) return false;
            if (cuadricula[nf][nc] != ' ' && cuadricula[nf][nc] != pal[i]) return false;
        }
        return true;
    }
}
