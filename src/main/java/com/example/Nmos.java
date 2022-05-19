package com.example;

import java.util.HashMap;

/**
 * this class is an Nmos component
 */
public class Nmos extends Component {


    protected HashMap<String, Double> ml = new HashMap<>();

    /**
     * the constructor of the Nmos class
     * @param id the id of the nmos component
     */
    public Nmos(String id) {
        super(id);
        // setting the type as nmos
        type = "nmos";
    }

    /**
     * this method adds more configuration to Ml
     * @param config the configuration to be added
     * @param value the value of the corresponding configuration
     */
    public void addToMl(String config, Double value) {
        ml.put(config, value);
    }
}
