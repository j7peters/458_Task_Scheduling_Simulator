package algorithms;

import java.util.Comparator;

import dataObjects.CPUTask;

public class RMSComparatorParent implements Comparator<CPUTask>{

	@Override
	public int compare(CPUTask o1, CPUTask o2) {
		//this sorts the parent tasks
		if(o1.period < o2.period){
			return -1;
		} else if(o1.period > o2.period){
			return 1;
		}
		return 0;
	}
	
}
