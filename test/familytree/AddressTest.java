/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package familytree;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Riaz
 */
public class AddressTest {
    
    public AddressTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Test of setStreetNumber method, of class Address.
     */
    @Test
    public void testSetStreetNumber() {
        System.out.println("setStreetNumber");
        String number = "";
        Address instance = new Address();
        instance.setStreetNumber(number);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStreetName method, of class Address.
     */
    @Test
    public void testSetStreetName() {
        System.out.println("setStreetName");
        String name = "";
        Address instance = new Address();
        instance.setStreetName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSuburb method, of class Address.
     */
    @Test
    public void testSetSuburb() {
        System.out.println("setSuburb");
        String suburb = "";
        Address instance = new Address();
        instance.setSuburb(suburb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPostCode method, of class Address.
     */
    @Test
    public void testSetPostCode() {
        System.out.println("setPostCode");
        String postcode = "";
        Address instance = new Address();
        instance.setPostCode(postcode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStreetNumber method, of class Address.
     */
    @Test
    public void testGetStreetNumber() {
        System.out.println("getStreetNumber");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getStreetNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStreetName method, of class Address.
     */
    @Test
    public void testGetStreetName() {
        System.out.println("getStreetName");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getStreetName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSurburb method, of class Address.
     */
    @Test
    public void testGetSurburb() {
        System.out.println("getSurburb");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getSurburb();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPostCode method, of class Address.
     */
    @Test
    public void testGetPostCode() {
        System.out.println("getPostCode");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getPostCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
