package org.example.logica;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Logica {

    public void anyadirLibro(MongoCollection<Document> collection){

        Scanner sc = new Scanner(System.in);
        Document libro = new Document();
        boolean salir = false;

        do {
            System.out.print("Pon titulo ");
            libro.append("titulo", sc.nextLine());

            System.out.print("Pon genero ");
            libro.append("genero", sc.nextLine());

            System.out.print("¿Agregar campo adicional? (s/n/salir): ");
            String respuesta = sc.nextLine();
            if(respuesta.equals("s")) {
                System.out.print("Tipo del campo (string, int, object, lista): ");
                String tipoCampo = sc.nextLine();

                if (tipoCampo.equals("lista")) {
                    salir = isSalir(sc, salir, libro);
                } else if (tipoCampo.equals("object")) {
                    boolean agregarValores = true;
                    while (agregarValores){
                        System.out.print("Nombre del campo del objeto: ");
                        String nombreCampo = sc.nextLine();
                        System.out.print("Tipo del campo (string, int, object, lista): ");
                        String type = sc.nextLine();

                        System.out.print("Valor del campo: ");
                        String valorCampo = sc.nextLine();
                        libro.append(nombreCampo, valorCampo);
                        System.out.print("¿Desea agregar otro campo al objeto? (si/no): ");
                        String answer = sc.nextLine();
                        if (answer.equalsIgnoreCase("no")) {
                            agregarValores = false;
                        }
                    }
                }
            } else if (respuesta.equals("salir")) {
                salir=true;
            }

        } while (!salir);

        collection.insertOne(libro);
    }

    private static boolean isSalir(Scanner sc, boolean salir, Document libro) {
        System.out.print("Nombre del campo: ");
        String nombreCampo = sc.nextLine();

        System.out.print("Valor del campo: ");
        String valorCampo = sc.nextLine();
        List<String> valoresLista = new ArrayList<>();
        boolean agregarValores = true;
        valoresLista.add(valorCampo);
        while (agregarValores) {
            System.out.print("Ingrese un valor para la lista (o escriba 'fin' para terminar): ");
            String valorLista = sc.nextLine();
            if (!valorLista.equalsIgnoreCase("fin")) {
                valoresLista.add(valorLista);
            } else {
                agregarValores = false;
                salir = true;
            }
        }
        libro.append(nombreCampo, valoresLista);
        return salir;
    }


}
