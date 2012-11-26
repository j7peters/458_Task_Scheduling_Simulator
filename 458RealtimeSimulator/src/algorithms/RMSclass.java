package algorithms;

import java.util.ArrayList;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import dataObjects.CPUTask;
import dataObjects.TaskInstance;

public class RMSclass {

	public TaskSeriesCollection chartDataset;

	public TaskInstance[] taskInstances;

	public Task[] graphTasks;

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
	public RMSclass(ArrayList<CPUTask> taskList, IntervalCategoryDataset chartDataset) {

		priorityTaskList = new ArrayList<CPUTask>();
		ArrayList<CPUTask> tmpTasks = new ArrayList<CPUTask>();
		
		try{
			tmpTasks = (ArrayList<CPUTask>) taskList.clone();			
		} catch (Exception e){
			System.err.println("Error cloning CPUTask list.");
		}
		
		numTasks = taskList.size();

		if(numTasks > 0){
			int[] periods = new int[numTasks];
			taskInstances = new TaskInstance[numTasks];
			graphTasks = new Task[numTasks];

			//create a list of prioritized tasks
			for(int i = 0; i<numTasks;i++){
				int min=0;
				for(int j = 0; j<tmpTasks.size();j++){
					if(tmpTasks.get(j).period < tmpTasks.get(min).period){
						min = j;
					}
				}
				priorityTaskList.add(tmpTasks.get(min));
				periods[i]=tmpTasks.get(min).period;
				tmpTasks.remove(min);
			}

			//compute the least common multiple
			lcm = Util.lcm(periods);
		}
	}

	/**
	 * Create the chartDataset to store the schedule
	 * @return True if success, otherwise false
	 */
	public boolean createSchedule(){
		//setup tasks
		for(int i=0; i<numTasks; i++){
			
			graphTasks[i] = Util.createTask(priorityTaskList.get(i).getName(), 1, lcm);
			
			s1.add(graphTasks[i]);
			taskInstances[i] = new TaskInstance(1, 1, priorityTaskList.get(i), i);
		}
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
