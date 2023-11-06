package it.uniroma3.hw3;

public class Main {
    final static String TABLE_PATH = System.getProperty("user.dir")+"/tabella.json";
    final static String INDEX_PATH = System.getProperty("user.dir")+"/index";
    public static void main(String[] args) {
        TableReader tr = new TableReader();
        tr.tableIndexer(TABLE_PATH,INDEX_PATH);
    }
}