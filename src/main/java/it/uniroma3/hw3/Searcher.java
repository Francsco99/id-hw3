package it.uniroma3.hw3;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Searcher {

    /*LEGGE DATI DA UN SET IN INPUT E CERCA NELL'INDICE I TOKEN CORRISPONDENTI*/
    public void read_from_index(String indexPath, Set<String> inputTermsSet) throws IOException {
        /*LUCENE SETUP*/
        Directory directory_index = FSDirectory.open(Paths.get(indexPath)); // Apri directory indice lucene
        IndexReader reader = DirectoryReader.open(directory_index); // Nuovo reader per leggere indice
        IndexSearcher searcher = new IndexSearcher(reader); // Nuovo searcher per ricerche su indice

        /*HASH MAP CHE HA COME CHIAVE IL TERMINE CERCATO E COME VALORE
         * L'INSIEME DI COLONNE CHE CONTENGONO QUEL TERMINE*/
        Map<String, Set<String>> termToColumnsMap = new HashMap<>();

        /*OGGETTO PER CREARE LA MAPPA SET2COUNT */
        Merge m = new Merge();

        /*ITERA SU TUTTI I TERMINI DEL SET DI INPUT*/
        for (String termine : inputTermsSet) {
            PhraseQuery.Builder builder = new PhraseQuery.Builder(); // Costruisci Phrase query
            Term term = new Term("contenuto", termine); // Cerca il termine nel campo "contenuto"
            builder.add(term);  // Aggiungi il termine al builder
            PhraseQuery phraseQuery = builder.build(); // Costruisci la query
            TopDocs hits = searcher.search(phraseQuery, 50);    // Restituisce  i primi 50 risultati

            /*ITERA SUI RISULTATI TROVATI*/
            for (int i = 0; i < hits.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = hits.scoreDocs[i]; // Prendi il documento i-esimo
                Document doc = searcher.doc(scoreDoc.doc);
                String columnValue = doc.get("contenuto"); // Prendi il valore del campo contenuto
                String columnName = doc.get("colonna");   // Prendi il valore del campo colonna
                String id = doc.get("id");  // Prendi il valore del campo id

                /*STAMPA id DELLA TABELLA + NOME DELLA COLONNA*/
                System.out.println("valore dell'id della tabella: " + id + "Colonna: " + columnName);

                /*CREAZIONE DELLA LISTA TERMINI->COLONNE*/
                termToColumnsMap.computeIfAbsent(columnValue, k -> new HashSet<>()); // Se la chiave non è presente, crea il nuovo set
                termToColumnsMap.get(columnValue).add(columnName); // Aggiungi la colonna in corrispondenza del termine
            }
        }

        /*STAMPA DELLA MAPPA*/
        for (Map.Entry<String, Set<String>> entry : termToColumnsMap.entrySet()) {
            System.out.println("Value: " + entry.getKey());
            System.out.println("NomiColonne: " + entry.getValue());
        }

        m.mergeList(termToColumnsMap); // Crea la mappa set2count
    }
}














