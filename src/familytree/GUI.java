package familytree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is the main class with all the GUI logic. The class
 * displays the family tree in the TreeView pane on the left and 
 * details about a member on the details pane on the right hand side.
 * 
 * @author Riaz
 */
public class GUI extends Application {
    
    TreeView tree = new TreeView();
    TreeItem root;
    familyTree FamilyTree;
    
    VBox initPane;
    GridPane initialPane;    
    Button add;
    HBox statusbar;
    Label status;
            
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Family Tree");        
        
        //application title
        Label title = new Label();
        title.setText("Family Tree Application");
        title.setFont(new Font(24));
        
        HBox header = new HBox();
        header.setPadding(new Insets(0,0,0,5)); //set left padding
        header.getChildren().addAll(title);
        
        HBox menu = createButtonMenu(primaryStage); 
        
        //create vbox wrapper for title and menu button
        VBox topWrapper = new VBox(5);
        topWrapper.getChildren().addAll(header, menu);
        
        initialPane = new GridPane();
        initialPane.setPadding(new Insets(5,5,5,5));
        initialPane.setVgap(8);
        initialPane.setHgap(10);
        
        Text initMessage = new Text("Looks like your tree is empty. Add a root person to the tree.");
        initMessage.setWrappingWidth(240);
        initMessage.setFont(new Font("Arial", 16));
        
        add = new Button("Add Person"); 
        add.setDisable(true);
        add.setOnAction(e -> {
            addMember();
        });
        
        initialPane.add(initMessage, 0, 0);
        initialPane.add(add, 0, 1);
        initPane = new VBox(initialPane);        
        
        tree.getSelectionModel().selectedItemProperty().addListener( (v, oldvalue, newvalue) -> {
                try {
                    displaySelectedMember((TreeItem<familyMember>)newvalue);
                    status.setText("Details of " + ((TreeItem<familyMember>)newvalue).getValue().getFirstName());
                }
                catch (NullPointerException i) {  }
                catch (ClassCastException c) {  }
        });
        tree.setMinHeight(500);
        tree.setMaxHeight(500);
        tree.setMinWidth(250);
        tree.setMaxWidth(250);
                
        //wrapper for treeview and person info pane
        HBox bottomWrapper = new HBox(5);
        bottomWrapper.setPadding(new Insets(8,4,8,4));
        bottomWrapper.getChildren().addAll(tree, initPane);
                
        statusbar = new HBox();
        statusbar.setPadding(new Insets(0,4,0,4));
        status = new Label("Application started");
        statusbar.getChildren().addAll(status);
        
        FlowPane rootPane = new FlowPane();
        rootPane.getChildren().addAll(topWrapper, bottomWrapper, statusbar);
        
