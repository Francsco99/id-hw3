package it.uniroma3.hw3;

import java.util.Scanner;

public class Main {

    final static String TABLE_PATH = System.getProperty("user.dir")+"/tables.json";
    final static String INDEX_PATH = System.getProperty("user.dir")+"/index";
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int scelta;

        do {
            stampaMenu();
            System.out.print("Scegli un'opzione: ");
            scelta = input.nextInt();

            switch (scelta) {
            case 1:
                TableIndexer tr = new TableIndexer();
                tr.tableIndexer(TABLE_PATH,INDEX_PATH);
                break;
            case 2:
                System.out.println("Crea l'indice di file json presenti in una directory - TO DO");
                break;
            case 3:
                TableMerge merge = new TableMerge(INDEX_PATH);
                merge.countSet("tabella_1.json");
                merge.statistiche();
                break;
            case 0:
                System.out.println("Uscita dal programma.");
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
            }

        } while (scelta != 0);

        input.close();

    }

    public static void stampaMenu() {
        System.out.println("Menu di scelta:");
        System.out.println("1. Crea l'indice dal file \"tables.json\"");
        System.out.println("2. Crea l'indice di file json presenti in una directory");
        System.out.println("3. Merge Query");
        System.out.println("0. Esci");
    }
}