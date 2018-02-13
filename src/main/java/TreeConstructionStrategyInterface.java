
public interface TreeConstructionStrategyInterface {
	/**
	 * Metoda konstrukcji drzewa. Metoda generuje drzewo o podanych parametrach.
	 * Dobor prototypow do budowy drzewa moze byc dowolny.
	 * 
	 * @param nodes
	 *            liczba wezlow, z ktorych drzewo ma byc zbudowane
	 * @param depth
	 *            glebokosc drzewa
	 * @param leafs
	 *            liczba lisci
	 * @param prototypes
	 *            obiekt zawierajacy prototypy wezlow, ktore nalezy uzyc do
	 *            konstrukcji drzewa
	 * @return drzewo zbudowane wg. przekazanych parametrow
	 */
	public NodeInterface construct(int nodes, int depth, int leafs, NodePrototypes prototypes);
}
