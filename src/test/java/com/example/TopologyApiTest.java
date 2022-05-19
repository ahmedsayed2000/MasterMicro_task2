package com.example;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * this class is the automated test for TopologyApi class
 */
public class TopologyApiTest {

    Api api;

    /**
     * this method initializes the api object
     */
    @Before
    public void setUp() {
        api = new TopologyApi();
    }

    /**
     * this method tests that the function that reads json file will throw an IO Exception as the path of the
     * file is not correct
     * @throws IOException if file path is not correct
     * @throws ParseException if the content of the file is not a json string (which is not the case)
     */
    @Test (expected = IOException.class)
    public void  testReadJsonThrowsIOException () throws IOException, ParseException {
        api.readJson("iffj");
    }

    /**
     *  this method tests that the function that reads json file will throw a Parse Exception as the content of the file
     *  is not parsable as a json object
     * @throws IOException if file path is not correct (which is not the case)
     * @throws ParseException if the content of the file is not a json string
     */
    @Test (expected = ParseException.class)
    public void  testReadJsonThrowsParseException () throws IOException, ParseException {
        api.readJson("src\\main\\resources\\testNotJson.json");
    }

    /**
     * this method tests that the function that reads json file will success as the file path is correct and
     * the file content is parsable as a json object
     * @throws IOException if file path is not correct (which is not the case)
     * @throws ParseException if the content of the file is not a json string (which is not the case)
     */
    @Test
    public void  testReadJsonSuccesses () throws IOException, ParseException {
        boolean addedToMemory = api.readJson("src\\main\\resources\\topology.json");
        assertTrue(addedToMemory);
        Memory.emptyMemory();
    }


    /**
     * this method tests that writeJson() function will fail in case of passing a false topology id
     * that is not in the memory
     * @throws IOException if file path is not correct (which is not the case)
     * @throws ParseException if the content of the file is not a json string (which is not the case)
     */
    @Test
    public void testWriteJsonFails() throws IOException, ParseException {
        api.readJson("src\\main\\resources\\topology.json");
        String jsonString =  api.writeJson("top2");
        assertNotSame("a real json string", jsonString);
        Memory.emptyMemory();
    }


    /**
     *  this method tests that writeJson() function will success in case of passing a true topology id
     *  that is in the memory
     * @throws IOException if file path is not correct (which is not the case)
     * @throws ParseException if the content of the file is not a json string (which is not the case)
     */
    @Test
    public void testWriteJsonSuccesses() throws IOException, ParseException {
        api.readJson("src\\main\\resources\\topology.json");
        String jsonString =  api.writeJson("top1");
        assertNotSame("", jsonString);
        Memory.emptyMemory();
    }


    /**
     * this method tests that queryTopology() function will return a list of size (1) after adding just one topology
     */
    @Test
    public void testQueryTopology() throws IOException, ParseException {
        api.readJson("src\\main\\resources\\topology.json");
        int size = api.queryTopology().size();
        assertEquals(1, size);
        Memory.emptyMemory();
    }


    /**
     * this method tests deleteTopology() function by adding one topology then deleting it and check
     * that the size of memory will equal zero
     */
    @Test
    public void testDeleteTopology() throws IOException, ParseException {
        api.readJson("src\\main\\resources\\topology.json");
        boolean deleted = api.deleteTopology("top1");
        assertTrue(deleted);
        int size = Memory.memoryList.size();
        assertEquals(0, size);
    }

    /**
     * this method tests queryDevices() function by adding a known topology and checking that
     * the size of the returned component list will equal to the number of components in
     * that topology
     */
    @Test
    public void testQueryDevicesInCertainTopology() throws IOException, ParseException {
        api.readJson("src\\main\\resources\\topology.json");
        int size = api.queryDevices("top1").size();
        assertEquals(2, size);
        Memory.emptyMemory();
    }

    /**
     * this method tests queryDevicesWithNetListNode() function by adding a known topology and checking that
     * the size of the returned component list will equal to the number of components in
     * that topology that are connected to a known node
     */
    @Test
    public void testQueryDevicesInCertainTopology_ConnectedToCertainNode() throws IOException, ParseException {
        api.readJson("src\\main\\resources\\topology.json");
        int size = api.queryDevicesWithNetListNode("top1", "vdd").size();
        assertEquals(1, size);
        Memory.emptyMemory();
    }




}