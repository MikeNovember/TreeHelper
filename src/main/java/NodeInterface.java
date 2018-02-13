public interface NodeInterface {
	/**
	 * Metoda zwraca referencje do wezla-potomny o wskazanym identyfikatorze
	 * childID.
	 *
	 * @param childID
	 *            numer wezla potomnego
	 * @return wezel potomny
	 * @throws StructureException
	 *             wyjatek zglaszany, gdy numer wezla potomnego jest niezgodny z
	 *             przeznaczeniem wezla (np. wywolanie tej metody na wezle-lisciu).
	 */
	public NodeInterface getChildNode(int childID) throws StructureException;

	/**
	 * Metoda ustawia wezel-potomny o wskazanym identyfikatorze childID. Metoda
	 * powinna byc wywolywana od childID=0 do childID=requiredChildNodes()-1
	 *
	 * @param childID
	 *            numer wezla potomnego
	 * @param child
	 *            ustawiany wezel potomny
	 * @throws StructureException
	 *             wyjatek zglaszany, gdy numer wezla potomnego jest niezgodny z
	 *             przeznaczeniem wezla (np. wywolanie tej metody na wezle-lisciu).
	 */
	public void setChildNode(int childID, NodeInterface child) throws StructureException;

	/**
	 * Metoda zwraca liczbe wymaganych wezlow potomnych.
	 *
	 * @return liczba potomkow wymaganych do prawidlowej pracy wezla
	 */
	public int requiredChildNodes();

	/**
	 * Metoda zwraca symbol matematyczny reprezentujacy dzialanie danego wezla
	 *
	 * @return symbol matematyczny
	 */
	public String getMathSymbol();

	/**
	 * Metoda zwraca wartosc liczbowa bedaca wynikiem wykonania wszystkich dzialan
	 * na wezlach potomnych tego wezla
	 *
	 * @return wartosc drzewa, ktorego ten wezel jest korzeniem
	 */
	public double getValue() throws MathError, StructureException;

	/**
	 * Metoda tworzaca nowy obiekt-klon.
	 * 
	 * @return obiekt-klon obiektu, na ktorym ta metoda zostanie wywolana.
	 */
	public NodeInterface deepClone();

	/**
	 * Metoda pozwala sprawdzic, czy wynik zwracany przez dany wezel zalezy od
	 * kolejnosci jego dzieci.
	 * 
	 * @return true - wynik nie ulegnie zmianie w wyniku zamiany kolejnosci wezlow
	 *         potomnych. Wartosc false moze zostac zwrocona tylko w przypadku
	 *         wezlow posiadajacych dwa wezly potomne.
	 */
	public boolean hasSymmetry();
}
