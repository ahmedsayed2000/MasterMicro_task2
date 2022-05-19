package com.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;
import java.util.List;


public class Main {


    public static void writeToFile(String filePath, String jsonString) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(jsonString);
        fw.flush();
    }



    public static void main(String[] args) {
	// write your code here

        // creating an api object
        Api api = new TopologyApi();

        // Testing the topology
        System.out.println("Testing the topology Api");

        // 1 ---> req1 --> reading a given topology file and add it to memory
        System.out.println("1... Reading the given topology (topology.json)");

        try {
            boolean added = api.readJson("src\\main\\resources\\topology.json");
            System.out.println("the topology is read and added to Memory");
        } catch (Exception e) {
            System.out.println("the topology is not added");
        }

        // 2 ---> req2 --> writing the topology read in req1 in another file (writeJson.json)
        System.out.println("2... writing (topology.json) in (writeJson.json)");
        String jsonString = api.writeJson(Memory.memoryList.get(0).id);
        try {
            writeToFile("src\\main\\resources\\writeJson.json", jsonString);
            System.out.println("(topology.json) with id : " + Memory.memoryList.get(0).id + " has been written to (writeJson.json)");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("(topology.json) with id : " + Memory.memoryList.get(0).id + " has not been written to (writeJson.json)");
        }

        // 3 ---> req3 --> query about topologies in the memory
        System.out.println("3... printing the topologies in the memory");
        // query about topologies using the api and get the topologies list
        List<Topology> topologies = api.queryTopology();
        System.out.println("Number of topologies in the memory is " + topologies.size());
        // printing the topologies in the memory
        Memory.printTopologies();

        // 4 ---> req4 --> query about topologies in the memory
        System.out.println("4... deleting the given topology (topology.json)");

        System.out.println("size of memory before deletion is " + Memory.memoryList.size());

        String id = Memory.memoryList.get(0).id;
        boolean deleted = api.deleteTopology(id);
        if(deleted)
            System.out.println("the topology with id : " + id + " is deleted from memory");
        else
            System.out.println("the topology with id : " + id + " is not deleted from meory");

        System.out.println("size of memory after deletion is " + Memory.memoryList.size());

        // 5 ---> req5 --> query about Devices in the given topology (topology.json)
        System.out.println("5... query about the devices in the given topology");
        System.out.println("we will add the file again for the sake of testing req5");
        try {
            api.readJson("src\\main\\resources\\topology.json");
            System.out.println("the given file is added again");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // getting the the devices/components in the topology.json
        System.out.println("getting the the devices/components in the topology.json");
        List<Component> devices = api.queryDevices(Memory.memoryList.get(0).id);

        if(devices != null) {
            if (devices.size() == 0)
                System.out.println("there are no components in the given topology");
            else {
                System.out.println("the components in the given topology are as follows : ");
                for (int i=0; i<devices.size(); i++) {
                    System.out.println("component " + (i+1) + " is with type : " + devices.get(i).type + " and id : " + devices.get(i).getId());
                    System.out.println("the netlist is " + devices.get(i).getNetList());
                }
            }
        }
        else {
            System.out.println("there are no topologies with the given id");
        }


        // 6 ---> req6 --> query about Devices connected to a given node in a given topology  (topology.json)
        System.out.println("6... query about devices connected to a given node in the given topology");
        System.out.println("we will query about node {vdd} in topology.json");

        devices = api.queryDevicesWithNetListNode(Memory.memoryList.get(0).id, "vdd");

        if(devices != null) {
            // printing the devices connected to node {vdd}
            System.out.println("the components in the given topology are as follows : ");
            for (int i=0; i<devices.size(); i++) {
                System.out.println("component " + (i+1) + " is with type : " + devices.get(i).type + " and id : " + devices.get(i).getId());
                System.out.println("is connected to node {vdd}");
            }
        }
        else {
            System.out.println("there are no devices connected to node {vdd}");
        }


    }



}
