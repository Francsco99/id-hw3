package it.uniroma3.hw3;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    final static String INDEX_PATH = System.getProperty("user.dir")+"/index";
    final static String TABLE_PATH = System.getProperty("user.dir")+"/tables.json";
    public static void main(String[] args) throws IOException {
        File indexFolder = new File(INDEX_PATH);
        File[] files = indexFolder.listFiles();
        TableIndexer tr = new TableIndexer();
        TableSearcher search = new TableSearcher();

       Codec codec = new SimpleTextCodec();
       tr.tableIndexer(TABLE_PATH, INDEX_PATH, codec);

        String inputString = "_6,Notes,Word,Elections,Call sign,Discoverer(s),First Game,Party,Continental,David Copperfield,occupation,Notes,Belgian Cup,Developer,State,County,County,Title,1995 23x15px (16),Common name(s),Season,Song,J. League,Home team(s),Country,ODS,Lab,Former railroad,Film,Core Based Statistical Area,23x15px Great Britain,Candidate,Week 6 Mar 24,Party,Notes,Date Time,Designation";

        // Dividi la stringa sulla virgola e rimuovi gli spazi
        String[] elements = inputString.split(",\\s*");

        // Converti l'array in un set
        Set<String> stringSet = new HashSet<>(Arrays.asList(elements));

        // Stampa il set
       // System.out.println(stringSet);
       //search.read_from_index(INDEX_PATH,stringSet);
    }

}
