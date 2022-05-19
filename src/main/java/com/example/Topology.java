package com.example;

import java.util.ArrayList;
import java.util.List;

public class Topology {
    String id;
    List<Component> components;

    public Topology(String id){
        this.id = id;
        components = new ArrayList<>();
    }

    public void addComponent(Component component){
        components.add(component);
    }

}
