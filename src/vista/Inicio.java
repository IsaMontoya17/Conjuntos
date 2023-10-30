package vista;

import bean.Hotel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inicio {

    public static void main(String[] args) {
        ArrayList<Hotel> hoteles = new ArrayList<>();
        String zonaElegida = "";
        Scanner scanner = new Scanner(System.in);
        cargarHotelesDesdeArchivo(hoteles);

        int opc = 0;
        do {
            System.out.println("-----------------------------------------------");
            System.out.println("| Hoteles |");
            System.out.println("-----------------------------------------------");
            System.out.println("| Menu Principal |");
            System.out.println("-----------------------------------------------");
            System.out.println("| 1. Listar y/o filtrar hoteles |");
            System.out.println("| 2. Ingresar un hotel |");
            System.out.println("| 3. Salir |");
            System.out.println("-----------------------------------------------");
            opc = scanner.nextInt();

            switch (opc) {
                case 1:

                    mostrarHoteles(hoteles);

                    zonaElegida = obtenerZonaDeseada();

                    HashSet<Hotel> hotelesZonaElegida = obtenerHotelesDeZona(hoteles, zonaElegida);
                    ArrayList<Hotel> hotelesOrdenadosPorPrecio = ordenarHotelesPorPrecio(hotelesZonaElegida);

                    System.out.println("\nHoteles en la zona " + zonaElegida + " ordenados por precio:");
                    mostrarHoteles(hotelesOrdenadosPorPrecio);

                    break;
                case 2:
                    agregarNuevoHotel(hoteles);
                    break;
            }

        } while (opc != 3);

        cargarHotelesDesdeArchivo(hoteles);

        System.out.println("Conjunto de hoteles sin ordenar:");
        mostrarHoteles(hoteles);

        zonaElegida = obtenerZonaDeseada();

        HashSet<Hotel> hotelesZonaElegida = obtenerHotelesDeZona(hoteles, zonaElegida);
        ArrayList<Hotel> hotelesOrdenadosPorPrecio = ordenarHotelesPorPrecio(hotelesZonaElegida);

        System.out.println("\nHoteles en la zona " + zonaElegida + " ordenados por precio:");
        mostrarHoteles(hotelesOrdenadosPorPrecio);

    }//CIERRE DEL MAIN

    private static void cargarHotelesDesdeArchivo(ArrayList<Hotel> hoteles) {
        
        try (BufferedReader br = new BufferedReader(new FileReader("./datos/hoteles.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    int idHotel = Integer.parseInt(parts[0]);
                    String nombre = parts[1];
                    String[] zonas = parts[2].split(",");
                    int precio = Integer.parseInt(parts[3]);

                    for (String zona : zonas) {
                        zona = zona.trim();
                        Hotel hotel = new Hotel();
                        hotel.setIdHotel(idHotel);
                        hotel.setNombre(nombre);
                        hotel.setZona(zona);
                        hotel.setPrecio(precio);

                        hoteles.add(hotel);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }//CIERRE DEL METODO

    private static void mostrarHoteles(ArrayList<Hotel> hoteles) {
        
        for (Hotel hotel : hoteles) {
            System.out.println(hotel.getIdHotel() + ": " + hotel.getNombre() + " - " + hotel.getZona() + " - " + hotel.getPrecio() + " euros");
        }
        
    }//CIERRE DEL METODO

    private static String obtenerZonaDeseada() {

        String zonaElegida = "";

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese la zona deseada (Playa, Montaña, Rural): ");
            zonaElegida = scanner.nextLine().toLowerCase();

            if (validarZona2(zonaElegida)) {
                break;
            } else {
                System.out.println("Zona no válida. Por favor, ingrese una zona válida.");
            }
        }

        return zonaElegida;
        
    }//CIERRE DEL METODO

    private static HashSet<Hotel> obtenerHotelesDeZona(ArrayList<Hotel> hoteles, String zonaElegida) {
        
        HashSet<Hotel> hotelesZonaElegida = new HashSet<>();
        String patron = ".*\\b" + Pattern.quote(zonaElegida) + "\\b.*";

        for (Hotel hotel : hoteles) {
            if (Pattern.compile(patron, Pattern.CASE_INSENSITIVE).matcher(hotel.getZona()).find()) {
                hotelesZonaElegida.add(hotel);
            }
        }
        return hotelesZonaElegida;
        
    }//CIERRE DEL METODO

    private static ArrayList<Hotel> ordenarHotelesPorPrecio(HashSet<Hotel> hoteles) {
        
        ArrayList<Hotel> hotelesOrdenados = new ArrayList<>(hoteles);
        Collections.sort(hotelesOrdenados, (h1, h2) -> Integer.compare(h1.getPrecio(), h2.getPrecio()));
        
        return hotelesOrdenados;
        
    }//CIERRE DEL METODO

    private static boolean validarZona(String zona) {
        
        return zona.equalsIgnoreCase("Playa") || zona.equalsIgnoreCase("Montaña") || zona.equalsIgnoreCase("Rural");
        
    }//CIERRE DEL METODO

    private static void agregarNuevoHotel(ArrayList<Hotel> hoteles) {
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los detalles del nuevo hotel:");
        System.out.print("ID del hotel: ");
        int idHotel = Integer.parseInt(scanner.nextLine());

        System.out.print("Nombre del hotel: ");
        String nombre = scanner.nextLine();

        System.out.print("Zona del hotel (playa, montaña, rural): ");
        String zona = scanner.nextLine();
        while (!validarZona2(zona)) {
            System.out.println("Zona no válida. Por favor, ingrese una zona válida.");
            zona = scanner.nextLine();
        }

        System.out.print("Precio del hotel (entre 40 y 150 euros): ");
        int precio = Integer.parseInt(scanner.nextLine());
        while (precio < 40 || precio > 150) {
            System.out.println("Precio fuera del rango válido. Ingrese un precio entre 40 y 150 euros.");
            precio = Integer.parseInt(scanner.nextLine());
        }

        Hotel nuevoHotel = new Hotel();
        nuevoHotel.setIdHotel(idHotel);
        nuevoHotel.setNombre(nombre);
        nuevoHotel.setZona(zona);
        nuevoHotel.setPrecio(precio);

        hoteles.add(nuevoHotel);
        System.out.println("Nuevo hotel agregado con éxito.");
        
    }//CIERRE DEL METODO

    private static boolean validarZona2(String zona) {

        String patron = "^(?i)((playa|rural|montaña)(-(playa|rural|montaña)){0,2})$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(zona);

        return matcher.matches();
        
    }//CIERRE DEL METODO

}//CIERRE DE LA CLASE
