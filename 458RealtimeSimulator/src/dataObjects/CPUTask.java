package dataObjects;

public class CPUTask {
	
	public String name;
	public int computationTime;
	public int period;
	public int deadline;
	
	public CPUTask(String name, int c, int p, int d){
		this.name = name;
		this.computationTime = c;
		this.period = p;
		this.deadline = d;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getComputationTime() {
		return computationTime;
	}
	public int getPeriod() {
		return period;
	}
	public int getDeadline() {
		return deadline;
	}
	
	public String toString(){
		return name + "={ "+ computationTime + ", " + period + ", " + deadline +" }";
	}
}
