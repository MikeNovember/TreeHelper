import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PMO_Test {

	//////////////////////////////////////////////////////////////////////////
	private static final Map<String, Double> tariff = new HashMap<>();

	static {
		tariff.put("TreeInfoInterfaceLeafsTest", 2.0);
		tariff.put("TreeInfoInterfaceNodesTest", 2.0);
		tariff.put("TreeInfoInterfaceHeightTest", 2.0);
		tariff.put("constructionTest1", 0.4);
		tariff.put("constructionTest2", 0.4);
		tariff.put("constructionTest3", 0.4);
		tariff.put("constructionTest4", 0.4);
		tariff.put("equivalanceTest1", 1.0);
		tariff.put("equivalanceTest2", 1.0);
	}

	public static double getTariff(String testName) {
		return tariff.get(testName);
	}
	//////////////////////////////////////////////////////////////////////////

	private TreeHelperInterface thelp;

	private NodePrototypes prototypes = NodePrototypes.getNodePrototypes();

	@Before
	public void createPrototypes() {

		thelp = (TreeHelperInterface) PMO_GeneralPurposeFabric.fabric("TreeHelper", "TreeHelperInterface");

		prototypes.addPrototype(new BiFunctionNode("+", (x, y) -> x + y, true));
		prototypes.addPrototype(new BiFunctionNode("-", (x, y) -> x - y, false));
		prototypes.addPrototype(new BiFunctionNode("*", (x, y) -> x * y, true));
		prototypes.addPrototype(new BiFunctionNode("min", (x, y) -> Math.min(x, y), true));
		prototypes.addPrototype(new BiFunctionNode("max", (x, y) -> Math.max(x, y), true));

		prototypes.addPrototype(new FunctionNode("sin", (x) -> Math.sin(x)));
		prototypes.addPrototype(new FunctionNode("cos", (x) -> Math.cos(x)));
		prototypes.addPrototype(new FunctionNode("abs", (x) -> Math.abs(x)));
		prototypes.addPrototype(new FunctionNode("exp", (x) -> Math.exp(x)));

		prototypes.addPrototype(new ConstantNode("1.0", 1.0));
		prototypes.addPrototype(new ConstantNode("2.0", 2.0));
		prototypes.addPrototype(new ConstantNode("5.0", 5.0));
		prototypes.addPrototype(new ConstantNode("10.0", 10.0));
		prototypes.addPrototype(new ConstantNode("0.1", 0.1));

		prototypes.addPrototype(new VariableNode("x"));
		prototypes.addPrototype(new VariableNode("y"));

		PMO_SystemOutRedirect.println("Prototypes ready...");

	}

	private NodeInterface tree1() {

		// abs( sin( cos( 1 + max( 2 * x, 10 ) ) ) )

		NodeInterface sin = prototypes.get("sin");
		NodeInterface cos = prototypes.get("cos");
		NodeInterface abs = prototypes.get("abs");
		NodeInterface max = prototypes.get("max");
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface x = prototypes.get("x");
		NodeInterface add = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");

		try {
			abs.setChildNode(0, sin);
			sin.setChildNode(0, cos);
			cos.setChildNode(0, add);
			add.setChildNode(0, c1);
			add.setChildNode(1, max);
			max.setChildNode(0, mult);
			max.setChildNode(1, c10);
			mult.setChildNode(0, c2);
			mult.setChildNode(1, x);
		} catch (StructureException e) {
			e.printStackTrace();
		}
		PMO_SystemOutRedirect.println("Wzor " + abs);
		return abs;
	}

	private NodeInterface tree2() {

		// abs( abs( sin( cos( abs( 1 + 2 ) ) ) ) )

		NodeInterface sin = prototypes.get("sin");
		NodeInterface cos = prototypes.get("cos");
		NodeInterface abs1 = prototypes.get("abs");
		NodeInterface abs2 = prototypes.get("abs");
		NodeInterface abs3 = prototypes.get("abs");
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface add = prototypes.get("+");

		try {
			abs1.setChildNode(0, abs2);
			abs2.setChildNode(0, sin);
			sin.setChildNode(0, cos);
			cos.setChildNode(0, abs3);
			abs3.setChildNode(0, add);
			add.setChildNode(0, c1);
			add.setChildNode(1, c2);
		} catch (StructureException e) {
			e.printStackTrace();
		}
		PMO_SystemOutRedirect.println("Wzor " + abs1);
		return abs1;
	}

	private NodeInterface tree3() {

		// sin{ [ ( 1 + 2 ) + ( 1 + 2 ) ] + [ ( 1 + 2 ) + ( 1 + 2 ) ] }
		// 1 2 3 4 5 6 7
		NodeInterface sin = prototypes.get("sin");
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface add1 = prototypes.get("+");
		NodeInterface add2 = prototypes.get("+");
		NodeInterface add3 = prototypes.get("+");
		NodeInterface add4 = prototypes.get("+");
		NodeInterface add5 = prototypes.get("+");
		NodeInterface add6 = prototypes.get("+");
		NodeInterface add7 = prototypes.get("+");

		try {
			sin.setChildNode(0, add4);
			add4.setChildNode(0, add2);
			add4.setChildNode(1, add6);
			add2.setChildNode(0, add1);
			add2.setChildNode(1, add3);
			add6.setChildNode(0, add5);
			add6.setChildNode(1, add7);
			add1.setChildNode(0, c1);
			add1.setChildNode(1, c2);
			add3.setChildNode(0, c1);
			add3.setChildNode(1, c2);
			add5.setChildNode(0, c1);
			add5.setChildNode(1, c2);
			add7.setChildNode(0, c1);
			add7.setChildNode(1, c2);
		} catch (StructureException e) {
			e.printStackTrace();
		}
		try {
			PMO_SystemOutRedirect.println("Wzor " + sin + " wartosc " + sin.getValue());
		} catch (MathError | StructureException e) {
			e.printStackTrace();
		}
		return sin;
	}

	private void testLeafs(NodeInterface root, int expected) {
		int value = PMO_TestHelper.tryExecute(() -> thelp.leafs(root), "leafs");

		assertEquals("Blad w wyznaczaniu liczby lisci", expected, value);
	}

	private void testNodes(NodeInterface root, int expected) {
		int value = PMO_TestHelper.tryExecute(() -> thelp.nodes(root), "nodes");

		assertEquals("Blad w wyznaczaniu liczby wezlow", expected, value);
	}

	private void testHight(NodeInterface root, int expected) {
		int value = PMO_TestHelper.tryExecute(() -> thelp.treeHeight(root), "treeHeight");

		assertEquals("Blad w wyznaczaniu wysokosci drzewa", expected, value);
	}

	private NodeInterface construct(int nodes, int depth, int leafs) {
		NodeInterface root = PMO_TestHelper.tryExecute(() -> thelp.construct(nodes, depth, leafs, prototypes),
				"construct");

		assertNotNull("Metoda constuct zwrocila NULL", root);

		return root;
	}

	@Test(timeout = 1000)
	public void TreeInfoInterfaceLeafsTest() {
		NodeInterface root = tree1();
		testLeafs(root, 4);
		root = tree2();
		testLeafs(root, 2);
		root = tree3();
		testLeafs(root, 8);
	}

	@Test(timeout = 1000)
	public void TreeInfoInterfaceNodesTest() {
		NodeInterface root = tree1();
		testNodes(root, 10);
		root = tree2();
		testNodes(root, 8);
		root = tree3();
		testNodes(root, 16);
	}

	@Test(timeout = 1000)
	public void TreeInfoInterfaceHeightTest() {
		NodeInterface root = tree1();
		testHight(root, 7);
		root = tree2();
		testHight(root, 7);
		root = tree3();
		testHight(root, 5);
	}

	private void constructAndTest(int nodes, int depth, int leafs) {
		NodeInterface root = construct(nodes, depth, leafs);

		String equation = root.toString();

		System.out.println(equation);

		assertFalse("Blad w wygenerowanym wzorze " + equation, equation.toString().toLowerCase().contains("null"));

		try {
			root.getValue();
		} catch (StructureException e) {
			fail("Blad we wzorze - pojawi sie wyjatek StructureException");
		} catch (Exception e) {
		}

		testHight(root, depth);
		testLeafs(root, leafs);
		testNodes(root, nodes);
	}

	@Test
	public void constructionTest1() {
		constructAndTest(11, 5, 3);
	}

	@Test
	public void constructionTest2() {
		constructAndTest(14, 5, 5);
	}

	@Test
	public void constructionTest3() {
		constructAndTest(24, 5, 9);
	}

	@Test
	public void constructionTest4() {
		constructAndTest(14, 8, 7);
	}

	private NodeInterface equivalanceTestGen1_1() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");

		try {
			mult.setChildNode(0, add);
			mult.setChildNode(1, c10);
			add.setChildNode(0, c1);
			add.setChildNode(1, c2);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return mult;
	}

	private NodeInterface equivalanceTestGen1_2() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");

		try {
			mult.setChildNode(1, add);
			mult.setChildNode(0, c10);
			add.setChildNode(1, c1);
			add.setChildNode(0, c2);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return mult;
	}

	private NodeInterface equivalanceTestGen1_3() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");

		try {
			mult.setChildNode(0, add);
			mult.setChildNode(1, c10);
			add.setChildNode(1, c1);
			add.setChildNode(0, c2);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return mult;
	}

	private NodeInterface equivalanceTestGen1_4() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface sub = prototypes.get("-");
		NodeInterface mult = prototypes.get("*");

		try {
			mult.setChildNode(0, sub);
			mult.setChildNode(1, c10);
			sub.setChildNode(0, c1);
			sub.setChildNode(1, c2);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return mult;
	}

	private void executeEquivalanceTest(NodeInterface root1, NodeInterface root2, boolean expected) {
		Boolean result;

		result = PMO_TestHelper.tryExecute(() -> thelp.areEquivalents(root1, root2), "areEquivalents");

		assertNotNull("Wynik porownania areEquivalents nie moze byc null", result);

		assertEquals("Oczekiwano innego wyniku porównania drzew", expected, result);
	}

	@Test
	public void equivalanceTest1() {
		NodeInterface root = equivalanceTestGen1_1();
		NodeInterface rootS1 = equivalanceTestGen1_2();
		NodeInterface rootS2 = equivalanceTestGen1_3();
		NodeInterface rootAS = equivalanceTestGen1_4();

		executeEquivalanceTest(root, rootS1, true);
		executeEquivalanceTest(root, rootS2, true);
		executeEquivalanceTest(rootS1, rootS2, true);
		executeEquivalanceTest(root, rootAS, false);
	}

	private NodeInterface equivalanceTestGen2_1() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c5 = prototypes.get("5.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add1 = prototypes.get("+");
		NodeInterface add2 = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");
		NodeInterface sub = prototypes.get("-");
		NodeInterface max = prototypes.get("max");
		NodeInterface sin = prototypes.get("sin");

		try {
			add1.setChildNode(0, max);
			add1.setChildNode(1, sin);
			sin.setChildNode(0, mult);
			mult.setChildNode(0, add2);
			mult.setChildNode(1, sub);
			add2.setChildNode(0, c1);
			add2.setChildNode(1, c2);
			sub.setChildNode(0, c10);
			sub.setChildNode(1, c2);
			max.setChildNode(0, c2);
			max.setChildNode(1, c5);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return add1;
	}

	private NodeInterface equivalanceTestGen2_2() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c5 = prototypes.get("5.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add1 = prototypes.get("+");
		NodeInterface add2 = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");
		NodeInterface sub = prototypes.get("-");
		NodeInterface max = prototypes.get("max");
		NodeInterface sin = prototypes.get("sin");

		try {
			add1.setChildNode(0, max);
			add1.setChildNode(1, sin);
			sin.setChildNode(0, mult);
			mult.setChildNode(0, add2);
			mult.setChildNode(1, sub);
			add2.setChildNode(0, c1);
			add2.setChildNode(1, c2);
			sub.setChildNode(0, c10);
			sub.setChildNode(1, c2);
			max.setChildNode(1, c2);
			max.setChildNode(0, c5);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return add1;
	}

	private NodeInterface equivalanceTestGen2_3() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c5 = prototypes.get("5.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add1 = prototypes.get("+");
		NodeInterface add2 = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");
		NodeInterface sub = prototypes.get("-");
		NodeInterface max = prototypes.get("max");
		NodeInterface sin = prototypes.get("sin");

		try {
			add1.setChildNode(1, max);
			add1.setChildNode(0, sin);
			sin.setChildNode(0, mult);
			mult.setChildNode(0, add2);
			mult.setChildNode(1, sub);
			add2.setChildNode(0, c1);
			add2.setChildNode(1, c2);
			sub.setChildNode(0, c10);
			sub.setChildNode(1, c2);
			max.setChildNode(1, c2);
			max.setChildNode(0, c5);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return add1;
	}

	private NodeInterface equivalanceTestGen2_4() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c5 = prototypes.get("5.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add1 = prototypes.get("+");
		NodeInterface add2 = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");
		NodeInterface sub = prototypes.get("-");
		NodeInterface max = prototypes.get("max");
		NodeInterface sin = prototypes.get("sin");

		try {
			add1.setChildNode(0, max);
			add1.setChildNode(1, sin);
			sin.setChildNode(0, mult);
			mult.setChildNode(0, add2);
			mult.setChildNode(1, sub);
			add2.setChildNode(0, c1);
			add2.setChildNode(1, c2);
			sub.setChildNode(1, c10);
			sub.setChildNode(0, c2);
			max.setChildNode(1, c2);
			max.setChildNode(0, c5);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return add1;
	}

	private NodeInterface equivalanceTestGen2_5() {
		NodeInterface c1 = prototypes.get("1.0");
		NodeInterface c2 = prototypes.get("2.0");
		NodeInterface c5 = prototypes.get("5.0");
		NodeInterface c10 = prototypes.get("10.0");
		NodeInterface add1 = prototypes.get("+");
		NodeInterface add2 = prototypes.get("+");
		NodeInterface mult = prototypes.get("*");
		NodeInterface sub = prototypes.get("-");
		NodeInterface max = prototypes.get("max");
		NodeInterface cos = prototypes.get("cos");

		try {
			add1.setChildNode(0, max);
			add1.setChildNode(1, cos);
			cos.setChildNode(0, mult);
			mult.setChildNode(0, add2);
			mult.setChildNode(1, sub);
			add2.setChildNode(0, c1);
			add2.setChildNode(1, c2);
			sub.setChildNode(0, c10);
			sub.setChildNode(1, c2);
			max.setChildNode(1, c2);
			max.setChildNode(0, c5);
		} catch (StructureException e) {
			e.printStackTrace();
		}

		return add1;
	}

	@Test
	public void equivalanceTest2() {
		NodeInterface root = equivalanceTestGen2_1();
		NodeInterface rootS1 = equivalanceTestGen2_2();
		NodeInterface rootS2 = equivalanceTestGen2_3();
		NodeInterface rootAS1 = equivalanceTestGen2_4();
		NodeInterface rootAS2 = equivalanceTestGen2_5();

		executeEquivalanceTest(root, rootS1, true);
		executeEquivalanceTest(root, rootS2, true);
		executeEquivalanceTest(rootS1, rootS2, true);
		executeEquivalanceTest(root, rootAS1, false);
		executeEquivalanceTest(root, rootAS2, false);
		executeEquivalanceTest(rootAS1, rootAS2, false);
	}

}
