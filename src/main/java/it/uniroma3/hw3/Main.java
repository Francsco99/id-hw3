package it.uniroma3.hw3;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    final static String TABLE_PATH = "C:/Users/fsali/Desktop/hw3_dati/tables/tables.json";
    final static String INDEX_PATH = "C:/Users/fsali/Desktop/hw3_dati/tables/indice";

    public static void main(String[] args) throws IOException {
        TableIndexer tr = new TableIndexer();
        Searcher search = new Searcher();
        Codec codec = new SimpleTextCodec();
        Set<String> set = new HashSet<>();

        //tr.tableIndexer(TABLE_PATH, INDEX_PATH, codec);
        String s = "War crimes";
        String c = "plural";
        String d = "singular";

        set.add(s);
        set.add(c);

        set.add(d);

        search.read_from_index(INDEX_PATH, set);

    }
}
