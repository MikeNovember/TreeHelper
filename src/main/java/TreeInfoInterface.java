
public interface TreeInfoInterface {
	/**
	 * Wysokosc drzewa to maksymalny poziom w drzewie. Poziom korzenia to 1.
	 * Poziom wezlow-dzieci korzenia to 2. Poziom wezlow-dzieci dzieci korzenia to 3. itd.
	 * @param root wezel-korzen drzewa
	 * @return wysokosc drzewa, ktorego korzen to root
	 */
	public int treeHeight( NodeInterface root );
	
	/**
	 * Liczba wezlow-lisci, czyli wezlow, ktore same nie posiadaja dzieci.
	 * @param root wezel-korzen drzewa
	 * @return liczba wezlow-lisci w drzewie
	 */
	public int leafs( NodeInterface root );
	
	/**
	 * Liczba wezlow w drzewie. 
	 * @param root wezel-korzen drzewa
	 * @return liczba wezlow drzewa
	 */
	public int nodes( NodeInterface root );
}
