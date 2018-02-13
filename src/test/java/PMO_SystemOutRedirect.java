import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class PMO_SystemOutRedirect {
	private static PrintStream ps;
	private static PrintStream orig = System.out;
	private static boolean redirectionON = false;

	static {
		ps = new PrintStream(new OutputStream() {
			public void write(int b) {
				//DO NOTHING
			}
		});
	}

	public static void startRedirectionToNull() {
		if (ps != null)
			System.setOut(ps);
		redirectionON = true;
	}

	public static void returnToStandardStream() {
		System.setOut(orig);
		redirectionON = false;
	}

	public static void closeNullStream() {
		ps.close();
	}

	public static void print(String s) {
		if (redirectionON) {
			returnToStandardStream();
			System.out.print(s);
			startRedirectionToNull();
		} else {
			System.out.print(s);
		}
	}

	public static void println(String s) {
		if (redirectionON) {
			returnToStandardStream();
			System.out.println(s);
			startRedirectionToNull();
		} else {
			System.out.println(s);
		}
	}

	public static void showMethodName() {
		PMO_SystemOutRedirect.println("_____" + Thread.currentThread().getStackTrace()[2].getMethodName() + "_____");
	}

}
