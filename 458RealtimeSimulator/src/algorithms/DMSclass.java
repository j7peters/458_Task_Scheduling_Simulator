package algorithms;

import java.util.ArrayList;
import java.util.Comparator;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import dataObjects.CPUTask;
import dataObjects.TaskInstance;

public class DMSclass {

	public TaskSeriesCollection chartDataset;

	public TaskInstance[] taskInstances;

	public Task[] graphTasks;
	
	public ArrayList<Task> OrderedGraphTasks;

	public ArrayList<CPUTask> priorityTaskList;

	final TaskSeries s1 = new TaskSeries("Scheduled");

	public int lcm;

	public int numTasks=0;

	/**
	 * This will create an RMS scheduler for the given task set
	 * @param taskList - list of tasks to be performed
	 * @param chartDataset - category dataset to be given to the JFreeChart Gantt chart
	 */
	@SuppressWarnings("unchecked")
	public DMSclass(ArrayList<CPUTask> taskList, IntervalCategoryDataset chartDataset) {

		priorityTaskList = new ArrayList<CPUTask>();
		ArrayList<CPUTask> tmpTasks = new ArrayList<CPUTask>();
		OrderedGraphTasks = new ArrayList<Task>();
		
		try{
			tmpTasks = (ArrayList<CPUTask>) taskList.clone();			
		} catch (Exception e){
			System.err.println("Error cloning CPUTask list.");
		}
		
		numTasks = taskList.size();

		if(numTasks > 0){
			int[] periods = new int[numTasks];
			ArrayList<String> TaskNames = new ArrayList<String>();
			
			taskInstances = new TaskInstance[numTasks];
			graphTasks = new Task[numTasks];
			
			// store the periods to find lcm
			for(int i=0; i<numTasks; i++){
				periods[i]=tmpTasks.get(i).period;
			}
			
			//compute the least common multiple
			lcm = Util.lcm(periods);

			//setup tasks
			for(int i=0; i<numTasks; i++){
				
				OrderedGraphTasks.add(Util.createTask(tmpTasks.get(i).getName(), 1, lcm));
				
				s1.add(OrderedGraphTasks.get(i));
			}
			
			//create a list of prioritized tasks
			for(int i = 0; i<numTasks;i++){
				int min=0;
				for(int j = 0; j<tmpTasks.size();j++){
					if(tmpTasks.get(j).deadline < tmpTasks.get(min).deadline){
						min = j;
					}
				}
				priorityTaskList.add(tmpTasks.get(min));
				graphTasks[i] = OrderedGraphTasks.get(min);
				OrderedGraphTasks.remove(min);
				tmpTasks.remove(min);
				
				taskInstances[i] = new TaskInstance(1, 1, priorityTaskList.get(i), i);
			}

			
		}
	}

	/**
	 * Create the chartDataset to store the schedule
	 * @return True if success, otherwise false
	 */
	public boolean createSchedule(){
		TaskInstance curTaskInstance;
		boolean curTimeUsed = false;

		//loop through and schedule all the tasks based on priority
		for(int now = 1; now<=lcm;){
			curTimeUsed = false;

			for(int j=0; j<numTasks && curTimeUsed == false; j++){
				if(taskInstances[j].readyTime <= now){
					int rt = taskInstances[j].remainingTime;

					//actual amount of computation to be allotted
					int c = rt;

					//allow for preemmption by higher priority tasks
					for(int higherPriority = j - 1; higherPriority >=0; higherPriority--){
						if(taskInstances[higherPriority].readyTime < now + c){
							c = taskInstances[higherPriority].readyTime - now;
						}
					}

					if(taskInstances[j].useComputationTime(now, now + c)){
						final Task st32 = Util.createTask(taskInstances[j].parentTask.getName(), now, now + c);
						st32.setPercentComplete(1.0);
						graphTasks[j].addSubtask(st32);

						if(taskInstances[j].remainingTime <= 0){
							curTaskInstance = new TaskInstance(	taskInstances[j].instanceNumber + 1, 
									taskInstances[j].readyTime + taskInstances[j].parentTask.period, 
									taskInstances[j].parentTask, 
									j);
							taskInstances[j] = curTaskInstance;
						}
						System.out.println("now="+ now +", c="+c+", j="+j+", pre rt="+rt+", post rt="+taskInstances[j].remainingTime);
						now = now + c;
						curTimeUsed = true;
					} else {
						System.out.println("FAIL:" + "now="+ now +", c="+c+", j="+j+", pre rt="+rt+", post rt="+taskInstances[j].remainingTime);
					}
				} 
			}
			if(curTimeUsed == false){
				now++;
			}
		}

		chartDataset = new TaskSeriesCollection();
		chartDataset.add(s1);

		return true;
	}

	public IntervalCategoryDataset getChartDataset() {
		return chartDataset;
	}

}

class DMSComparatorCPUTask implements Comparator<CPUTask>{

	@Override
	public int compare(CPUTask o1, CPUTask o2) {
		//this sorts the parent tasks
		if(o1.deadline < o2.deadline){
			return -1;
		} else if(o1.deadline > o2.deadline){
			return 1;
		}
		return 0;
	}
	
}

class DMSComparatorTaskInstance implements Comparator<TaskInstance>{

	@Override
	public int compare(TaskInstance o1, TaskInstance o2) {
		// no dynamic sorting is done
		return 0;
	}
	
}
