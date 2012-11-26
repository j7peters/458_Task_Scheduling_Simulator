package algorithms;

import java.util.Calendar;
import java.util.Date;

import org.jfree.data.gantt.Task;

public class Util {

	public static final int tabWindowWidth = 800;
	
	public static final int tabWindowHeight = 500;
	
	private static int lcm(int a, int b)
	{
	    return a * (b / gcd(a, b));
	}

	public static int lcm(int[] input)
	{
	    int result = input[0];
	    for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
	    return result;
	}
	
	private static int gcd(int a, int b)
	{
	    while (b > 0)
	    {
	        int temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}
	
	public static Task createTask(String name, int start, int end){
		return new Task(name, Util.dateYear(start), Util.dateYear(end));
	}
	
	public static Date dateYear(final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, 01, 01, 01, 0, 0);
        final Date result = calendar.getTime();
        return result;

    }
}
