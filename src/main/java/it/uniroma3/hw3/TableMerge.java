package it.uniroma3.hw3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TableMerge {
    private String indexPath;
    private HashMap<String, ArrayList> set2count;
    private HashMap<String, Integer> setValori;
    private LinkedHashMap<String, Integer> mappaOrdinata;
    public TableMerge(String _index){
        set2count = new HashMap<>();

        indexPath = _index;
    }

    public void countSet( String nFile){

        String nomeFile = System.getProperty("user.dir")+ "/" + nFile;
        try {
            FileReader fileReader = new FileReader(nomeFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder stringBuilder = new StringBuilder();
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                stringBuilder.append(linea.replace("\t",""));
            }

            JSON_Parser(stringBuilder.toString());

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void JSON_Parser(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        String valoreCampo;
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            String tableName = rootNode.at("/_id/$oid").asText();
            //System.out.println("nome tabella: " + tableName);

            JsonNode cellsNode = rootNode.get("cells");

            for (JsonNode cellNode : cellsNode) {
                JsonNode cleanedTextNode = cellNode.get("cleanedText");

                if (cleanedTextNode != null) {
                    if (cellNode.at("/isHeader").asText().equals("false")) {
                        valoreCampo = cellNode.at("/cleanedText").asText();
                        queryTables(valoreCampo);
                    }
                }
            }
            tabellaInversa();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryTables(String input) throws ParseException {
        TableQuery query = new TableQuery(indexPath);
        query.setDEBUG(true);
        try {
            query.lanciaQuery("content " + input );
        }catch (Exception e) {
            e.printStackTrace();
        }

        if (query.getNumTabelle() > 0){
            set2count.put(input,query.getArrayTable());
        }
    }
    
    private void tabellaInversa(){
        setValori = new HashMap<>();
        set2count.forEach((chiave, value) -> {
            ArrayList<String> setDiStringhe = value;
            for (String elemento : setDiStringhe) {
                if(setValori.get(elemento) == null){
                    setValori.put(elemento,0);
                }
                setValori.put(elemento, setValori.get(elemento) + 1);
            }
        });
        List<Map.Entry<String, Integer>> listaValori = new ArrayList<>(setValori.entrySet());

        // Ordina la lista in modo decrescente in base ai valori Integer
        Collections.sort(listaValori, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Crea una nuova mappa ordinata
        mappaOrdinata = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : listaValori) {
            mappaOrdinata.put(entry.getKey(), entry.getValue());
        }
    }
    public void statistiche(){
        int count = 0;
        if(!mappaOrdinata.isEmpty()) {
            for (Map.Entry<String, Integer> entry : mappaOrdinata.entrySet()) {
                if (count < 3) {
                    System.out.println("Chiave: " + entry.getKey() + ", Valore: " + entry.getValue());
                    count++;
                } else {
                    break;
                }
            }
        }
    }
}