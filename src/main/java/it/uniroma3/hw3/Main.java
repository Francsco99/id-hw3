package it.uniroma3.hw3;

import java.util.List;
import java.util.Map;

public class Main {

    final static String TABLE_PATH = System.getProperty("user.dir")+"/tavola.json";
    final static String INDEX_PATH = System.getProperty("user.dir")+"/index";
    public static void main(String[] args) {
        //TableIndexer tr = new TableIndexer();
        //tr.tableIndexer(TABLE_PATH,INDEX_PATH);
        InvertedIndex inv = new InvertedIndex();
        Map<String, List<String>> invertedIndex = inv.inverted(TABLE_PATH,INDEX_PATH);

        int count = 0;
        for (Map.Entry<String, List<String>> entry : invertedIndex.entrySet()) {
            if (count >= 10) {
                break;
            }
            String key = entry.getKey();
            List<String> columns = entry.getValue();
            System.out.println("Element: " + key);
            System.out.println("Columns: " + columns);
            count++;
        }
    }
}