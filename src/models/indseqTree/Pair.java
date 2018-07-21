package models.indseqTree;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Pair implements Serializable {
	private NodeIndSeq node;
	private boolean last;
	
	public Pair(NodeIndSeq node, boolean last) {
		this.node = node;
		this.last = last;
	}

	public NodeIndSeq getNode() {
		return node;
	}

	public void setNode(NodeIndSeq node) {
		this.node = node;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
	
}
