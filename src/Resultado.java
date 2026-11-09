public class Resultado {
    private int fila, columna;
    private Direccion direccion;

    public Resultado(int fila, int columna, Direccion direccion) {
        this.fila = fila; this.columna = columna; this.direccion = direccion;
    }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public Direccion getDireccion() { return direccion; }
}
