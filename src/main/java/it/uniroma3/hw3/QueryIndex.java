package it.uniroma3.hw3;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class QueryIndex {
    public static void main(String[] args) {
        String indexDirectory = "table_index"; // Inserisci il percorso dell'indice creato

        try {
            // Apre l'indice esistente
            Directory directory = FSDirectory.open(Paths.get(indexDirectory));
            IndexReader reader = DirectoryReader.open(directory);

            // Crea un searcher per l'indice
            IndexSearcher searcher = new IndexSearcher(reader);

            // Esegue una query per trovare tutti i documenti (query di esempio)
            Query query = new MatchAllDocsQuery();
            TopDocs topDocs = searcher.search(query, 10); // Numero massimo di documenti da recuperare

            // Itera sui risultati della query
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docId = scoreDoc.doc;
                Document document = searcher.doc(docId);
                System.out.println("Document ID: " + document.getField("id"));
                // Puoi ottenere i campi del documento qui
            }

            // Chiude l'IndexReader
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
