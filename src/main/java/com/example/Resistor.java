package com.example;

import java.util.HashMap;

/**
 * this is the Resistor component
 */
public class Resistor extends Component {

    protected HashMap<String, Double> resistance;


    /**
     * public constructor that initializes the resistor id
     * @param id the resistor id
     */
    public Resistor(String id) {
        super(id);
        type = "resistor";
        resistance = new HashMap<>();
    }

    /**
     * this method adds more configuration to the resistance of the resistor
     * @param config the configuration to be added
     *               for example : the first terminal of the resistor (t1)
     * @param value the value of the corresponding configuration
     *              for example : vdd or ground
     */
    public void addResistanceValue(String config, Double value){
        resistance.put(config, value);
    }



}
