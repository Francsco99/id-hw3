package it.uniroma3.hw3;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class TableQuery {
    private boolean DEBUG;
    private String indexPath;
    private long elapsedTime = 0;
    private ArrayList<String> listaTabelle;
    private int numTabelle;
    private int dimensioneIndice;
    private String stringaQuery;
    public TableQuery(String _indexPath){
        indexPath = _indexPath;
        DEBUG = false;
    }

    public void lanciaQuery(String input) throws ParseException {
        if(DEBUG){ System.out.println("Lucene query=>"+input); }
        Query luceneQuery = buildLuceneQuery(input);
        stringaQuery = input;

        try (Directory directory = FSDirectory.open(Paths.get(this.indexPath))) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                dimensioneIndice = reader.numDocs();
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher, luceneQuery);

            } finally {
                directory.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getArrayTable(){
        return listaTabelle;
    }

    public int getNumTabelle(){
        return numTabelle;
    }

    public void setDEBUG(boolean flag){ DEBUG=flag; }

    private void runQuery(IndexSearcher searcher, Query query) throws IOException {
        runQuery(searcher, query, false);
    }
    private void runQuery(IndexSearcher searcher, Query query,boolean explain) throws IOException {
        // Avvia il timer
        long startTime = new Date().getTime();

        TopDocs hits = searcher.search(query, dimensioneIndice);

        // Ferma il timer
        long endTime = new Date().getTime();
        // Calcola il tempo trascorso in millisecondi
        this.elapsedTime = endTime - startTime;

        if(DEBUG){ System.out.println("Numero tabelle trovate:" + hits.scoreDocs.length); }
        if(DEBUG){ System.out.println("Tempi di ricerca(ms):" + this.elapsedTime); }

        numTabelle = hits.scoreDocs.length;

        if(hits.scoreDocs.length > 0){
            listaTabelle = new ArrayList<>();
            for (int i = 0; i < hits.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = hits.scoreDocs[i];
                listaTabelle.add(searcher.doc(scoreDoc.doc).get("table"));

                if (explain) {
                    Explanation explanation = searcher.explain(query, scoreDoc.doc);
                    System.out.println(explanation);
                }
            }
        }
    }

    private Query buildLuceneQuery(String input) {
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        String[] terms = input.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String fieldName = terms[0]; // Estrai il nome del campo dalla prima parola
        String operator = "SHOULD"; // Default operator is OR
        System.out.println("");

        for (int i = 1; i < terms.length; i++) {
            String term = terms[i];
            if (term.equals("AND")) {
                operator = "MUST";
            } else if (term.equals("OR")) {
                operator = "SHOULD";
            } else if (term.equals("NOT")) {
                operator = "MUST_NOT";
            } else {
                Query query = createQueryFromTerm(fieldName, term.toLowerCase());
                booleanQuery.add(new BooleanClause(query, getBooleanClause(operator)));
            }
        }

        return booleanQuery.build();
    }

    private Query createQueryFromTerm(String fieldName, String term) {

        if (term.startsWith("\"") && term.endsWith("\"")) {
            // Phrase query
            String phrase = term.substring(1, term.length() - 1);
            String[] words = phrase.split(" ");
            PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
            for (String word : words) {
                phraseQuery.add(new Term(fieldName, word));
            }
            return phraseQuery.build();
        } else {
            // Term query
            return new TermQuery(new Term(fieldName, term));
        }
    }

    private BooleanClause.Occur getBooleanClause(String operator) {
        switch (operator) {
            case "MUST":
                return BooleanClause.Occur.MUST;
            case "MUST_NOT":
                return BooleanClause.Occur.MUST_NOT;
            case "SHOULD":
            default:
                return BooleanClause.Occur.SHOULD;
        }
    }

}
