package it.uniroma3.hw3;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    final static String INDEX_PATH = "C:/Users/franc/Desktop/hw3_dati/tables/indice";
    final static String TABLE_PATH = "C:/Users/franc/Desktop/hw3_dati/tables/tables.json";

    public static void main(String[] args) throws IOException {
        File indexFolder = new File(INDEX_PATH);
        File[] files = indexFolder.listFiles();
        TableIndexer tr = new TableIndexer();
        TableSearcher search = new TableSearcher();
        Set<String> set = new HashSet<>();

//if(files != null && files.length==0) {
        Codec codec = new SimpleTextCodec();
    tr.tableIndexer(TABLE_PATH, INDEX_PATH, codec);
//}
//else{
  //  System.out.println("La cartella INDEX_PATH non è vuota. Non verrà eseguita l'indicizzazione.");
//}

        String s = "War crimes";
        String c = "plural";
        String d = "Singular";

        set.add(s);
        set.add(c);
        set.add(d);

        //search.read_from_index(INDEX_PATH, set);

    }
}
