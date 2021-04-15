package Compiler.Service;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.Elements.MethodEndElement;
import Compiler.Model.Elements.MethodStartElement;
import Compiler.Model.SpaceModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class Code {

    static String START_NODE = "Start";
    static String END_NODE = "End";
    static String TAB = "    ";

    static public String Generate() throws IOException, URISyntaxException {

        String graphTemplate = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("template/graph.template").toURI().getPath()));
        String blockTemplate = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("template/block.template").toURI().getPath()));
        String connectionTemplate = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("template/connection.template").toURI().getPath()));
        String startConnectionTemplate = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("template/startConnection.template").toURI().getPath()));
        String endConnectionTemplate = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("template/endConnection.template").toURI().getPath()));
        String nodeTemplate = Files.readString(Path.of(ClassLoader.getSystemClassLoader().getResource("template/node.template").toURI().getPath()));

        String blocks = "";
        String startConnections = "";
        String endConnections = "";

        int currentSpaceIndex = 0;


        for (SpaceModel space : Store.getInstance().getAllSpaces()) {

            String spaceName = Code.indexToAlphabetic(currentSpaceIndex).toLowerCase(Locale.ROOT);
            String connections = "";
            String nodes = "";

            Queue<AbstractElement> elementsQueue = new LinkedList<>();
            ArrayList<String> foundElements = new ArrayList<>();

            AbstractElement startElement = space.getElements().stream().filter(element -> element instanceof MethodStartElement).findFirst().orElse(null);
            AbstractElement endElement = space.getElements().stream().filter(element -> element instanceof MethodEndElement).findFirst().orElse(null);


            elementsQueue.add(startElement);
            foundElements.add(startElement.getId());

            AbstractElement currentElement;

            while ((currentElement = elementsQueue.poll()) != null) {
                nodes = nodes.concat(nodeTemplate
                        .replaceAll("\\{tabs}", TAB + TAB)
                        .replaceAll("\\{spaceName}", spaceName)
                        .replaceAll("\\{nodeId}", foundElements.indexOf(currentElement.getId()) + "")
                        .replaceAll("\\{nodeType}", currentElement.getType())
                );

                for (AbstractElement toElement : currentElement.getToConnections()) {
                    if (!foundElements.contains(toElement.getId())) {
                        elementsQueue.add(toElement);
                        foundElements.add(toElement.getId());
                    }

                    connections = connections.concat(connectionTemplate
                            .replaceAll("\\{tabs}", TAB + TAB)
                            .replaceAll("\\{spaceName}", spaceName)
                            .replaceAll("\\{fromNodeId}", foundElements.indexOf(currentElement.getId()) + "")
                            .replaceAll("\\{fromNodeType}", currentElement.getType())
                            .replaceAll("\\{toNodeId}", foundElements.indexOf(toElement.getId()) + "")
                            .replaceAll("\\{toNodeType}", toElement.getType())
                    );
                }
            }


            startConnections = startConnections.concat(startConnectionTemplate
                    .replaceAll("\\{tabs}", TAB)
                    .replaceAll("\\{spaceName}", spaceName)
                    .replaceAll("\\{startNode}", START_NODE)
                    .replaceAll("\\{toNodeId}", foundElements.indexOf(startElement.getId()) + "")
                    .replaceAll("\\{toNodeType}", startElement.getType())
            );


            endConnections = endConnections.concat(endConnectionTemplate
                    .replaceAll("\\{tabs}", TAB)
                    .replaceAll("\\{spaceName}", spaceName)
                    .replaceAll("\\{fromNodeId}", foundElements.indexOf(endElement.getId()) + "")
                    .replaceAll("\\{fromNodeType}", endElement.getType())
                    .replaceAll("\\{endNode}", END_NODE)

            );


            blocks = blocks.concat(blockTemplate
                    .replaceAll("\\{spaceName}", spaceName)
                    .replaceAll("\\{connections}", connections)
                    .replaceAll("\\{nodes}", nodes)
            );


            currentSpaceIndex++;
        }

        return graphTemplate
                .replaceAll("\\{blocks}", blocks)
                .replaceAll("\\{startConnections}", startConnections)
                .replaceAll("\\{endConnections}", endConnections)
                .replaceAll("\\{startNode}", START_NODE)
                .replaceAll("\\{endNode}", END_NODE);
    }


    private static String indexToAlphabetic(int i) {
        int quot = i / 26;
        int rem = i % 26;
        char letter = (char) ((int) 'A' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return indexToAlphabetic(quot - 1) + letter;
        }
    }
}
