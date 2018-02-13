import java.util.function.DoubleUnaryOperator;

public class FunctionNode extends Node {

	private final DoubleUnaryOperator operator;

	public FunctionNode(String mathSymbol, DoubleUnaryOperator operator) {
		super(1, mathSymbol);
		this.operator = operator;
	}

	@Override
	public double getValue() throws MathError, StructureException {
		testChildNodesSet();

		try {
			return operator.applyAsDouble(getChildNode(0).getValue());
		} catch (Exception e) {
			throw new MathError();
		}
	}

	@Override
	public NodeInterface deepClone() {
		return new FunctionNode(mathSymbol, operator);
	}

	@Override
	public String toString() {
		try {
			return mathSymbol + "(" + getChildNode(0) + ")";
		} catch (StructureException e) {
			return "STRUCTURE ERROR";
		}
	}

}
