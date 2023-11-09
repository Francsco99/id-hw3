package it.uniroma3.hw3;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    final static String INDEX_PATH = System.getProperty("user.dir")+"/index";
    final static String TABLE_PATH = System.getProperty("user.dir")+"/tables.json";

    public static void main(String[] args) throws IOException {
        File indexFolder = new File(INDEX_PATH);
        File[] files = indexFolder.listFiles();
        TableIndexer tr = new TableIndexer();
        Searcher search = new Searcher();
        Set<String> set = new HashSet<>();

if(files != null && files.length==0) {
    tr.tableIndexer(TABLE_PATH, INDEX_PATH);
}
else{
    System.out.println("La cartella INDEX_PATH non è vuota. Non verrà eseguita l'indicizzazione.");
}

        String s = "War crimes";
        String c = "plural";
        String d = "singular";

        set.add(s);
        set.add(c);

        set.add(d);

        search.read_from_index(INDEX_PATH, set);

    }
}
