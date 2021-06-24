package familytree;

import java.io.Serializable;

/*
* This class creates an object to store the address of a family member.
* The class accepts addresses in different countries that have
* naming different conventions.
*
* @author Riaz
*/

public class Address implements Serializable {
    
    //street number is string to account for apartments e.g 10A
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String postCode;

    public Address() {
        streetNumber = null;
        streetName = null;
        suburb = null;
        postCode = null;
    }
    
    public Address(String number, String name, String suburb, String postcode) {
        setStreetNumber(number);
        setStreetName(name);
        setSuburb(suburb);
        setPostCode(postcode);   
    }

    public void setStreetNumber(String number) {
        if(number.trim().matches("^[\\d]+$") && number.trim().matches(".*\\d.*")) {
            this.streetNumber = number.trim();  
        }
        else {
            throw new IllegalArgumentException("Invalid Street number.");
        }
    }

    public void setStreetName(String name) {
        if(name.trim().matches("^[a-zA-z' ]+$")) {
            this.streetName = name.trim();
        }
        else {
            throw new IllegalArgumentException("Street name cannot have special characters.");
        }        
    }

    public void setSuburb(String suburb) {
        if(suburb.trim().matches("^[a-zA-z' ]+$")) {
            this.suburb = suburb.trim();
        }
        else {
            throw new IllegalArgumentException("Suburb cannot have special characters.");
        }        
    }

    //regex check for valid postcode
    public final void setPostCode(String postcode) {
        if (postcode.trim().matches("\\d{4}")){
            this.postCode = postcode.trim();
        }
        else {
            throw new IllegalArgumentException("Post code must be a positive numerical value and over 4 numbers");
        }
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public String getSurburb() {
        return this.suburb;
    }

    public String getPostCode() {
        return this.postCode;
    }
   
}
