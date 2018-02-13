
public class Example {
	public static void main(String[] args) throws StructureException, MathError {

		NodePrototypes prototypes = NodePrototypes.getNodePrototypes();

		prototypes.addPrototype(new BiFunctionNode("+", (x, y) -> x + y, true));
		prototypes.addPrototype(new BiFunctionNode("-", (x, y) -> x - y, false));
		prototypes.addPrototype(new BiFunctionNode("*", (x, y) -> x * y, true));
		prototypes.addPrototype(new FunctionNode("sin", (x) -> Math.sin(x)));
		prototypes.addPrototype(new FunctionNode("cos", (x) -> Math.cos(x)));
		prototypes.addPrototype(new ConstantNode("2", 2));
		prototypes.addPrototype(new VariableNode("x"));
		prototypes.addPrototype(new VariableNode("y"));

		// skladamy y + 2 * sin( x )
		
		System.out.println( "Stałe    : " + prototypes.getConstants() );
		System.out.println( "Zmienne  : " + prototypes.getVariables() );
		System.out.println( "Funkcje  : " + prototypes.getFunctions() );
		System.out.println( "BiFunkcje: " + prototypes.getBiFunctions() );

		NodeInterface sin = prototypes.get("sin");
		sin.setChildNode(0, prototypes.get("x"));

		NodeInterface mult = prototypes.get("*");
		mult.setChildNode(0, prototypes.get("2"));
		mult.setChildNode(1, sin);

		NodeInterface plusC = prototypes.get("+");
		plusC.setChildNode(0, prototypes.get("y"));
		plusC.setChildNode(1, mult);

		System.out.println("Wzor : " + plusC);

		Variables vars = Variables.getVariables();

		vars.setValue("y", 0.0);
		for (int i = 0; i < 16; i++) {
			vars.setValue("x", i / 5.0);
			System.out.println(vars.getValue("x") + " --> " + plusC.getValue());
		}

		vars.setValue("y", 10.0);
		for (int i = 0; i < 16; i++) {
			vars.setValue("x", i / 5.0);
			System.out.println(vars.getValue("x") + " --> " + plusC.getValue());
		}
	}
}
