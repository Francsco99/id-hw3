package it.uniroma3.hw3;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    final static String INDEX_PATH = System.getProperty("user.dir")+"/index";
    final static String TABLE_PATH = System.getProperty("user.dir")+"/tables.json";

    public static void main(String[] args) throws IOException {
        TableIndexer tr = new TableIndexer();
        Searcher search = new Searcher();
        Codec codec = new SimpleTextCodec();
        Set<String> set = new HashSet<>();

        tr.tableIndexer(TABLE_PATH, INDEX_PATH, codec);
/*        String s = "crimes";
        String c = "plural";
        String d = "singular";

        set.add(s);
        set.add(c);

        set.add(d);

        search.read_from_index(INDEX_PATH, set);*/


        /*int count = 0;
        for (Map.Entry<String, Set<String>> entry : invertedIndex.entrySet()) {
            if (count >= 50) {
                break;
            }
            String key = entry.getKey();
            Set<String> columns = entry.getValue();
            System.out.println("Element: " + key);
            System.out.println("Columns: " + columns);
            System.out.println("\n\n");

            count++;
        }
        int numeroDiChiavi = invertedIndex.size();

        System.out.println("Numero di chiavi nell'HashMap: " + numeroDiChiavi);*/
    }
}
