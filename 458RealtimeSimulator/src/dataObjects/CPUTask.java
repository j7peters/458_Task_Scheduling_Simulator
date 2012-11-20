package dataObjects;

public class CPUTask {
	
	public String name;
	public int worstCaseComputationTime;
	public int computationTime;
	public int period;
	public int deadline;
	
	public CPUTask(String name, int wc, int c, int p, int d){
		this.name = name;
		this.worstCaseComputationTime = wc;
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
}
