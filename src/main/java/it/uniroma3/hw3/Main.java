package it.uniroma3.hw3;

public class Main {

    final static String TABLE_PATH = "C:/Users/franc/Desktop/hw3_dati/tables/tabella.json";

    final static String INDEX_PATH = "C:/Users/franc/Desktop/hw3_dati/tables/indice";
    public static void main(String[] args) {
        TableIndexer tr = new TableIndexer();
        tr.tableIndexer(TABLE_PATH,INDEX_PATH);
    }
}