package com.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * this class implements Api interface and used to deal with topology json files
 */
public class TopologyApi implements Api {

    /**
     * this method reads a json file that contains topology configuration
     * and store it in memory as a topology object
     * @param fileName the file path of json file that contains the topology
     * @return returns true if the file is read and stored in memory correctly
     * @throws IOException if there is an error in file path
     * @throws ParseException if there is an error in parsing the json file (inappropriate format)
     */
    @Override
    public  boolean readJson(String fileName) throws IOException, ParseException {
        // getting the topology json object from file
        JSONObject obj = readJsonFromFile(fileName);
        // deserialize the json object in a topology object
        Topology tp = createTopologyFromJson(obj);
        // saving the topology in memory
        Memory.addToMemory(tp);
        return true;

    }


    /**
     * this method takes a topology id, searches for it in memory and returns a json string representing this topology
     * [Note] this method should be used with the help of another method to write the returned json string
     * in a specified file
     * @param topologyId a string represents the topology id to read from memory
     *                   and store in another json file
     * @return a json string of the topology read from memory if the topology is already in the memory
     *  and an empty string "" if the topology is not the memory
     */
    @Override
    public  String writeJson(String topologyId) {
        // finding the specified topology in memory
        Topology tp = Memory.findTopology(topologyId);
        // if found we create a json string corresponds to the topology object or return empty string otherwise
        if (tp != null)
            return createJsonFromTopology(tp);
        else
            return "";
    }


    /**
     *  this method returns the number of topologies in memory
     * @return a list of Topology objects that are in memory or null if the memory is empty
     */
    @Override
    public List<Topology> queryTopology() {
        // returning list of topologies in memory
        return Memory.memoryList;
    }


    /**
     *  this method deletes a topology specified with its id from memory
     * @param topologyId the id of the topology to be deleted
     * @return true if the topology is deleted or false if the topology is not in memory
     */
    @Override
    public boolean deleteTopology(String topologyId) {
        return Memory.deleteTopology(topologyId);
    }


    /**
     * this method returns the number of devices for specific topology
     * @param topologyId the id of the topology that has devices
     * @return a list of component objects that are in the topology or null if the topology is not in memory
     */
    @Override
    public List<Component> queryDevices(String topologyId) {
        // finding the specified topology in memory
        Topology tp = Memory.findTopology(topologyId);
        // if found return a component list attached with that topology or return null otherwise
        if(tp != null)
            return tp.components;
        else
            return null;
    }


    /**
     * this method returns the number of devices that are connected to a specific node for specific topology
     * @param topologyId the id of the topology that has devices
     * @param node the node to searchh for devices that are connected to it
     * @return a list of component objects that are connected to the node
     *   null if the topology is not in memory or there are no components connected to that node in that topology
     */
    @Override
    public List<Component> queryDevicesWithNetListNode(String topologyId, String node) {
        // finding the specified topology in memory
        Topology tp = Memory.findTopology(topologyId);
        // if found return a component list attached with that topology and connected to that node or return null otherwise
        if(tp != null)
            return getComponentsWithNode(tp, node);
        else
            return null;

    }


    // the following functions are used in readJson() function

    /**
     * this function takes file path and reads the json object insides it
     * @param fileName the file path of the json file
     * @return a JsonObject corresponding to the content of the file
     * @throws IOException if there is an error in file path
     * @throws ParseException if there is an error in parsing the json file (inappropriate format)
     */
     JSONObject readJsonFromFile(String fileName) throws IOException, ParseException  {
         JSONParser jsonParser = new JSONParser();
         // reading the json file as a string
         FileReader fr = new FileReader(fileName);
         // using the parser to get a json object from the json string
         return (JSONObject) jsonParser.parse(fr);
    }


    /**
     * this method creates a Topology object from JsonObject
     * @param obj the json object to be deserialized
     * @return a Topology object read from the json object
     */
    Topology createTopologyFromJson(JSONObject obj){
        // getting the topology id
        String id = (String) obj.get("id");
        // creating a new topology object
        Topology tp = new Topology(id);
        // creating a json array to store the topology components
        JSONArray components = (JSONArray) obj.get("components");

        // for every component object we deserialize it and get a component object
        // to store it in the topology component list
        components.forEach( component -> tp.addComponent(getComponent((JSONObject)component)) );

        // returning the created topology
        return tp;


    }

