import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Klasa zawierajaca prototypy wezlow. Do budowy grafu nalezy uzywac kopii
 * wezlow-prototypow. Wezly przechowywane sa w czterech kolekcjach: sa to
 * <ul>
 * <li>Kolekcja stalych
 * <li>Kolekcja zmiennych
 * <li>Kolekcja funkcji
 * <li>Kolekcja funkcji dwuargumentowych
 * </ul>
 */
public class NodePrototypes {

	private Set<String> constants = new TreeSet<>();
	private Set<String> variables = new TreeSet<>();
	private Set<String> functions = new TreeSet<>();
	private Set<String> biFunctions = new TreeSet<>();
	private Map<String, NodeInterface> nodes = new TreeMap<>();
	private static NodePrototypes ref;

	private NodePrototypes() {

	}

	public static NodePrototypes getNodePrototypes() {
		if (ref == null)
			ref = new NodePrototypes();
		return ref;
	}

	private void add(Set<String> set, NodeInterface prototype) {
		set.add(prototype.getMathSymbol());
		nodes.put(prototype.getMathSymbol(), prototype);
	}

	/**
	 * Metoda pozwala na dodanie prototypu wezla. Wezly dodawane sa do wspolnej
	 * mapy, ktorej kluczem jest symbol matematyczny wezla.
	 * 
	 * @param prototype
	 *            prototyp dodawanego wezla
	 */
	public void addPrototype(NodeInterface prototype) {
		if (prototype instanceof ConstantNode) {
			add(constants, prototype);
		}
		if (prototype instanceof VariableNode) {
			add(variables, prototype);
		}
		if (prototype instanceof FunctionNode) {
			add(functions, prototype);
		}
		if (prototype instanceof BiFunctionNode) {
			add(biFunctions, prototype);
		}
	}

	/**
	 * Metoda zwraca zbior symboli stalych.
	 * 
	 * @return zbior stalych
	 */
	public Set<String> getConstants() {
		return constants;
	}

	/**
	 * Metoda zwraca zbior symboli zmiennych.
	 * 
	 * @return zbior zmiennych
	 */
	public Set<String> getVariables() {
		return variables;
	}

	/**
	 * Metoda zwraca zbior symboli funkcji
	 * 
	 * @return zbior funkcji
	 */
	public Set<String> getFunctions() {
		return functions;
	}

	/**
	 * Metoda zwraca zbior symboli bi-funkcji
	 * 
	 * @return zbior bi-funkcji
	 */
	public Set<String> getBiFunctions() {
		return biFunctions;
	}

	/**
	 * Metoda zwraca klon wezla o podanym symbolu matematycznym.
	 * 
	 * @param name
	 *            symbol pobieranego wezla
	 * @return klon wezla o nazwie name
	 */
	public NodeInterface get(String name) {
		return nodes.get(name).deepClone();
	}
}
