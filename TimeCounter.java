package skyline;
import java.util.HashMap;
import java.util.Map;

public class TimeCounter {

	protected Map<String, Long> functionStartTime;
	protected Map<String, Long> functionTimeSum;

	public TimeCounter() {
		functionStartTime = new HashMap<String, Long>();
		functionTimeSum = new HashMap<String, Long>();
	}

	protected void printResults() {
		System.out.println(functionTimeSum);
	}

	protected void start(String callerFunctionName) {
		functionStartTime.put(callerFunctionName, System.currentTimeMillis());
	}

	protected void stop(String callerFunctionName) {

		long callerFunctionStartTime = functionStartTime.get(callerFunctionName);

		long newCallerFunctionTimeSum = 0;

		if (functionTimeSum.containsKey(callerFunctionName)) {

			long callerFunctionTimeSum = functionTimeSum.get(callerFunctionName);
			newCallerFunctionTimeSum = callerFunctionTimeSum + System.currentTimeMillis() - callerFunctionStartTime;
		} else {
			newCallerFunctionTimeSum = System.currentTimeMillis() - callerFunctionStartTime;
		}
		functionTimeSum.put(callerFunctionName, newCallerFunctionTimeSum);
	}
}
