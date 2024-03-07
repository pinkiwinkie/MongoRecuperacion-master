package org.example.logica;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Logica2 {
    public void addBook(MongoCollection<Document> collection){
        Scanner sc = new Scanner(System.in);
        Document libro = new Document();
        boolean salir = false;

        System.out.print("Pon titulo ");
        libro.append("titulo", sc.nextLine());

        System.out.print("Pon genero ");
        libro.append("genero", sc.nextLine());

        do{
            System.out.print("¿Agregar campo adicional? (s/salir): ");
            String respuesta = sc.nextLine();
            if(respuesta.equals("s")) {
                System.out.print("Tipo del campo (string, int, object, lista): ");
                String tipoCampo = sc.nextLine();

                if (tipoCampo.equals("lista")) {
                    addList(sc, libro);
                } else if (tipoCampo.equals("object")) {
                    addObject(sc, libro);
                }
            }  else if (respuesta.equals("salir")) {
                salir=true;
            }
        } while (!salir);
        collection.insertOne(libro);
    }


    private static void addList(Scanner sc, Document libro){
        boolean salir = false;
        do{
            System.out.print("Nombre del campo: ");
            String nombreCampo = sc.nextLine();

            System.out.print("Valor del campo: ");
            String valorCampo = sc.nextLine();
            List<String> valoresLista = new ArrayList<>();
            boolean agregarValores = true;
            valoresLista.add(valorCampo);
            while (agregarValores) {
                System.out.print("Ingrese un valor para la lista (o escriba fin para terminar): ");
                String valorLista = sc.nextLine();
                if (!valorLista.equals("fin")) {
                    valoresLista.add(valorLista);
                } else {
                    agregarValores = false;
                }
            }
            libro.append(nombreCampo, valoresLista);
            salir = true;
        }while (!salir);
    }

    private static void addObject(Scanner sc, Document libro) {
        Document libro2 = new Document();
        boolean agregarValores = true;
        System.out.print("Nombre del campo del objeto: ");
        String nombreCampo = sc.nextLine();
        System.out.print("Valor del campo: ");
        String valorCampo = sc.nextLine();
        int count = 0;
        do {
            libro2.append(nombreCampo, valorCampo);
            System.out.print("¿Desea agregar otro campo al objeto  "+ count + " ?(si/no): ");
            String answer = sc.nextLine();

            if (answer.equals("no")) {
                agregarValores = false;
            } else {
                boolean salir = false;
                do {
                    System.out.print("Tipo del campo (string, int, object, lista): ");
                    String type = sc.nextLine();
                    if (type.equals("lista")) {
                        addList(sc, libro);
                        salir = true;
                    } else if (type.equals("object")) {
                        count++;
                        addObject(sc, libro2);
                        salir = true;
                    }
                } while (!salir);
            }
            libro.append(nombreCampo, libro2);
        } while (agregarValores);
    }

}
