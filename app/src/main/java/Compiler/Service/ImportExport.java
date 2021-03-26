package Compiler.Service;

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

    public static boolean saveTo(File saveLocation) {

        if (saveLocation != null) {

            JSONObject obj = new JSONObject();

            JSONArray spacesJson = new JSONArray();
            JSONArray elementsJson = new JSONArray();

            Store store = Store.getInstance();

            for (SpaceModel space : store.getAllSpaces()) {
                spacesJson.add(space.export());
            }

            for (AbstractElement element : store.getAllElements()) {
                if (element.getSpaceModel() != null) {
                    elementsJson.add(element.export());
                }
            }

            obj.put("spaces", spacesJson);
            obj.put("elements", elementsJson);

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
            JSONParser jsonParser = new JSONParser();
            ArrayList<SpaceModel> spaces = new ArrayList<>();

            try (FileReader reader = new FileReader(fileLocation)) {
                //Read JSON file
                JSONObject data = (JSONObject) jsonParser.parse(reader);

                JSONArray spacesJson = (JSONArray) data.get("spaces");
                JSONArray elementsJson = (JSONArray) data.get("elements");

                spacesJson.forEach(spaceJson -> {
                    JSONObject json = (JSONObject) spaceJson;
                    spaces.add(new SpaceModel(json));
                });

                elementsJson.forEach(elementJson -> {
                    JSONObject json = (JSONObject) elementJson;
                    String className = (String) json.get("class");
                    AbstractElement.Factory(className, json);
                });

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

                return spaces;

            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