    /**
     * this method takes a JsonObject corresponds to a component in the topology list and deserializes it
     * @param obj a JsonObject corresponds to a component in topology list
     * @return a Component object that holds the data in the given JsonObject
     */
    Component getComponent(JSONObject obj) {
        // get the type and id of the component
        String type = (String) obj.get("type");
        String id = (String) obj.get("id");
        // case of the component is a resistor
        if(type.equals("resistor")) {
            // get the netlist and resistance json objects
            JSONObject netList = (JSONObject) obj.get("netlist");
            JSONObject resistance = (JSONObject) obj.get("resistance");

            // getting the values of resistance
            double defaultValue = ((Number) resistance.get("default")).doubleValue();
            double mintValue = ((Number) resistance.get("min")).doubleValue();
            double maxValue = ((Number) resistance.get("max")).doubleValue();

            // getting the netlist nodes t1 and t2
            String t1 = (String) netList.get("t1");
            String t2 = (String) netList.get("t2");
            // creating a resistor component
            Resistor resistor = new Resistor(id);

            // adding the nodes to the netlist
            resistor.addToNetList("t1", t1);
            resistor.addToNetList("t2", t2);

            // adding the resistance values to the resistance configuration map
            resistor.addResistanceValue("default", defaultValue);
            resistor.addResistanceValue("min", mintValue);
            resistor.addResistanceValue("max", maxValue);

            return resistor;
        }
        else {
            // get the netlist and resistance json objects
            JSONObject netList = (JSONObject) obj.get("netlist");
            JSONObject ml = (JSONObject) obj.get("m(l)");

            // getting the values of ml
            double  defaultValue = ((Number) ml.get("default")).doubleValue();
            double mintValue = ((Number) ml.get("min")).doubleValue();
            double maxValue = ((Number) ml.get("default")).doubleValue();

            // getting the netlist nodes drain , source and gate
            String drain = (String) netList.get("drain");
            String gate = (String) netList.get("gate");
            String source = (String) netList.get("source");
            // creating an nmos component
            Nmos nmos = new Nmos(id);
            // adding the nodes to the netlist
            nmos.addToNetList("drain", drain);
            nmos.addToNetList("gate", gate);
            nmos.addToNetList("source", source);

            // adding the ml values to the ml configuration map
            nmos.addToMl("default", defaultValue);
            nmos.addToMl("min", mintValue);
            nmos.addToMl("max", maxValue);


            return nmos;
        }
    }

    // the following function used in writeJson() function

    /**
     * this method creates a jsonObject from Topology object
     * @param tp topology object to be serialized
     * @return a json String corresponds to the topology object
     */
    String createJsonFromTopology(Topology tp) {
        JSONObject obj = new JSONObject();

        // storing the topology id
        obj.put("id", tp.id);

        // storing the topology components
        JSONArray components = new JSONArray();
        List<Component> componentsList = tp.components;

        for (Component comp: componentsList) {
            JSONObject compObj = new JSONObject();
            compObj.put("type", comp.getType());
            compObj.put("id", comp.getId());
            if(comp.type.equals("resistor")){
                Resistor resistor = (Resistor) comp;
                JSONObject resistance = new JSONObject();
                resistance.put("default", resistor.resistance.get("default"));
                resistance.put("min", resistor.resistance.get("min"));
                resistance.put("max", resistor.resistance.get("max"));

                JSONObject netList = new JSONObject();
                netList.put("t1", resistor.getNetList().get("t1"));
                netList.put("t2", resistor.getNetList().get("t2"));

                compObj.put("resistance", resistance);
                compObj.put("netlist", netList);

            }
            else if (comp.type.equals("nmos")) {
                Nmos nmos = (Nmos) comp;
                JSONObject ml = new JSONObject();
                ml.put("default", nmos.ml.get("default"));
                ml.put("min", nmos.ml.get("min"));
                ml.put("max", nmos.ml.get("max"));

                JSONObject netList = new JSONObject();
                netList.put("drain", nmos.getNetList().get("drain"));
                netList.put("gate", nmos.getNetList().get("gate"));
                netList.put("source", nmos.getNetList().get("source"));

                compObj.put("m(l)", ml);
                compObj.put("netlist", netList);
            }
            components.add(compObj);
        }
        obj.put("components", components);


        return obj.toJSONString();
    }

    /**
     * this method takes a specific topology and a specific node to get all components in that topology
     * [Note] it is a helper method with queryDevicesWithNetListNode() function
     * that are connected to that node
     * @param tp the topology object
     * @param node the node to search for it
     * @return a list of Components that are connected to that node or null if there are no components conncted to that node
     */
    public List<Component> getComponentsWithNode(Topology tp, String node) {
        // creating an empty list
        List<Component> list = new ArrayList<>();

        // loop on components list in the topology
        for (Component comp : tp.components) {
            // if component is connected to the node then add to the list
            if (comp.getNetList().containsValue(node)) {
                list.add(comp);
            }
        }

        if(list.size() > 0)
            return list;
        // return null if the list is empty indicating there are no components conneteced to the node
        return null;
    }




}
