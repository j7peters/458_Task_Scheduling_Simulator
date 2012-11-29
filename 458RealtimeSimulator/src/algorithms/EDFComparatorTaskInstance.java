package algorithms;

import java.util.Comparator;

import dataObjects.TaskInstance;

public class EDFComparatorTaskInstance implements Comparator<TaskInstance>{

	@Override
	public int compare(TaskInstance o1, TaskInstance o2) {
		if(o1.deadline < o2.deadline){
			return -1;
		} else if(o1.deadline > o2.deadline){
			return 1;
		}
		return 0;
	}
	
}
