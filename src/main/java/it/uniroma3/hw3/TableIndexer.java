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
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableIndexer {

    public void tableIndexer(String jsonPath, String indexPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));   // Apre un file JSON contenente tabelle
            String line;    // riga corrente del file json
            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));  // Directory Lucene per indice
            IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());  // Writer per scrivere sull'indice
            IndexWriter writer = new IndexWriter(indexDirectory, indexConfig);
            writer.deleteAll(); // Pulisco il vecchio indice
            int j=0;
            //itera su tutte le tabelle
            while ((line = reader.readLine()) != null) {
                // Analizza la riga corrente come oggetto JSON
                JsonObject jsonTable = JsonParser.parseString(line).getAsJsonObject();
                String tableId = jsonTable.get("id").getAsString(); // Ottiene l'ID della tabella
                int maxRows = jsonTable.getAsJsonObject("maxDimensions").get("row").getAsInt(); // Ottiene il numero massimo di righe
                int maxCols = jsonTable.getAsJsonObject("maxDimensions").get("column").getAsInt(); // Ottiene il numero massimo di colonne
                JsonArray cells = jsonTable.getAsJsonArray("cells"); // Ottiene l'array di celle

                String[] nomiColonne = new String[maxCols+1]; //lista contentente i nomi delle colonne
                // Creare una mappa per l'aggregazione delle colonne
                Map<String, StringBuilder> columnData = new HashMap<>();

                //itera su tutte le celle
                for(int i=0; i<cells.size(); i++){
                    //la prima riga contiene le intestazioni, le salvo in una lista
                    JsonObject cell = cells.get(i).getAsJsonObject();
                    JsonObject coordinates = cell.getAsJsonObject("Coordinates");
                    int col = coordinates.get("column").getAsInt();
                    String cleanedText = cell.get("cleanedText").getAsString();

                    if(coordinates.get("row").getAsInt()==0){
                    nomiColonne[col]=(cleanedText);
                    }
                    else{
                        String columnName = nomiColonne[col];
                        // Aggiungi il valore di cleanedText alla colonna corrispondente
                        columnData.computeIfAbsent(columnName, k -> new StringBuilder()).append(cleanedText).append(",");
                    }
                }
                // Stampa i risultati
                System.out.println("tabella "+j);
                for (Map.Entry<String, StringBuilder> entry : columnData.entrySet()) {
                    String col = entry.getKey();
                    String aggregatedText = entry.getValue().toString().trim();
                    System.out.println("Colonna " + col + ": " + aggregatedText);
                }

                Document doc = new Document();
                doc.add(new TextField("id",tableId,Field.Store.YES));
                for (Map.Entry<String, StringBuilder> entry : columnData.entrySet()) {
                    doc.add(new TextField(entry.getKey(),(entry.getValue().toString()),Field.Store.YES));
                }
                writer.addDocument(doc);

                j++;
            }

            reader.close(); // Chiudi file JSON
            writer.close(); // Chiudi l'indice

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

