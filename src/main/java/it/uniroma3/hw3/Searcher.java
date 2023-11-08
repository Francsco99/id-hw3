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
    public void read_from_index(String indexPath, Set<String> datiColonna) throws IOException {
        Directory directory_index = FSDirectory.open(Paths.get(indexPath));
        IndexReader reader = DirectoryReader.open(directory_index);

        IndexSearcher searcher = new IndexSearcher(reader);
        Map<String, Set<String>> mappa = new HashMap<>();
        for (String termine : datiColonna) {
            PhraseQuery.Builder builder = new PhraseQuery.Builder();
            Term term = new Term("contenuto", termine);
            builder.add(term);
            PhraseQuery phraseQuery = builder.build();

            // cerca tra i primi 50 risultati
            TopDocs hits = searcher.search(phraseQuery, 50);
            for (int i = 0; i < hits.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = hits.scoreDocs[i];
                Document doc = searcher.doc(scoreDoc.doc);
                String contenuto = doc.get("contenuto");
                String colonnaValue = doc.get("colonna");
                String id = doc.get("id");
                System.out.println("valore dell'id della tabella ----->>>>---->>>>"+id + "Colonna---->" + colonnaValue);

                // Se la chiave contenuto non è presente nella mappa, creala e aggiungi una nuova lista
                mappa.computeIfAbsent(contenuto, k -> new HashSet<>());
                // Aggiungi il valore di colonna alla lista associata alla chiave contenuto
                mappa.get(contenuto).add(colonnaValue);
            }
        }

        // Stampa la mappa
        for (Map.Entry<String, Set<String>> entry : mappa.entrySet()) {
            System.out.println("Value: " + entry.getKey());
            System.out.println("NomiColonne: " + entry.getValue());
        }
        Merge m = new Merge();
        m.mergeList(mappa);
    }
}














