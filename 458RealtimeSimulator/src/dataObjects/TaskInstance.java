package dataObjects;

public class TaskInstance {

	public int instanceNumber;
	public int deadline;
	public int remainingTime;
	public int priority;
	public int readyTime;
	public int laxity;
	public CPUTask parentTask;
	
	public TaskInstance (int number, int readyTime, CPUTask parent, int priority){
		this.readyTime = readyTime;
		this.instanceNumber = number;
		this.deadline = readyTime + parent.getDeadline();
		this.remainingTime = parent.getComputationTime();
		this.priority = priority;
		this.parentTask = parent;
		
		//initialize to large number
		this.laxity = 10000;
	}
	
	public boolean useComputationTime(int begin, int end){
		if(end<begin){
			//bad input
			return false;
		} else if(end > deadline+1 || begin > deadline){
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
	
	
	public int computeLaxity(int curTime){
		this.laxity = deadline - curTime - remainingTime;
		return this.laxity;
	}
	
	public boolean isPastDeadline(int now){
		if(now >= this.deadline){
			return true;
		}
		
		return false;
	}
	
}
