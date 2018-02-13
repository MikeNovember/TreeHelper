/**
 * Implementacja podstawowych wlasnosci NodeInterface
 */
public abstract class Node implements NodeInterface {
	private int childNodesRequired;
	protected NodeInterface[] child;
	protected String mathSymbol;

	public Node(int childNodesRequired, String mathSymbol) {
		this.childNodesRequired = childNodesRequired;
		this.mathSymbol = mathSymbol;
		if (childNodesRequired > 0) {
			child = new NodeInterface[childNodesRequired];
		}
	}

	private void testChildID(int childID) throws StructureException {
		if (childID >= childNodesRequired)
			throw new StructureException(StructureException.ExceptionCause.CHILD_ID_TOO_BIG);
		if (childID < 0)
			throw new StructureException(StructureException.ExceptionCause.CHILD_ID_TOO_SMALL);
	}

	protected void testChildNodesSet() throws StructureException {
		if (childNodesRequired > 0) {
			if (childNodesRequired > 0)
				if (child[0] == null)
					throw new StructureException(StructureException.ExceptionCause.CHILD_NODE_NOT_SET);
			if (childNodesRequired > 1)
				if (child[1] == null)
					throw new StructureException(StructureException.ExceptionCause.CHILD_NODE_NOT_SET);
		}
	}

	@Override
	public NodeInterface getChildNode(int childID) throws StructureException {
		testChildID(childID);
		return child[childID];
	}

	@Override
	public void setChildNode(int childID, NodeInterface child) throws StructureException {
		testChildID(childID);
		this.child[childID] = child;
	}

	@Override
	public int requiredChildNodes() {
		return childNodesRequired;
	}

	@Override
	public String getMathSymbol() {
		return mathSymbol;
	}
	
	@Override
	public boolean hasSymmetry() {
		return true;
	}

}
