
public class ConstantNode extends Node {

	private final double value;

	public ConstantNode(String mathSymbol, double value) {
		super(0, mathSymbol);
		this.value = value;
	}

	@Override
	public double getValue() throws MathError, StructureException {
		return value;
	}

	@Override
	public NodeInterface deepClone() {
		return this;
	}

	@Override
	public String toString() {
		return mathSymbol;
	}

}
