package org.example;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.example.logica.Logica;
import org.example.logica.Logica2;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("exa2");
            MongoCollection<Document> librosCollection = database.getCollection("libro");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Insertar libro");
                System.out.println("2. Buscar libros por título");
                System.out.println("3. Buscar libros por género");
                System.out.println("4. Eliminar libro");
                System.out.println("5. Salir");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea después de nextInt()

                switch (opcion) {
                    case 1:
                        Logica2 logica = new Logica2();
                        logica.addBook(librosCollection);
                    //    logica.anyadirLibro(librosCollection);
                    //    insertarLibro(librosCollection, scanner);
                        break;
                    case 2:
                        buscarPorTitulo(librosCollection, scanner);
                        break;
                    case 3:
                        buscarPorGenero(librosCollection, scanner);
                        break;
                    case 4:
                        eliminarLibro(librosCollection, scanner);
                        break;
                    case 5:
                        return; // Salir del programa
                    default:
                        System.out.println("Opción inválida.");
                }
            }
        }
    }

    private static void insertarLibro(MongoCollection<Document> collection, Scanner scanner) {
        Document libro = new Document();

        System.out.print("Título del libro: ");
        libro.append("titulo", scanner.nextLine());

        System.out.print("Género del libro: ");
        libro.append("genero", scanner.nextLine());

        boolean agregarCampos = true;
        while (agregarCampos) {
            System.out.print("¿Desea agregar un campo adicional? (si/no): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("si")) {
                System.out.print("Nombre del campo: ");
                String nombreCampo = scanner.nextLine();

                System.out.print("Tipo del campo (string, int, object, lista): ");
                String tipoCampo = scanner.nextLine();

                System.out.print("Valor del campo: ");
                String valorCampo = scanner.nextLine();

                // Agregar el campo al documento del libro
                agregarCampo(libro, nombreCampo, tipoCampo, valorCampo, scanner);
            } else if (respuesta.equalsIgnoreCase("no")) {
                agregarCampos = false;
            } else {
                System.out.println("Respuesta no válida. Por favor, responda 'si' o 'no'.");
            }
        }

        collection.insertOne(libro);
        System.out.println("Libro insertado correctamente.");
    }
    private static void agregarCampo(Document libro, String nombreCampo, String tipoCampo, String valorCampo, Scanner scanner) {
        switch (tipoCampo.toLowerCase()) {
            case "string":
                libro.append(nombreCampo, valorCampo);
                break;
            case "int":
                libro.append(nombreCampo, Integer.parseInt(valorCampo));
                break;
            case "object":
                Document objeto = new Document();
                agregarCamposObjeto(objeto, scanner);
                libro.append(nombreCampo, objeto);
                break;
            case "lista":
                List<String> valoresLista = Arrays.asList(valorCampo.split(","));
                libro.append(nombreCampo, valoresLista);
                break;
            default:
                System.out.println("Tipo de campo no válido. Los tipos válidos son: string, int, object, lista.");
        }
    }

    private static void agregarCamposObjeto(Document objeto, Scanner scanner) {
        boolean agregarCampos = true;
        while (agregarCampos) {
            System.out.print("Nombre del campo del objeto: ");
            String nombreCampo = scanner.nextLine();

            System.out.print("Tipo del campo (string, int, object, lista): ");
            String tipoCampo = scanner.nextLine();

            System.out.print("Valor del campo: ");
            String valorCampo = scanner.nextLine();

            agregarCampo(objeto, nombreCampo, tipoCampo, valorCampo, scanner); // Se añade el escáner como argumento

            System.out.print("¿Desea agregar otro campo al objeto? (si/no): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("no")) {
                agregarCampos = false;
            }
        }
    }

    private static void buscarPorTitulo(MongoCollection<Document> collection, Scanner scanner) {
        System.out.print("Ingrese el título del libro a buscar: ");
        String titulo = scanner.nextLine();

        Document query = new Document("titulo", titulo);
        FindIterable<Document> libros = collection.find(query);

        System.out.println("Resultados de la búsqueda:");
        for (Document libro : libros) {
            System.out.println(libro.toJson());
        }
    }

    private static void buscarPorGenero(MongoCollection<Document> collection, Scanner scanner) {
        System.out.print("Ingrese el género del libro a buscar: ");
        String genero = scanner.nextLine();

        Document query = new Document("genero", genero);
        FindIterable<Document> libros = collection.find(query);

        System.out.println("Resultados de la búsqueda:");
        for (Document libro : libros) {
            System.out.println(libro.toJson());
        }
    }

    private static void eliminarLibro(MongoCollection<Document> collection, Scanner scanner) {
        System.out.print("Ingrese el título del libro a eliminar: ");
        String titulo = scanner.nextLine();

        Document query = new Document("titulo", titulo);
        DeleteResult result = collection.deleteOne(query);

        if (result.getDeletedCount() > 0) {
            System.out.println("Libro eliminado correctamente.");
        } else {
            System.out.println("No se encontró ningún libro con ese título.");
        }
    }
}
