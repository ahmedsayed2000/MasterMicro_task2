package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * this class acts as Memory but it is only a list that stores Topologies
 */
public class Memory {

    public static List<Topology> memoryList = new ArrayList<>();

    /**
     * this method adds a topology object in the memory list
     * @param tp the topology object to be added to memory
     */
    public static void addToMemory(Topology tp) {
        memoryList.add(tp);
    }


    /**
     * this method deletes a topology with a given id
     * @param id the id of the topology
     * @return retrun true if the topology is deleted successfully from memory
     * false otherwise
     */
    public static boolean deleteTopology(String id) {
        for (int i=0; i<memoryList.size(); i++) {
            if (memoryList.get(i).id.equals(id)) {
                memoryList.remove(i);
                return true;
            }

        }
        return false;
    }


    /**
     * this method searches for a topology with a given id in the memory list
     * @param id topology id
     * @return the topology object if found , null if it is not found
     */
    public static Topology findTopology(String id) {
        for (int i = 0; i < memoryList.size(); i++) {
            if (memoryList.get(i).id.equals(id)) {
                return memoryList.get(i);
            }
        }
        return null;
    }


    /**
     * this method prints the topologies that are in memory
     */
    public static void printTopologies() {
        for (int i = 0; i < memoryList.size(); i++) {
            System.out.println("topology " + i+1 + " in memory with id : " + memoryList.get(i).id);
            }

        if(memoryList.size() == 0)
            System.out.println("No Topologies are in the memory");
    }

    /**
     * this method deletes all topologies that are in memory
     */
    public static void emptyMemory() {
        for(int i=0; i<memoryList.size(); i++)
            memoryList.remove(i);
    }



}
