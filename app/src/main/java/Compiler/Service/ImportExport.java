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

public class ImportExport {
    private static FileWriter file;

    public static boolean saveTo(ArrayList<SpaceModel> spaces, File saveLocation) {

        if (saveLocation != null) {

            JSONObject obj = new JSONObject();

            JSONArray spacesJson = new JSONArray();
            JSONArray elementsJson = new JSONArray();
            JSONArray pointsJson = new JSONArray();

            Store store = Store.getInstance();

            for (SpaceModel space : store.getAllSpaces()) {
                spacesJson.add(space.export());
            }

            for (AbstractElement element : store.getAllElements()) {
                if (element.getSpaceModel() != null) {
                    elementsJson.add(element.export());
                }
            }

            for (ConnectionPointModel pointModel : store.getAllConnectionPoints()) {
                if (pointModel.getElementModel().getSpaceModel() != null) {
                    pointsJson.add(pointModel.export());
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

            Store store = Store.getInstance();
            store.clear();

            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(fileLocation)) {
                //Read JSON file
                JSONObject data = (JSONObject) jsonParser.parse(reader);

                JSONArray spacesJson = (JSONArray) data.get("spaces");
                JSONArray elementsJson = (JSONArray) data.get("elements");
                JSONArray pointsJson = (JSONArray) data.get("points");

                spacesJson.forEach(spaceJson -> {
                    JSONObject json = (JSONObject) spaceJson;
                    new SpaceModel(json);
                });

                elementsJson.forEach(elementJson -> {
                    JSONObject json = (JSONObject) elementJson;
                    String className = (String) json.get("class");
                    AbstractElement.Factory(className, json);
                });

                pointsJson.forEach(pointJson -> {
                    JSONObject json = (JSONObject) pointJson;
                    String className = (String) json.get("class");
                    ConnectionPointModel.Factory(className, json);
                });


                System.out.println(store.getAllSpaces().size());
                spacesJson.forEach(spaceJson -> {
                    JSONObject json = (JSONObject) spaceJson;
                    String id = (String) json.get("id");

                    SpaceModel space = store.getSpaceById(id);
                    space.importRelationships(json);
                });

                elementsJson.forEach(elementJson -> {
                    JSONObject json = (JSONObject) elementJson;
                    String id = (String) json.get("id");

                    AbstractElement element = store.getElementById(id);
                    element.importRelationships(json);
                });

                pointsJson.forEach(pointJson -> {
                    JSONObject json = (JSONObject) pointJson;
                    String id = (String) json.get("id");

                    ConnectionPointModel point = store.getConnectionPointById(id);
                    point.importRelationships(json);
                });

                return store.getAllSpaces();

            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
