
package familytree;

import java.io.Serializable;

/**
 * This class encapsulates a familyMember object in an abstract tree.
 * 
 * @author Riaz
 */
public class familyTree implements Serializable {
    
    private familyMember root;
    
    public familyTree() {
        this.root = null;
    }
    
    public void setRoot(familyMember rootMember){
        this.root = rootMember;
    }
    
    public familyMember getRoot(){
        return this.root;
    }
    
    public boolean hasRoot(){
        if(this.root != null) {
            return true;
        }        
        return false;
    }
    
    
    
}
