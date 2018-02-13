import java.util.function.DoubleBinaryOperator;

public class BiFunctionNode extends Node {

	private final DoubleBinaryOperator operator;
	private final boolean symmetry;

	public BiFunctionNode(String mathSymbol, DoubleBinaryOperator operator, boolean symmetry) {
		super(2, mathSymbol);
		this.operator = operator;
		this.symmetry = symmetry;
	}

	@Override
	public double getValue() throws MathError, StructureException {
		testChildNodesSet();

		try {
			return operator.applyAsDouble(getChildNode(0).getValue(), getChildNode(1).getValue());
		} catch (Exception e) {
			throw new MathError();
		}
	}

	@Override
	public NodeInterface deepClone() {
		return new BiFunctionNode(mathSymbol, operator, symmetry );
	}

	@Override
	public String toString() {
		try {
			return "( " + getChildNode(0) + " ) " + mathSymbol + " ( " + getChildNode(1) + " )";
		} catch (Exception e) {
			return "STRUCTURE ERROR";
		}
	}

	@Override
	public boolean hasSymmetry() {
		return symmetry;
	}
}
