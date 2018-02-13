import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Start {

	private static String code;
	private static String term;
	private static double maxPoints;
	private static DecimalFormat myFormatter = new DecimalFormat("#.#");

	private static void showVerdict(double points, String note) {
		System.out.println(code + ";" + myFormatter.format(points) + ";Termin" + term + ">" + note);
	}

	private static String listFailedTests(List<String> failedTests) {

		if (failedTests.size() == 0)
			return "";

		String penaltyStr = failedTests.get(0);

		for (int i = 1; i < failedTests.size(); i++) {
			penaltyStr += ", " + failedTests.get(i);
		}

		return penaltyStr;
	}

	private static void evaluation( int tests, List<String> failedTests, String testClassName, double maxPoints)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method tariff = Class.forName(testClassName).getMethod("getTariff", String.class);

		double penalty = 0;
		for (String failedTest : failedTests) {
			penalty += ((Double) tariff.invoke(null, failedTest)).doubleValue();
		}

		if ( failedTests.size() == tests ) {
			showVerdict(0, "Program nie zaliczył żadnego testu");
		} else if ( failedTests.size() * 2 > tests ) {
			showVerdict(0, "Większość testów nie została zaliczona");			
		} else if (failedTests.size() == 0) {
			showVerdict(maxPoints, "OK");
		} else if (penalty <= maxPoints) {
			showVerdict(maxPoints - penalty, "Odliczono punkty za błędy w: " + listFailedTests(failedTests));
		} else {
			showVerdict(0, "Suma kar za błędy w: " + listFailedTests(failedTests) + " przewyższa maksymalną punktację");
		}
	}

	private static void testCode( String[] args )  {
		Result result = null;
		try {
			result = JUnitCore.runClasses(Class.forName(args[0]));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		PMO_SystemOutRedirect.returnToStandardStream();
		System.out.println("-------------------------------------------");
		List<String> failures = new ArrayList<>();
		for (Failure failure : result.getFailures()) {
			Description dsc = failure.getDescription();
			System.out.println("BLAD: " + failure.getTestHeader() + "> " + failure.getMessage() );
			
			if ( failure.getMessage() == null ) {
				System.out.println( " NULL jako getMessage ");
			}
			
			failures.add(dsc.getMethodName());
		}

		System.out.println("-------------------------------------------");
		System.out.println("Wykonano      : " + result.getRunCount() + " testow");
		System.out.println("Nie zaliczono : " + result.getFailureCount() + " testow");

		System.out.println("-------------------------------------------");
		if (result.wasSuccessful()) {
			System.out.println("Testy zakonczone calkowitym sukcesem");
		} else {
			System.out.println("Nie wszystkie testy zostaly zaliczone");
		}

		if (args.length == 4) {
			code = args[1];
			term = args[2];
			maxPoints = (new Double(args[3])).doubleValue();
			try {
				evaluation( result.getRunCount(), failures, args[0], maxPoints);
			} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}		
	}
	
	private static void runTestAsThread( String[] args, long sleepTime ) {
		Thread th = new Thread( () -> Start.testCode(args));
		th.setDaemon( true );
		th.start();
		try {
			th.join( sleepTime );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		runTestAsThread(args, 150000 );
		
		System.exit( 0 );
	}
}
