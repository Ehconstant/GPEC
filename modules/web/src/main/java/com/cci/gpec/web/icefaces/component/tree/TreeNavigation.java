package com.cci.gpec.web.icefaces.component.tree;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.component.tree.Tree;


/**
 * The TreeNavigation class is the backing bean for the navigation
 * tree on the left hand side of the application. Each node in the tree is made
 * up of a PageContent which is responsible for the navigation action when a
 * tree node is selected.
 * 
 * When the Tree component binding takes place the tree nodes are initialized
 * and the tree is built.  Any addition to the tree navigation must be made to
 * this class.
 *
 */
public class TreeNavigation {
    
    // binding to component
    private Tree treeComponent;

    // bound to components value attribute
    private DefaultTreeModel model;

    // root node of tree, for delayed construction
    private DefaultMutableTreeNode rootTreeNode;


    // initialization flag
    private boolean isInitiated;

    /**
     * Default constructor of the tree.  The root node of the tree is created at
     * this point.
     */
    public TreeNavigation() {
    	ResourceBundle  bundle = ResourceBundle.getBundle("menu");
    	rootTreeNode = new DefaultMutableTreeNode();
    	IceUserObjectExtend rootObject = new IceUserObjectExtend(rootTreeNode);
        rootObject.setText(bundle.getString("Parametres"));

        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        model = new DefaultTreeModel(rootTreeNode);

    }
    
    /**
     * Utility method to build the entire navigation tree.
     * @throws UnsupportedEncodingException 
     */
    private void init() throws UnsupportedEncodingException {
        // set init flag
        isInitiated = true;

        if (rootTreeNode != null) {
        	ResourceBundle  bundle = ResourceBundle.getBundle("menu");
        	
        	// add ICEsoft URL child node
            DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
            IceUserObjectExtend branchObject = new IceUserObjectExtend(branchNode);
            branchObject.setText(bundle.getString("Groupes"));
            branchObject.setAction("groupe");
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);

            // add Google URL child node
            branchNode = new DefaultMutableTreeNode();
            branchObject = new IceUserObjectExtend(branchNode);
            branchObject.setText(bundle.getString("Entreprises"));
            branchObject.setAction("entreprises");
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);

            // add Google URL child node
            branchNode = new DefaultMutableTreeNode();
            branchObject = new IceUserObjectExtend(branchNode);
            branchObject.setText(bundle.getString("Services"));
            branchObject.setAction("services");
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);
        	
            branchNode = new DefaultMutableTreeNode();
            branchObject = new IceUserObjectExtend(branchNode);
            branchObject.setText(bundle.getString("Metiers"));
            branchObject.setAction("metiers");
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);
            
//            branchNode = new DefaultMutableTreeNode();
//            branchObject = new IceUserObject(branchNode);
//            branchObject.setText("Catégories socio professionnelles");
//            branchObject.setAction("statuts");
//            branchObject.setLeaf(true);
//            branchNode.setUserObject(branchObject);
//            rootTreeNode.add(branchNode);
            
            branchNode = new DefaultMutableTreeNode();
            branchObject = new IceUserObjectExtend(branchNode);
            branchObject.setText(bundle.getString("Absence"));
            branchObject.setAction("typesAbsences");
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);
            
//            branchNode = new DefaultMutableTreeNode();
//            branchObject = new IceUserObject(branchNode);
//            branchObject.setText("Domaines de formation");
//            branchObject.setAction("domainesFormation");
//            branchObject.setLeaf(true);
//            branchNode.setUserObject(branchObject);
//            rootTreeNode.add(branchNode);
            
            branchNode = new DefaultMutableTreeNode();
            branchObject = new IceUserObjectExtend(branchNode);
            branchObject.setText(bundle.getString("Habilitation"));
            branchObject.setAction("typesHabilitation");
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);
            
//            branchNode = new DefaultMutableTreeNode();
//            branchObject = new IceUserObject(branchNode);
//            branchObject.setText(bundle.getString("Accident"));
//            branchObject.setAction("typesAccident");
//            branchObject.setLeaf(true);
//            branchNode.setUserObject(branchObject);
//            rootTreeNode.add(branchNode);
//            
//            branchNode = new DefaultMutableTreeNode();
//            branchObject = new IceUserObject(branchNode);
//            branchObject.setText(bundle.getString("Lesion"));
//            branchObject.setAction("typesLesion");
//            branchObject.setLeaf(true);
//            branchNode.setUserObject(branchObject);
//            rootTreeNode.add(branchNode);
            
//            branchNode = new DefaultMutableTreeNode();
//            branchObject = new IceUserObject(branchNode);
//            branchObject.setText("Types de contrat");
//            branchObject.setAction("typesContrat");
//            branchObject.setLeaf(true);
//            branchNode.setUserObject(branchObject);
//            rootTreeNode.add(branchNode);
//            
//            branchNode = new DefaultMutableTreeNode();
//            branchObject = new IceUserObject(branchNode);
//            branchObject.setText(bundle.getString("Cause Accident"));
//            branchObject.setAction("typesCausesAccident");
//            branchObject.setLeaf(true);
//            branchNode.setUserObject(branchObject);
//            rootTreeNode.add(branchNode);

            branchNode = new DefaultMutableTreeNode();
          branchObject = new IceUserObjectExtend(branchNode);
          branchObject.setText(bundle.getString("ParamsGeneraux"));
          branchObject.setAction("paramsGeneraux");
          branchObject.setLeaf(true);
          branchNode.setUserObject(branchObject);
          rootTreeNode.add(branchNode);
        }
        
    }
    /**
     * Gets the default tree model.  This model is needed for the value
     * attribute of the tree component.
     *
     * @return default tree model used by the navigation tree
     */
    public DefaultTreeModel getModel() {
        return model;
    }

    /**
     * Sets the default tree model.
     *
     * @param model new default tree model
     */
    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }

    /**
     * Gets the tree component binding.
     *
     * @return tree component binding
     */
    public Tree getTreeComponent() {
        return treeComponent;
    }

    /**
     * Sets the tree component binding.
     *
     * @param treeComponent tree component to bind to
     * @throws UnsupportedEncodingException 
     */
    public void setTreeComponent(Tree treeComponent) throws UnsupportedEncodingException {
        this.treeComponent = treeComponent;
        if (!isInitiated) {
            init();
        }
    }
}
