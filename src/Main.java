import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SopaDeLetras sopa = new SopaDeLetras();
        ArrayList<String> encontradas = new ArrayList<>();
        int opcion = 0;

        while (opcion != 5) {
            System.out.println("\n=== SOPA DE LETRAS ===");
            System.out.println("1. Cargar sopa desde archivo");
            System.out.println("2. Generar sopa aleatoria");
            System.out.println("3. Buscar palabra");
            System.out.println("4. Mostrar sopa");
            System.out.println("5. Salir");
            System.out.print("Opcion: ");
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del archivo: ");
                    try {
                        sopa.cargarDesdeArchivo(sc.nextLine().trim());
                        encontradas.clear();
                        sopa.mostrar();
                    } catch (Exception e) {
                        System.out.println("No se pudo leer el archivo: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Renglones: ");
                        int filas = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Columnas: ");
                        int cols = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Palabras separadas por coma: ");
                        ArrayList<String> palabras = new ArrayList<>();
                        for (String p : sc.nextLine().split(",")) {
                            if (!p.trim().isEmpty()) palabras.add(p.trim());
                        }
                        if (sopa.generar(filas, cols, palabras)) {
                            encontradas.clear();
                            sopa.mostrar();
                            System.out.print("Nombre del archivo para guardarla: ");
                            sopa.guardarEnArchivo(sc.nextLine().trim());
                            System.out.println("Sopa guardada.");
                        } else {
                            System.out.println("No cupieron las palabras. Usa una cuadricula mas grande.");
                        }
                    } catch (Exception e) {
                        System.out.println("Datos invalidos: " + e.getMessage());
                    }
                    break;

                case 3:
                    if (!sopa.estaCargada()) {
                        System.out.println("Primero carga o genera una sopa.");
                        break;
                    }
                    System.out.print("Palabra a buscar: ");
                    String palabra = sc.nextLine().trim();
                    Resultado r = sopa.buscar(palabra);
                    sopa.limpiarMarcas();
                    if (r == null) {
                        System.out.println("La palabra NO esta en la cuadricula.");
                    } else {
                        sopa.marcar(r, palabra.length());
                        System.out.println("Encontrada en renglon " + r.getFila()
                                + ", columna " + r.getColumna() + ", direccion " + r.getDireccion());
                        if (!encontradas.contains(palabra.toUpperCase())) {
                            encontradas.add(palabra.toUpperCase());
                        }
                    }
                    sopa.mostrar();
                    System.out.println("Palabras encontradas (" + encontradas.size() + "): " + encontradas);
                    break;

                case 4:
                    if (sopa.estaCargada()) sopa.mostrar();
                    else System.out.println("Primero carga o genera una sopa.");
                    break;

                case 5:
                    System.out.println("Adios.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
}
