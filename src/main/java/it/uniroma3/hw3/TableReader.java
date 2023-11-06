package it.uniroma3.hw3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableReader {

    public void tableIndexer(String jsonPath,String indexPath) {
        try {
            // Apre un file JSON contenente tabelle
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));
            String line;
            int j = 0; // Contatore per le tabelle


            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());
            IndexWriter writer = new IndexWriter(indexDirectory,indexConfig);
            writer.deleteAll();


            while ((line = reader.readLine()) != null) {
                // Analizza la riga corrente come oggetto JSON
                JsonObject jsonTable = JsonParser.parseString(line).getAsJsonObject();
                String tableId = jsonTable.get("id").getAsString(); // Ottiene l'ID della tabella
                JsonObject maxDimensions = jsonTable.getAsJsonObject("maxDimensions");
                int maxRows = maxDimensions.get("row").getAsInt(); // Ottiene il numero massimo di righe
                int maxCols = maxDimensions.get("column").getAsInt(); // Ottiene il numero massimo di colonne
                JsonArray cells = jsonTable.getAsJsonArray("cells"); // Ottiene l'array di celle

                System.out.println("Tabella " + j + " " + tableId + " " + maxRows + " " + maxCols);
                j++;
                // Crea una mappa per memorizzare i valori "cleanedText" per ciascuna colonna
                Map<Integer, List<String>> columnValues = new HashMap<>();

                Document doc = new Document();
                // Riempie la mappa con i valori "cleanedText" per ciascuna colonna
                for (int i = 0; i < cells.size(); i++) {
                    JsonObject cell = cells.get(i).getAsJsonObject();
                    JsonObject coordinates = cell.getAsJsonObject("Coordinates");
                    int col = coordinates.get("column").getAsInt();
                    String cleanedText = cell.get("cleanedText").getAsString();
                    // Aggiunge il valore alla colonna corrispondente
                    if (!columnValues.containsKey(col)) {
                        columnValues.put(col, new ArrayList<>());
                    }
                    columnValues.get(col).add(cleanedText);
                }

                doc.add(new TextField("id",tableId,Field.Store.YES));
                // Itera sulle colonne per stampare il contenuto
                for (int c = 0; c <= maxCols; c++) {
                    if (columnValues.containsKey(c)) {
                        List<String> columnData = columnValues.get(c);
                        String columnDataString = String.join(", ",columnData);
                        doc.add(new TextField("column"+c,columnDataString,Field.Store.YES));
                        System.out.println("Column " + c + ": " + String.join(", ", columnData));
                    }
                }
                writer.addDocument(doc);
                writer.commit();

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
    /*    public void readTable(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int j = 0;
            while ((line = reader.readLine()) != null) {
                JsonObject jsonTable = JsonParser.parseString(line).getAsJsonObject();
                String tableId = jsonTable.get("id").getAsString();

                JsonArray cells = jsonTable.getAsJsonArray("cells");
                for (int i = 0; i < cells.size(); i++) {
                    JsonObject cell = cells.get(i).getAsJsonObject();
                    String columnName = cell.get("cleanedText").getAsString();

                }
                System.out.println("tabella " + j + " " + tableId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/



