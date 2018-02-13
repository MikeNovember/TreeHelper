
public class VariableNode extends Node {
	private final Variables vars = Variables.getVariables();

	public VariableNode(String variableName) {
		this(variableName, 0);
	}

	public VariableNode(String variableName, double initialValue) {
		super(0, variableName);
		vars.setValue(variableName, initialValue);
	}

	@Override
	public double getValue() throws MathError, StructureException {
		return vars.getValue(getMathSymbol());
	}

	@Override
	public NodeInterface deepClone() {
		return this;
	}

	@Override
	public String toString() {

		//System.out.println(">>> " + getMathSymbol());

		return getMathSymbol();
	}
}
