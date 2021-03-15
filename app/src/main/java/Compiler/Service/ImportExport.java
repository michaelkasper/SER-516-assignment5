package Compiler.Service;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ImportExport {
    private static FileWriter file;

    public static boolean saveTo(ArrayList<SpaceModel> spaces, File saveLocation) {

        if (saveLocation != null) {

            JSONObject obj = new JSONObject();

            JSONArray spacesJson = new JSONArray();
            JSONArray elementsJson = new JSONArray();
            JSONArray pointsJson = new JSONArray();
            for (SpaceModel space : spaces) {
                spacesJson.add(space.export());

                for (AbstractElement element : space.getElements()) {
                    elementsJson.add(element.export());

                    for (ConnectionPointModel pointModel : element.getAllConnectionPoints()) {
                        pointsJson.add(pointModel.export());
                    }
                }
            }

            obj.put("spaces", spacesJson);
            obj.put("elements", elementsJson);
            obj.put("points", pointsJson);

            try {
                file = new FileWriter(saveLocation);
                file.write(obj.toJSONString());

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;

            } finally {
                try {
                    file.flush();
                    file.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }

        return false;
    }


    public static ArrayList<SpaceModel> loadFrom(File fileLocation) {
        if (fileLocation != null) {

            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(fileLocation)) {
                ArrayList<SpaceModel> spaces = new ArrayList<>();

                //Read JSON file
                JSONObject data = (JSONObject) jsonParser.parse(reader);

                JSONArray spacesJson = (JSONArray) data.get("spaces");
                JSONArray elementsJson = (JSONArray) data.get("elements");
                JSONArray pointsJson = (JSONArray) data.get("points");

                HashMap<String, SpaceModel> spacesMap = new HashMap<>();
                HashMap<String, AbstractElement> elementsMap = new HashMap<>();
                HashMap<String, ConnectionPointModel> pointsMap = new HashMap<>();

                spacesJson.forEach(spaceJson -> {
                    JSONObject json = (JSONObject) spaceJson;
                    SpaceModel space = new SpaceModel();
                    space.importJson(json);
                    spacesMap.put(space.getId(), space);
                    spaces.add(space);
                });

                elementsJson.forEach(elementJson -> {
                    JSONObject json = (JSONObject) elementJson;
                    String className = (String) json.get("class");
                    AbstractElement element = AbstractElement.Factory(className);
                    element.importJson(json);
                    elementsMap.put(element.getId(), element);
                });

                pointsJson.forEach(pointJson -> {
                    JSONObject json = (JSONObject) pointJson;
                    String className = (String) json.get("class");
                    ConnectionPointModel point = ConnectionPointModel.Factory(className);
                    point.importJson(json);
                    pointsMap.put(point.getId(), point);
                });


                spacesJson.forEach(spaceJson -> {
                    JSONObject json = (JSONObject) spaceJson;
                    String id = (String) json.get("id");

                    SpaceModel space = spacesMap.get(id);
                    space.importRelationships(json, elementsMap, pointsMap);
                });

                elementsJson.forEach(elementJson -> {
                    JSONObject json = (JSONObject) elementJson;
                    String id = (String) json.get("id");

                    AbstractElement element = elementsMap.get(id);
                    element.importRelationships(json, spacesMap, pointsMap);
                });

                pointsJson.forEach(pointJson -> {
                    JSONObject json = (JSONObject) pointJson;
                    String id = (String) json.get("id");

                    ConnectionPointModel point = pointsMap.get(id);
                    point.importRelationships(json, elementsMap, pointsMap);
                });

                return spaces;

            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
