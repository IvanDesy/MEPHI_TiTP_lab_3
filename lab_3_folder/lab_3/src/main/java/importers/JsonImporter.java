package importers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import Objects.Reactor;
import masters.ReactorsHolder;
/**
 *
 * @author vanya
 */
public class JsonImporter extends Importer{
    @Override
    protected void inputCode(File file, ReactorsHolder reactorMap){
        ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(file);

            for (Iterator<String> it = rootNode.fieldNames(); it.hasNext(); ) {
                String fieldName = it.next();
                JsonNode reactorNode = rootNode.get(fieldName);
                if (reactorNode != null && reactorNode.isObject()) {
                    Reactor reactor = parseReactor(reactorNode);
                    reactorMap.addReactor(fieldName, reactor);
                }
            }
    }
    
    @Override
    protected boolean isTrueType(File file){
        return file.getName().endsWith(".json");
    }

    private Reactor parseReactor(JsonNode reactorNode) {
        String type = reactorNode.has("type") ? reactorNode.get("type").asText() : null;
        String reactorClass = reactorNode.has("class") ? reactorNode.get("class").asText() : null;
        double burnup = reactorNode.has("burnup") ? reactorNode.get("burnup").asDouble() : 0.0;
        double electricalCapacity = reactorNode.has("electrical_capacity") ? reactorNode.get("electrical_capacity").asDouble() : 0.0;
        double enrichment = reactorNode.has("enrichment") ? reactorNode.get("enrichment").asDouble() : 0.0;
        double firstLoad = reactorNode.has("first_load") ? reactorNode.get("first_load").asDouble() : 0.0;
        double kpd = reactorNode.has("kpd") ? reactorNode.get("kpd").asDouble() : 0.0;
        int lifeTime = reactorNode.has("life_time") ? reactorNode.get("life_time").asInt() : 0;
        double thermalCapacity = reactorNode.has("termal_capacity") ? reactorNode.get("termal_capacity").asDouble() : 0.0;

        return new Reactor(type, reactorClass, burnup, electricalCapacity, enrichment, firstLoad, kpd, lifeTime, thermalCapacity, "JSON");
    }


}
