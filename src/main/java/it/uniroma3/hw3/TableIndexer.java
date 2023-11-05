package it.uniroma3.hw3;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TableIndexer {
    public static void main(String[] args) {
        // Directory in cui verrà creato l'indice Lucene
        String indexDirectory = "table_index";

        try {
            // Inizializza l'analizzatore e la configurazione dell'IndexWriter
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            // Specifica la directory in cui verrà creato l'indice Lucene
            Path indexPath = Paths.get(indexDirectory);
            Directory directory = FSDirectory.open(indexPath);

            IndexWriter indexWriter = new IndexWriter(directory, config);
            // Cancella tutto quello che era presente nella cartella degli indici
            indexWriter.deleteAll();
            // Carica il file JSON contenente le tabelle
            JSONArray tables = loadTablesFromJSONFile(System.getProperty("user.dir")+"/tavola.json");

            // Indicizza le tabelle
            for (int i = 0; i < tables.length(); i++) {
                JSONObject table = tables.getJSONObject(i);
                indexTable(indexWriter, table);
            }

            // Chiudi l'IndexWriter
            indexWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray loadTablesFromJSONFile(String jsonFilePath) {
        JSONArray tables = null;

        try {
            // Crea un oggetto FileReader per leggere il file JSON
            FileReader fileReader = new FileReader(jsonFilePath);

            // Leggi il contenuto del file JSON
            StringBuilder jsonContent = new StringBuilder();
            int character;
            while ((character = fileReader.read()) != -1) {
                jsonContent.append((char) character);
            }

            // Chiudi il FileReader
            fileReader.close();

            // Parsa il contenuto JSON in un JSONArray
            tables = new JSONArray(jsonContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tables;
    }

    private static void indexTable(IndexWriter indexWriter, JSONObject table) throws IOException {
        // Crea un documento Lucene per rappresentare la tabella
        Document doc = new Document();

        // Aggiungi i campi del documento
        // Aggiungi i campi del documento
        doc.add(new TextField("id", table.getString("id"), Field.Store.YES));
        doc.add(new TextField("referenceContext", table.getString("referenceContext"), Field.Store.YES));
        // Aggiungi il documento all'indice
        indexWriter.addDocument(doc);
    }
}
