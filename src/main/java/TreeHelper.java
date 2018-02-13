
import java.util.*;

import static java.lang.Math.*;

public class TreeHelper implements TreeHelperInterface {

    private Random rng = new Random();

    private static class ConstructionHelper {
        private NodePrototypes mPrototypes;
        List<String> mLeaves;
        List<String> mFunctions;
        List<String> mBiFunctions;

        private Random rng = new Random();

        private <T> T pickRandom(List<T> list) {
            int item = rng.nextInt(list.size());
            int i = 0;
            for(T obj : list)
            {
                if (i == item)
                    return obj;
                i++;
            }
            return null;
        }

        public ConstructionHelper(NodePrototypes prototypes) {
            mPrototypes = prototypes;
            mLeaves = new ArrayList<>();
            mLeaves.addAll(prototypes.getConstants());
            mLeaves.addAll(prototypes.getVariables());
            mFunctions = new ArrayList<>();
            mFunctions.addAll(prototypes.getFunctions());
            mBiFunctions = new ArrayList<>();
            mBiFunctions.addAll(prototypes.getBiFunctions());
        }

        public NodeInterface getRandomLeaf() {
            return mPrototypes.get(pickRandom(mLeaves));
        }

        public NodeInterface getRandomFunction() {
            return mPrototypes.get(pickRandom(mFunctions));
        }

        public NodeInterface getRandomBiFunction() {
            return mPrototypes.get(pickRandom(mBiFunctions));
        }
    }

    private static class NodeWrapper {
        public NodeWrapper(NodeInterface node) {
            this.node = node;
        }
        public NodeInterface node;
    }

    private int nearestLeafDepth(NodeInterface root, int depth) {
        if (root.requiredChildNodes() == 0)
            return depth;
        if (root.requiredChildNodes() == 1) {
            try {
                return nearestLeafDepth(root.getChildNode(0), depth+1);
            } catch (StructureException exc) {}
        }
        try {
            return min(nearestLeafDepth(root.getChildNode(0), depth+1), nearestLeafDepth(root.getChildNode(1), depth+1));
        } catch (StructureException exc) {}
        return 0;
    }

    private boolean addLeaf(NodeWrapper wrapper, int depth, NodeInterface parent, int childIdx, ConstructionHelper helper) {
        try {
            if (wrapper.node.requiredChildNodes() == 2) {
                if (addLeaf(new NodeWrapper(wrapper.node.getChildNode(0)), depth-1, wrapper.node, 0, helper))
                    return true;
                return addLeaf(new NodeWrapper(wrapper.node.getChildNode(1)), depth-1, wrapper.node, 1, helper);
            } else if (wrapper.node.requiredChildNodes() == 1) {
                NodeInterface subtree = wrapper.node.getChildNode(0);
                wrapper.node = helper.getRandomBiFunction();
                int randInt = rng.nextInt(2);
                wrapper.node.setChildNode(randInt, subtree);
                wrapper.node.setChildNode(1-randInt, helper.getRandomLeaf());
                if (parent != null)
                    parent.setChildNode(childIdx, wrapper.node);
                return true;
            }
        } catch (StructureException exc) {}
        return false;
    }

