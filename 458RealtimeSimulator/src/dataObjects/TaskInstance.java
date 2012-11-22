package dataObjects;

public class TaskInstance {

	public int instanceNumber;
	public int deadline;
	public int remainingTime;
	public int priority;
	public int readyTime;
	public CPUTask parentTask;
	
	public TaskInstance (int number, int readyTime, CPUTask parent, int priority){
		this.readyTime = readyTime;
		this.instanceNumber = number;
		this.deadline = readyTime + parent.getDeadline();
		this.remainingTime = parent.getComputationTime();
		this.priority = priority;
		this.parentTask = parent;
	}
	
	public boolean useComputationTime(int begin, int end){
		if(end<begin){
			//bad input
			return false;
		} else if(end > deadline || begin > deadline){
			//past deadline
			return false;
		}
		
		//proper execution
		if(this.remainingTime >= (end - begin)){
			this.remainingTime = this.remainingTime - (end - begin);
			return true;
		}
		
		return false;
		
	}
	
	
	public int calculateLaxity(int curTime){
		return deadline - curTime - remainingTime;
	}
	
}
