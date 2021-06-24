package familytree;

/*
@author Riaz
*/

import java.io.Serializable;
import java.util.ArrayList;

/*
* This class creates a member belonging to a particular family.
* Every member has a set of attributes
*/
public class familyMember implements Serializable
{
    public enum Gender {
        Male ,
        Female ;
    }   
    
    //private long serialVersionUID = 1L;
    private String firstName;
    private String surname;
    private String legalSurname;
    private Gender gender; 
    private Address address;
    private String description;
    private familyMember father;
    private familyMember mother;
    private familyMember spouse;
    private ArrayList<familyMember> children;
    
    private String nameRegex = "^[\\p{L} .'-]+$";
    /*
    Override the toString() method in TreeView to display the name of the member in the tree
    */
    @Override
    public String toString() {
        
        char icon;
        //Code inspired from https://gist.github.com/Samistine/bb59be5c4000689ae625 on lines 61 and 62
        if (this.gender == Gender.Male){
            icon = '\u2642';
        } else {
            icon = '\u2640';
        }        
        
        return icon + " " + this.firstName + " " + this.surname;
    }
   
    
    public familyMember(String fname, String surname, Gender gender, Address address, String desc) {

        setFirstName(fname);
        setSurname(surname);
        setLegalSurname("");
        setGender(gender);
        setAddress(address);
        setDescription(desc);
        
        this.father = null;
        this.mother = null;
        this.spouse = null;
        this.children = new ArrayList<>();
    }
    
    public void setFirstName(String firstName) {  
        if(firstName.trim().matches(nameRegex)) {
            this.firstName = firstName.trim();
        } else {
            throw new IllegalArgumentException("Invalid first name");
        }       
    }

    public void setSurname(String surname) {
        if(surname.trim().matches(nameRegex)) {
            this.surname = surname.trim();
        } else {
            throw new IllegalArgumentException("Invalid surname");
        }   
    }

    public void setLegalSurname(String legalSurname) {  
        if(legalSurname.trim().matches(nameRegex)) {
            if(this.gender == Gender.Female) {
                this.legalSurname = legalSurname.trim();
            }            
            else {
                throw new IllegalArgumentException("Only females can have maiden name");
            }
        }
        else if  (legalSurname.isEmpty()) {
            this.legalSurname = "";
        }
        else {
            throw new IllegalArgumentException("Invalid maiden name");
        }
                
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAddress(Address address) {
        //shallow copy??
        this.address = address;
    }

    //description field is optional and can be empty
    public void setDescription(String desc) {
        this.description = desc;
    }
   
    //check if member has a father and gender is male
    public void setFather(familyMember father) {
        if(!this.hasFather()) {
            if(father.gender == Gender.Male) {
                this.father = father;
                //father.setChild(this);
            }
            else {
                throw new IllegalArgumentException("Father can only be male");
            }
        }
        else {
            throw new IllegalArgumentException("Father already added");
        }        
    }
    
    public void setMother(familyMember mother) {
        if(!this.hasMother()) {
            if(mother.gender == Gender.Female) {
                this.mother = mother;
                //mother.setChild(this);
            }
            else {
                throw new IllegalArgumentException("Mother can only be female");
            }
        }
        else {
            throw new IllegalArgumentException("Mother already added");
        }
    }

    public void setSpouse(familyMember spouse) {
        if(!this.hasSpouse()) {
            if((this.gender == Gender.Male && spouse.getGender() == Gender.Female) || (this.gender == Gender.Female && spouse.getGender() == Gender.Male)) {
                this.spouse = spouse;
                spouse.spouse = this;
            }
            else {
                throw new IllegalArgumentException("Spouse must be opposite gender");
            }
        }
        else {
            throw new IllegalArgumentException("Spouse already added");
        }
    }
    
    public void setChild(familyMember child) {
        child.setFather(this);
        child.setMother(this.getSpouse());
        this.children.add(child);
    }

    public void setChildren(ArrayList<familyMember> children) {
        this.children = children; //shallow copy
    }
    
    /*
    Set the relatives of a family member
    */
    public void setRelative(String relativeType, familyMember newMember, familyMember rootMember) {
        
        if(relativeType.equals("Spouse")) {
            rootMember.setSpouse(newMember);
        }       
        else if(relativeType.equals("Mother")) {
            rootMember.setMother(newMember);
        }        
        else if(relativeType.equals("Father")) {
            rootMember.setFather(newMember);
        }
        else if(relativeType.equals("Child") ){
            rootMember.setChild(newMember);
        }
        else {
            throw new NullPointerException("need to specify relative");
        }
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getSurname() {
        return this.surname;
    }
    
    public String getlegalName() {
        return this.legalSurname;
    }
    
    public Gender getGender() {
        return this.gender;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    public familyMember getFather() {
        return this.father;
    }
    
    public familyMember getMother() {
        return this.mother;
    }
    
    public familyMember getSpouse() {
        return this.spouse;
    }
    
    public ArrayList<familyMember> getChildren() {
        return this.children;
    }    
    
    public boolean hasFather() {
        if(this.getFather() != null) {
            return true;
        }
        else
            return false;
    }
    
    public boolean hasMother() {
        if(this.getMother() != null) {
            return true;
        }
        else
            return false;
    }
    
    public boolean hasParents() {
        if((this.getFather() != null) || (this.getMother() != null)) {
            return true;
        }
        else
            return false;
    }
    
    public boolean hasSpouse() {
        if(this.getSpouse() != null) {
            return true;
        }
        else
            return false;
    }
    
    public boolean hasChildren() {
        if(!this.getChildren().isEmpty()) {
            return true;
        }
        else
            return false;
    }
}
