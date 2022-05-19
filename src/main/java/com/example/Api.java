package com.example;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;
import java.lang.*;

/**
 * this is the interface that should be implemented
 * and responsible for dealing with topologies
 */
public interface Api {
    /**
     * this method reads a json file that contains topology configuration
     * and store it in memory as a topology object
     * @param fileName the file path of json file that contains the topology
     * @return returns true if the file is read and stored in memory correctly
     * @throws IOException if there is an error in file path
     * @throws ParseException if there is an error in parsing the json file (inappropriate format)
     */
    boolean readJson(String fileName) throws IOException, ParseException;

    /**
     * this method takes a topology id, searches for it in memory and returns a json string representing this topology
     * [Note] this method should be used with the help of another method to write the returned json string
     * in a specified file
     * @param topologyId a string represents the topology id to read from memory
     *                   and store in another json file
     * @return a json string of the topology read from memory if the topology is already in the memory
     *  and an empty string "" if the topology is not the memory
     */
    String writeJson(String topologyId);


    /**
     *  this method returns the number of topologies in memory
     * @return a list of Topology objects that are in memory or null if the memory is empty
     */
    List<Topology> queryTopology();


    /**
     *  this method deletes a topology specified with its id from memory
     * @param topologyId the id of the topology to be deleted
     * @return true if the topology is deleted or false if the topology is not in memory
     */
    boolean deleteTopology(String topologyId);


    /**
     * this method returns the number of devices for specific topology
     * @param topologyId the id of the topology that has devices
     * @return a list of component objects that are in the topology or null if the topology is not in memory
     */
    List<Component> queryDevices(String topologyId);


    /**
     * this method returns the number of devices that are connected to a specific node for specific topology
     * @param topologyId the id of the topology that has devices
     * @param node the node to searchh for devices that are connected to it
     * @return a list of component objects that are connected to the node
     *   null if the topology is not in memory or there are no components connected to that node in that topology
     */
    List<Component> queryDevicesWithNetListNode(String topologyId, String node);

}
