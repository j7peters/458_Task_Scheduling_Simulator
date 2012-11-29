package algorithms;

import java.util.Comparator;

import dataObjects.TaskInstance;

public class RMSComparatorTaskInstance implements Comparator<TaskInstance>{

	@Override
	public int compare(TaskInstance o1, TaskInstance o2) {
		// no dynamic sorting is done
		return 0;
	}
	
}

