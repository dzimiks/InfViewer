package models.indseqTree;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tree implements Serializable {

    private NodeIndSeq rootElement;
    
    public Tree() {
        super();
    }
 
    public NodeIndSeq getRootElement() {
        return this.rootElement;
    }
 
    public void setRootElement(NodeIndSeq rootElement) {
        this.rootElement = rootElement;
    }
     

	
}