        Scene scene = new Scene(rootPane, 530, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); //prevent user from resizing the window
        primaryStage.show();
        
    }
    
    /*
     * Menu for all the buttons
    */
    public HBox createButtonMenu(Stage primaryStage) {
        
        //create button menu
        HBox menu = new HBox(5); //set spacing of 5 between elements
        menu.setPadding(new Insets(0,0,0,8));
        
        FileChooser fileChooser = new FileChooser();
        Button load = new Button("Load a family tree");
        load.setOnAction(e-> {
            File selected = fileChooser.showOpenDialog(primaryStage);
            
            try {
                FileInputStream fi = new FileInputStream(selected);
                ObjectInputStream oi = new ObjectInputStream(fi);
                
                FamilyTree = (familyTree) oi.readObject();
                oi.close();
                fi.close();
                
                FamilyTree.setRoot(FamilyTree.getRoot());
                status.setText("Family tree loaded");
                displayTreeFromFile();
            }
            catch (IOException i) {
                status.setText("Error: " + i.getMessage());
            }
            catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
        });
        
        Button save = new Button("Save family tree");
        save.setOnAction(e-> {
            
            FileChooser fc = new FileChooser();
            fc.setTitle("Save family tree");
            FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter(".ser", "*.ser");
            fc.getExtensionFilters().add(fileExtension);
            File f = fc.showSaveDialog(primaryStage);
            
            if(f != null) {
                try {
                    FileOutputStream fo = new FileOutputStream(f);
                    ObjectOutputStream oo = new ObjectOutputStream(fo);
                    oo.writeObject(FamilyTree);
                    oo.close();
                    fo.close();
                    status.setText("Family tree saved");
                }
                catch (IOException i) {
                    status.setText("Error: " + i.getMessage());
                }
            }
        });
        
        Button create = new Button("Create new tree");
        create.setOnAction(e-> {            
            
            //root is set to null in constructor
            FamilyTree = new familyTree();
            displayTree();
            initPane.getChildren().setAll(initialPane);

            add.setDisable(false);     
            status.setText("Empty tree created");
        });
        
        menu.getChildren().addAll(load, save, create);
        
        return menu;        
    }
    
    /*
     * Add a new family member to the tree
    */
    public void addMember() {
        
        GridPane detailsGrid = new GridPane();
        detailsGrid.setPadding(new Insets(5,5,5,5));
        detailsGrid.setVgap(8);
        detailsGrid.setHgap(10);
        
        Label personInfo = new Label("Person Info");
        personInfo.setFont(new Font("Arial", 16));
        detailsGrid.add(personInfo, 0, 0);
        
        Label relativeLabel = new Label("Relative");
        detailsGrid.add(relativeLabel, 0, 1);
        ComboBox relative = new ComboBox();
        relative.getItems().add("Mother");
        relative.getItems().add("Father");
        relative.getItems().add("Spouse");
        relative.getItems().add("Child");
        detailsGrid.add(relative, 1, 1);
        
        //disable the combobox selection when tree is empty
        if(!FamilyTree.hasRoot()) {
            relative.setDisable(true);
        }
        else { //enable the combobox selection when tree has node and set the selection to the first item
            relative.setDisable(false);
            relative.getSelectionModel().selectFirst();
        }
        
        Label firstNameLabel = new Label("Name");
        TextField firstName = new TextField("Jay");
        detailsGrid.add(firstNameLabel, 0, 2);
        detailsGrid.add(firstName, 1, 2);
        
        Label surnameLabel = new Label("Surname");
        TextField surname = new TextField("Pritchett");
        detailsGrid.add(surnameLabel, 0, 3);
        detailsGrid.add(surname, 1, 3);
        
        Label maidenNameLabel = new Label("Maiden Name");        
        TextField maidenName = new TextField("");
        detailsGrid.add(maidenNameLabel, 0, 4);
        detailsGrid.add(maidenName, 1, 4);
        
        Label genderLabel = new Label("Gender");
        RadioButton male = new RadioButton(familyMember.Gender.Male.toString());
        male.setSelected(true);
        RadioButton female = new RadioButton(familyMember.Gender.Female.toString());

        ToggleGroup genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        
        HBox genderSelection = new HBox(2);
        genderSelection.getChildren().addAll(male, female);
        detailsGrid.add(genderLabel, 0, 5);
        detailsGrid.add(genderSelection, 1, 5);
        
        Label descriptionLabel = new Label("Description");
        TextArea description = new TextArea("I own Pritchett's closets");
        description.setPrefColumnCount(10);
        description.setPrefRowCount(5);
        description.setWrapText(true);
        ScrollPane scrollpane = new ScrollPane(description);
        detailsGrid.add(descriptionLabel, 0, 6);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        detailsGrid.add(scrollpane, 1, 6);
        
        Label addressInfo = new Label("Address");
        addressInfo.setFont(new Font("Arial", 16)); 
        detailsGrid.add(addressInfo, 0, 7);
        
        Label streetNoLabel = new Label("Street Number");
        TextField streetNo = new TextField("121");
        detailsGrid.add(streetNoLabel, 0, 8);
        detailsGrid.add(streetNo, 1, 8);
        
        Label streetNameLabel = new Label("Street Name");
        TextField streetName = new TextField("Clifwood Dr");
        detailsGrid.add(streetNameLabel, 0, 9);
        detailsGrid.add(streetName, 1, 9);
        
        Label suburbLabel = new Label("Suburb");
        TextField suburb = new TextField("Brentwood");
        detailsGrid.add(suburbLabel, 0, 10);
        detailsGrid.add(suburb, 1, 10);
        
        Label postCodeLabel = new Label("Post Code");
        TextField postCode = new TextField("9478");
        detailsGrid.add(postCodeLabel, 0, 11);
        detailsGrid.add(postCode, 1, 11);
                
        Button add = new Button("Add Relative");
        add.setOnAction(e -> {
            try {
                String relativeType = (String) relative.getValue();
                RadioButton GenderValue = (RadioButton) genderGroup.getSelectedToggle();
                String gender = GenderValue.getText();
                Address address = new Address(streetNo.getText(), streetName.getText(), suburb.getText(), postCode.getText());
                familyMember newMember = new familyMember(firstName.getText(), surname.getText(), familyMember.Gender.valueOf(gender), address, description.getText());
                newMember.setLegalSurname(maidenName.getText());

                /*
                Set new member to root if none,
                otherwise add as child node of root
                */
                if(!FamilyTree.hasRoot()) {
                    FamilyTree.setRoot(newMember);
                    displayRoot(newMember);
                }
                else {
                    newMember.setRelative(relativeType, newMember, FamilyTree.getRoot());
                    displayTree();
                }
                status.setText("Relative added");

                GridPane defaultPane = defaultPane();
                initPane.getChildren().setAll(defaultPane);
            } catch(Exception ex) {
                showErrDialog(ex);
            }
            
        });
        detailsGrid.add(add, 0, 12);
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            initPane.getChildren().setAll(initialPane);
            
        });
        detailsGrid.add(cancel, 1, 12);
        
        initPane.getChildren().setAll(detailsGrid);
    }
    
    /*
     * The pane to display when the tree is no longer empty
    */
    public GridPane defaultPane() {
        GridPane defaultP = new GridPane();
        defaultP.setPadding(new Insets(5,5,5,5));
        defaultP.setVgap(8);
        defaultP.setHgap(10);
        
        Text Message = new Text("Select a member to view their details.");
        Message.setWrappingWidth(240);
        Message.setFont(new Font("Arial", 16));
        
        defaultP.add(Message, 0, 0);
        
        return defaultP;
    }
    
    /*
     * Display tree when only root is created
    */
    public void displayRoot(familyMember rootMember) {
        root = new TreeItem();
        root.getChildren().add(new TreeItem<familyMember>(rootMember));
        root.setExpanded(true);
        tree.setRoot(root);
        tree.setShowRoot(false);
    }
    
    /*
     * Method to display tree loaded from file
    */
    public void displayTreeFromFile() {
        
        root = new TreeItem();
        TreeItem<familyMember> rootMember;
        
        if(!FamilyTree.hasRoot()) {
            rootMember = new TreeItem("Empty Tree");
        }
        else {
            rootMember = new TreeItem(FamilyTree.getRoot());
            rootMember.setExpanded(true);
            createTree(rootMember, FamilyTree.getRoot());
        }        
        
        root.getChildren().add(rootMember);
        root.setExpanded(true);
        tree.setRoot(root);
        tree.setShowRoot(false);
        
    }
    
    /*
     * Overloaded method that displays a new tree when new members are added
    */
    public void displayTree() {
        
        root = new TreeItem();
        TreeItem<familyMember> rootMember;
        
        if(!FamilyTree.hasRoot()) {
            rootMember = new TreeItem("No tree");
        }
        else {
            rootMember = new TreeItem(FamilyTree.getRoot());
            rootMember.setExpanded(true);
            createTree(rootMember, FamilyTree.getRoot());
        }        
        
        root.getChildren().add(rootMember);
        root.setExpanded(true);
        tree.setRoot(root);
        tree.setShowRoot(false);
        
    }
    
    /*
     * This method re-creates a tree when a relative is added
    */
    public void createTree(TreeItem<familyMember>rootMember, familyMember root) {
        
        if((root == FamilyTree.getRoot()) && root.hasParents()) {
            TreeItem parents = new TreeItem("Parents"); //create parent branch to store mother and father
            rootMember.getChildren().add(parents); //add branch to tree
            
            /*
             * Check if family member has a father or mother and add them to the tree
            */
            if(root.hasFather() == true) {
                TreeItem<familyMember> father = new TreeItem(root.getFather());
                father.setExpanded(true);
                parents.getChildren().add(father); 
            }
            
            if(root.hasMother() == true) {
                TreeItem<familyMember> mother = new TreeItem(root.getMother());
                mother.setExpanded(true);
                parents.getChildren().add(mother);
            }
            
            parents.setExpanded(true);
        }
        
        /*
         * Check for a spouse and add to the tree
        */
        TreeItem spouse = new TreeItem("Spouse");
        if(root.hasSpouse() == true) {
            TreeItem<familyMember> Spouse = new TreeItem(root.getSpouse());
            spouse.getChildren().add(Spouse);
            spouse.setExpanded(true);
            rootMember.getChildren().add(spouse);
        }        
        
        /*
         * Check for children and add to tree
        */
        if(root.hasChildren() == true) {
            TreeItem children = new TreeItem("Children");
            for(familyMember f : root.getChildren()) {
                TreeItem<familyMember> child = new TreeItem(f);
                createTree(child, f);
                child.setExpanded(true);
                children.getChildren().add(child);
                children.setExpanded(true);
            }
            rootMember.getChildren().add(children);
        }
    }
    
    /* 
     * Display the information of a family member when selected from the treeview
    */
    public void displaySelectedMember(TreeItem<familyMember> newvalue) {
        
        GridPane infoGrid = new GridPane();
        infoGrid.setPadding(new Insets(5,5,5,5));
        infoGrid.setVgap(8);
        infoGrid.setHgap(10);
        
        Label personInfo = new Label("Person Info");
        personInfo.setFont(new Font("Arial", 16));
        infoGrid.add(personInfo, 0, 0);
        
        TreeItem<familyMember> member = (TreeItem<familyMember>)newvalue;
        
        Label firstNameLabel = new Label("Name");
        Text firstName = new Text(member.getValue().getFirstName());
        infoGrid.add(firstNameLabel, 0, 1);
        infoGrid.add(firstName, 1, 1);
        
        Label surnameLabel = new Label("Surname");
        Text surname = new Text(member.getValue().getSurname());
        infoGrid.add(surnameLabel, 0, 2);
        infoGrid.add(surname, 1, 2);
        
        Label maidenNameLabel = new Label("Maiden Name");        
        Text maidenName = new Text(member.getValue().getlegalName());
        infoGrid.add(maidenNameLabel, 0, 3);
        infoGrid.add(maidenName, 1, 3);
        
        Label genderLabel = new Label("Gender");
        Text gender = new Text(member.getValue().getGender().toString());
        infoGrid.add(genderLabel, 0, 4);
        infoGrid.add(gender, 1, 4);
        
        Label descriptionLabel = new Label("Description");
        Text description = new Text(member.getValue().getDescription());
        infoGrid.add(descriptionLabel, 0, 5);
        infoGrid.add(description, 1, 5);
        
        Label addressInfo = new Label("Address");
        addressInfo.setFont(new Font("Arial", 16)); 
        infoGrid.add(addressInfo, 0, 6);
        
        Label streetNoLabel = new Label("Street Number");
        Text streetNo = new Text(member.getValue().getAddress().getStreetNumber());
        infoGrid.add(streetNoLabel, 0, 7);
        infoGrid.add(streetNo, 1, 7);
        
        Label streetNameLabel = new Label("Street Name");
        Text streetName = new Text(member.getValue().getAddress().getStreetName());
        infoGrid.add(streetNameLabel, 0, 8);
        infoGrid.add(streetName, 1, 8);
        
        Label suburbLabel = new Label("Suburb");
        Text suburb = new Text(member.getValue().getAddress().getSurburb());
        infoGrid.add(suburbLabel, 0, 9);
        infoGrid.add(suburb, 1, 9);
        
        Label postCodeLabel = new Label("Post Code");
        Text postCode = new Text(member.getValue().getAddress().getPostCode());
        infoGrid.add(postCodeLabel, 0, 10);
        infoGrid.add(postCode, 1, 10);
        
        Label relativeInfo = new Label("Relatives");
        relativeInfo.setFont(new Font("Arial", 16)); 
        infoGrid.add(relativeInfo, 0, 11);
        
        Label fatherLabel = new Label("Father");
        Text father;
        if(member.getValue().hasFather()){
            father = new Text(member.getValue().getFather().toString());
        }
        else {
            father = new Text("No father");
        }
        
        infoGrid.add(fatherLabel, 0, 12);
        infoGrid.add(father, 1, 12);
        
        Label motherLabel = new Label("Mother");
        Text mother;
        if(member.getValue().hasMother()){
            mother = new Text(member.getValue().getMother().toString());
        }
        else {
            mother = new Text("No mother");
        }
        infoGrid.add(motherLabel, 0, 13);
        infoGrid.add(mother, 1, 13);
        
        Label spouseLabel = new Label("Spouse");
        Text spouse;
        if(member.getValue().hasSpouse()){
            spouse = new Text(member.getValue().getSpouse().toString());
        }
        else {
            spouse = new Text("No spouse");
        }
        infoGrid.add(spouseLabel, 0, 14);
        infoGrid.add(spouse, 1, 14);
        
        Label childrenLabel = new Label("Children");
        Text children;
        if(member.getValue().hasChildren()){
            children = new Text(member.getValue().getChildren().toString());
        }
        else {
            children = new Text("No children");
        }
        infoGrid.add(childrenLabel, 0, 15);
        infoGrid.add(children, 1, 15);
        
        Label grandChildrenLabel = new Label("Grand Children");
        Text grandChildren = new Text("No grand children");
        infoGrid.add(grandChildrenLabel, 0, 16);
        infoGrid.add(grandChildren, 1, 16);
        
        Button add = new Button("Add Relative");
        add.setOnAction(e -> {
            addMember();
        });
        infoGrid.add(add, 0, 18);
        
        Button edit = new Button("Edit Details");
        edit.setOnAction(e -> {
            editMember(member);
        });
        infoGrid.add(edit, 1, 18);
        
        initPane.getChildren().setAll(infoGrid);
    }
    
    /*
     * Edit the details for the selected family member
    */
    public void editMember(TreeItem<familyMember> member) {
        
        GridPane detailsGrid = new GridPane();
        detailsGrid.setPadding(new Insets(5,5,5,5));
        detailsGrid.setVgap(8);
        detailsGrid.setHgap(10);
        
        Label personInfo = new Label("Person Info");
        personInfo.setFont(new Font("Arial", 16));
        detailsGrid.add(personInfo, 0, 0);
        
        Label firstNameLabel = new Label("Name");
        TextField firstName = new TextField(member.getValue().getFirstName());
        detailsGrid.add(firstNameLabel, 0, 2);
        detailsGrid.add(firstName, 1, 2);
        
        Label surnameLabel = new Label("Surname");
        TextField surname = new TextField(member.getValue().getSurname());
        detailsGrid.add(surnameLabel, 0, 3);
        detailsGrid.add(surname, 1, 3);
        
        Label maidenNameLabel = new Label("Maiden Name");        
        TextField maidenName = new TextField("");
        detailsGrid.add(maidenNameLabel, 0, 4);
        detailsGrid.add(maidenName, 1, 4);
        
        //restrict user from changing gender
        Label genderLabel = new Label("Gender");
        RadioButton male = new RadioButton(familyMember.Gender.Male.toString());
        male.setDisable(true);
        RadioButton female = new RadioButton(familyMember.Gender.Female.toString());
        female.setDisable(true);

        ToggleGroup genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        
        HBox genderSelection = new HBox(2);
        genderSelection.getChildren().addAll(male, female);
        detailsGrid.add(genderLabel, 0, 5);
        detailsGrid.add(genderSelection, 1, 5);
        
        Label descriptionLabel = new Label("Description");
        TextArea description = new TextArea(member.getValue().getDescription());
        description.setPrefColumnCount(10);
        description.setPrefRowCount(3);
        detailsGrid.add(descriptionLabel, 0, 6);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        detailsGrid.add(description, 1, 6);
        
        Label addressInfo = new Label("Address");
        addressInfo.setFont(new Font("Arial", 16)); 
        detailsGrid.add(addressInfo, 0, 7);
        
        Label streetNoLabel = new Label("Street Number");
        TextField streetNo = new TextField(member.getValue().getAddress().getStreetNumber());
        detailsGrid.add(streetNoLabel, 0, 8);
        detailsGrid.add(streetNo, 1, 8);
        
        Label streetNameLabel = new Label("Street Name");
        TextField streetName = new TextField(member.getValue().getAddress().getStreetName());
        detailsGrid.add(streetNameLabel, 0, 9);
        detailsGrid.add(streetName, 1, 9);
        
        Label suburbLabel = new Label("Surburb");
        TextField suburb = new TextField(member.getValue().getAddress().getSurburb());
        detailsGrid.add(suburbLabel, 0, 10);
        detailsGrid.add(suburb, 1, 10);
        
        Label postCodeLabel = new Label("PostCode");
        TextField postCode = new TextField(member.getValue().getAddress().getPostCode());
        detailsGrid.add(postCodeLabel, 0, 11);
        detailsGrid.add(postCode, 1, 11);
        
        Button update = new Button("Update Relative");
        update.setOnAction(e -> {
            try {
                member.getValue().setFirstName(firstName.getText());
                member.getValue().setSurname(surname.getText());
                member.getValue().setDescription(description.getText());
                member.getValue().setLegalSurname(maidenName.getText());
                
                member.getValue().getAddress().setStreetNumber(streetNo.getText());
                member.getValue().getAddress().setStreetName(streetName.getText());
                member.getValue().getAddress().setSuburb(suburb.getText());
                member.getValue().getAddress().setPostCode(postCode.getText());
                
                status.setText("Relative updated");
                displaySelectedMember(member);
            } 
            catch(Exception ex) {
                showErrDialog(ex);
            }            
        });
        detailsGrid.add(update, 0, 12);
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            displaySelectedMember(member);
        });
        detailsGrid.add(cancel, 1, 12);
        
        initPane.getChildren().setAll(detailsGrid);
    }
    
    public void showErrDialog(Exception ex) {
        Dialog<String> error = new Dialog<String>();
        ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
        
        error.setTitle("Error");
        error.setContentText(ex.getMessage());        
        error.getDialogPane().getButtonTypes().add(ok);
        error.showAndWait();
    }
}
