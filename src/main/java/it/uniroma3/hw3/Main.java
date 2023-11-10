package it.uniroma3.hw3;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.index.Term;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    final static String INDEX_PATH = System.getProperty("user.dir") + "/index";
    final static String TABLE_PATH = System.getProperty("user.dir") + "/tables.json";

    public static void main(String[] args) throws IOException {
        TableIndexer tr = new TableIndexer();
        TableSearcher search = new TableSearcher();
        TermExtractor te = new TermExtractor();
        PrintColored printer = new PrintColored();

        Codec codec = new SimpleTextCodec();
        // tr.tableIndexer(TABLE_PATH, INDEX_PATH, codec);

        List<Set<String>> randomSets = te.randomSetBuilder();

       int totalSetsNumber = randomSets.size();

        long currentTime = System.currentTimeMillis();
        printer.printColored("Inizio query","red");
        printer.printColored("******************************************************************","red");
       for(Set<String> rs : randomSets){
           search.read_from_index(INDEX_PATH,rs);
       }
       long endTime = System.currentTimeMillis();

       long elapsedTimeSeconds = (endTime-currentTime)/1000;
        printer.printColored("Fine query","red");
        printer.printColored("******************************************************************","red");

       printer.printColored("Tempo per eseguire "+totalSetsNumber+" query: "+ elapsedTimeSeconds +" secondi","green");

    }

}
