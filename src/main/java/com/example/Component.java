package com.example;

import java.util.HashMap;
import java.util.List;

/**
 * this class is the super class of all components
 * that means that all components have to extend this class
 */
public abstract class Component {
    protected String type;
    private String id;
    private HashMap<String, String> netList;

    /**
     * public constructor
     * @param id the id of the component
     */
    public Component(String id){
        this.id = id;
        netList = new HashMap<>();
    }

    /**
     * getter of component id
     * @return the component id
     */
    public String getId() {
        return id;
    }

    /**
     * getter of component type
     * @return a string represent the component type
     */
    public String getType() {
        return type;
    }

    /**
     * a getter of the component netList
     * @return a hash map that holds the component terminals and their corresponding values
     */
    public HashMap<String, String> getNetList() {
        return netList;
    }

    /**
     * this method adds configuration to device terminal
     * @param deviceTerminal the name of device terminal
     *                       for example : the (gate) terminal of the nmos
     * @param node the node name that are connected to this terminal
     */
    public void addToNetList(String deviceTerminal, String node){
        netList.put(deviceTerminal, node);
    }

}
