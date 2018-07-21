package models.indseqTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;


@SuppressWarnings("serial")
public class NodeIndSeq implements TreeNode, Serializable {

    public List<NodeElement>  data;
    public List<NodeIndSeq> children;
 
    
    public NodeIndSeq() {
    	
    	
        
        data=new ArrayList<NodeElement>();
        children=new ArrayList<NodeIndSeq>();
    }
 
 

    public NodeIndSeq(List<NodeElement>  data) {
        this();
        setData(data);
    } 
    
    
    public void addChild(NodeIndSeq child) {
        if (children == null) {
            children = new ArrayList<NodeIndSeq>();
        }
        children.add(child);
    }
    
    
	public Enumeration children() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return true;
	}

	public TreeNode getChildAt(int childIndex) {
		// TODO Auto-generated method stub
		 return this.children.get(childIndex);
	}

	public int getChildCount() {
		// TODO Auto-generated method stub
        if (children == null) {
            return 0;
        }
        return children.size();
	}

	public int getIndex(TreeNode node) {
		// TODO Auto-generated method stub
		return children.indexOf(node);
	}

	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLeaf() {
		if(this.getChildCount() == 0){
			return true;
		}
		return false;
	}
	
    public List<NodeElement> getData() {
        return this.data;
    }
 
    public void setData(List<NodeElement> data) {
        this.data = data;
    }
    
    public void addData(NodeElement data){
    	this.data.add(data);
    }
    
    public List<NodeIndSeq> getChildren() {
        if (this.children == null) {
            return new ArrayList<NodeIndSeq>();
        }
        
        return this.children;
    }
 

    public void setChildren(List<NodeIndSeq> children) {
        this.children = children;
    }
    
    
	public NodeIndSeq clone(){
	    List<NodeElement>  dataCopy=new ArrayList<NodeElement>();
	    for (int i=0;i<data.size();i++){
	    	NodeElement nodeElement=data.get(i).clone();
	    	dataCopy.add(nodeElement);
	    }
	    List<NodeIndSeq> childrenCopy=new ArrayList<NodeIndSeq>();		
	    for (int i=0;i<children.size();i++){
	    	NodeIndSeq node=children.get(i).clone();
	    	childrenCopy.add(node);
	    }	
	    
	    NodeIndSeq nodeCopy=new NodeIndSeq();
	    nodeCopy.setChildren(childrenCopy);
	    nodeCopy.setData(dataCopy);
	    return nodeCopy;
	}

}