    private void addNonLeafNode(NodeWrapper wrapper, int depth, NodeInterface parent, int childIdx, ConstructionHelper helper) {
        if (depth == 1)
            return;
        if (wrapper.node.requiredChildNodes() == 0) {
            try {
                NodeInterface old = wrapper.node;
                wrapper.node = helper.getRandomFunction();
                wrapper.node.setChildNode(0, old);
                if (parent != null)
                    parent.setChildNode(childIdx, wrapper.node);
            } catch (StructureException exc) {}
            return;
        } else if (wrapper.node.requiredChildNodes() == 1) {
            try {
                if (treeHeight(wrapper.node) < depth)
                    addNonLeafNode(new NodeWrapper(wrapper.node.getChildNode(0)), depth - 1, wrapper.node, 0, helper);
                else {
                    NodeInterface subtree = wrapper.node.getChildNode(0);
                    wrapper.node = helper.getRandomBiFunction();
                    int randInt = rng.nextInt(2);
                    wrapper.node.setChildNode(randInt, subtree);
                    wrapper.node.setChildNode(1-randInt, helper.getRandomLeaf());
                    if (parent != null)
                        parent.setChildNode(childIdx, wrapper.node);
                }
            } catch (StructureException exc) {}
            return;
        } else if (wrapper.node.requiredChildNodes() == 2 && depth == 2)
            return;
        try {
            int chosenChildIdx = 0;
            int leftSubtreeHeight = treeHeight(wrapper.node.getChildNode(0));
            int rightSubtreeHeight = treeHeight(wrapper.node.getChildNode(1));
            if (leftSubtreeHeight > rightSubtreeHeight)
                chosenChildIdx = 1;
            else if (leftSubtreeHeight == rightSubtreeHeight) {
                int leftLeafDepth = nearestLeafDepth(wrapper.node.getChildNode(0), 0);
                int rightLeafDepth = nearestLeafDepth(wrapper.node.getChildNode(1), 0);
                if (leftLeafDepth > rightLeafDepth)
                    chosenChildIdx = 1;
                else if (leftLeafDepth == rightLeafDepth) {
                    int leftNodes = nodes(wrapper.node.getChildNode(0));
                    int rightNodes = nodes(wrapper.node.getChildNode(1));
                    if (leftNodes > rightNodes)
                        chosenChildIdx = 1;
                    else
                        chosenChildIdx = rng.nextInt(2);
                }
            }
            addNonLeafNode(new NodeWrapper(wrapper.node.getChildNode(chosenChildIdx)), depth - 1, wrapper.node, chosenChildIdx, helper);
        } catch (StructureException exc) {}
    }

    private NodeInterface randomUniTree(int depth, ConstructionHelper helper) {
        if (depth > 1) {
            NodeInterface node = helper.getRandomFunction();
            try {
                node.setChildNode(0, randomUniTree(depth-1, helper));
            } catch (StructureException exception) {}
            return node;
        }
        return helper.getRandomLeaf();
    }

    @Override
    public NodeInterface construct(int nodes, int depth, int leafs, NodePrototypes prototypes) {
        ConstructionHelper helper = new ConstructionHelper(prototypes);
        NodeInterface tree;
        tree = randomUniTree(depth, helper);
        NodeWrapper wrapper = new NodeWrapper(tree);
        while (leafs - leafs(wrapper.node) != nodes - nodes(wrapper.node))
            addNonLeafNode(wrapper, depth, null, 0, helper);
        int leavesToAdd = leafs - leafs(wrapper.node);
        for (int i = 0 ; i < leavesToAdd ; ++i)
            addLeaf(wrapper, 0, null, 0, helper);
        return wrapper.node;
    }

    @Override
    public int treeHeight(NodeInterface root) {
        try {
            if (root.requiredChildNodes() == 2)
                return 1 + max(treeHeight(root.getChildNode(0)), treeHeight(root.getChildNode(1)));
            else if (root.requiredChildNodes() == 1)
                return 1 + treeHeight(root.getChildNode(0));
        } catch (Exception exc) {
            return 0;
        }
        return 1;
    }

    @Override
    public int leafs(NodeInterface root) {
        try {
            if (root.requiredChildNodes() == 2)
                return leafs(root.getChildNode(0)) + leafs(root.getChildNode(1));
            else if (root.requiredChildNodes() == 1)
                return leafs(root.getChildNode(0));
        } catch (Exception exc) {
            return 0;
        }
        return 1;
    }

    @Override
    public int nodes(NodeInterface root) {
        try {
            if (root.requiredChildNodes() == 2)
                return 1 + nodes(root.getChildNode(0)) + nodes(root.getChildNode(1));
            else if (root.requiredChildNodes() == 1)
                return 1 + nodes(root.getChildNode(0));
        } catch (Exception exc) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public boolean areEquivalents(NodeInterface root1, NodeInterface root2) {
        if (root1.getMathSymbol() != root2.getMathSymbol() || root1.requiredChildNodes() != root2.requiredChildNodes())
            return false;
        try {
            if (root1.requiredChildNodes() == 2) {
                if (areEquivalents(root1.getChildNode(0), root2.getChildNode(0)) && areEquivalents(root1.getChildNode(1), root2.getChildNode(1)))
                    return true;
                if (root1.hasSymmetry())
                    return areEquivalents(root1.getChildNode(1), root2.getChildNode(0)) && areEquivalents(root1.getChildNode(0), root2.getChildNode(1));
                return false;
            }
            else if (root1.requiredChildNodes() == 1)
                return areEquivalents(root1.getChildNode(0), root2.getChildNode(0));
        } catch (Exception exc) {
            return false;
        }
        return true;
    }
}
