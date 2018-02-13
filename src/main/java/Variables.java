import java.util.Map;
import java.util.TreeMap;

/**
 * Klasa - repozytorium zmiennych. Każda zmienna jest typu double i jest
 * identyfikowana za pomoca nazwy (ciagu znakow). Klasa posiada prywatny
 * konstruktor i w programie moze istniec tylko jeden obiekt repozytorium
 * (singleton).
 */
public class Variables {
	private final Map<String, Double> map = new TreeMap<String, Double>();
	private static Variables ref;

	/**
	 * Blokada mozliwosci niekontrolowanego powstania obiektow klasy Variables
	 */
	private Variables() {
	}

	/**
	 * Metoda zwracajaca referecje do jednego obiektu tej klasy.
	 * 
	 * @return obiekt klasy Variables - wspolny dla wszystkich.
	 */
	public static Variables getVariables() {
		if (ref == null) {
			ref = new Variables();
		}
		return ref;
	}

	public void createVariable(String name, double initialValue) {

		if (!map.containsKey(name)) {
			map.put(name, initialValue);
		}

	}

	public double getValue(String name) {
		if (!map.containsKey(name))
			throw new RuntimeException();

		return map.get(name);
	}

	public void setValue(String name, double value) {
		if (map.containsKey(name)) {
			map.replace(name, value);
		} else {
			createVariable(name, value);
		}
	}

}
