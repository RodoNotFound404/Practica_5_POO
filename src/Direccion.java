public enum Direccion {
    ARRIBA(-1, 0), ABAJO(1, 0), IZQUIERDA(0, -1), DERECHA(0, 1),
    DIAG_ARRIBA_IZQ(-1, -1), DIAG_ARRIBA_DER(-1, 1),
    DIAG_ABAJO_IZQ(1, -1), DIAG_ABAJO_DER(1, 1);

    public final int df, dc; // cambio en fila y columna
    Direccion(int df, int dc) { this.df = df; this.dc = dc; }
}
