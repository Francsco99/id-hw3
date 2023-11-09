
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
    public void read_from_index(String indexPath, Set<String> inputTermsSet) throws IOException {
        Directory directory_index = FSDirectory.open(Paths.get(indexPath));
        IndexReader reader = DirectoryReader.open(directory_index);
        IndexSearcher searcher = new IndexSearcher(reader);


        /*HASH MAP CHE HA COME CHIAVE IL TERMINE CERCATO E COME VALORE
         * L'INSIEME DI COLONNE E ID TABLE CHE CONTENGONO QUEL TERMINE*/

        Map<String, Set<String[]>> termToColumnsAndIDsMap = new HashMap<>();

        for (String termine : inputTermsSet) {
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

                // Crea una tupla di stringhe per colonnaValue e ID
                String[] tuple = new String[]{colonnaValue, id};

                // Verifica se contenuto è già presente come chiave nella mappa
                if (!termToColumnsAndIDsMap.containsKey(contenuto)) {
                    // Se contenuto non è presente nella mappa, aggiungi contenuto e la tupla corrispondente
                    termToColumnsAndIDsMap.computeIfAbsent(contenuto, k -> new HashSet<>())
                            .add(tuple);
                } else {
                    // Se contenuto è già presente nella mappa, verifica se esiste una tupla con lo stesso primo termine
                    boolean canAddTuple = termToColumnsAndIDsMap.get(contenuto).stream()
                            .noneMatch(existingTuple -> existingTuple[0].equals(colonnaValue));

                    // Se non esiste una tupla con lo stesso primo termine, aggiungi la tupla
                    if (canAddTuple) {
                        termToColumnsAndIDsMap.get(contenuto).add(tuple);
                    }
                }


            }
        }

        // Stampa la mappa
        System.out.println("\n");
        System.out.println("Stampo l'indice invertito:");
        System.out.println("\n");
        for (Map.Entry<String, Set<String[]>> entry : termToColumnsAndIDsMap.entrySet()) {
            System.out.println("Value: " + entry.getKey());
            Set<String[]> columnIdTuple = entry.getValue();
            for (String[] tuple : columnIdTuple) {
                System.out.println("NomiColonne: " + tuple[0]);
                System.out.println("IdTables: " + tuple[1]);
            }
            System.out.println("\n");
        }
        Merge m = new Merge();
        m.mergeList(termToColumnsAndIDsMap);
    }
}