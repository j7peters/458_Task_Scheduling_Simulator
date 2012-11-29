package algorithms;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import dataObjects.CPUTask;
import dataObjects.TaskInstance;

public class DMSrefactor {

	public TaskSeriesCollection chartDataset;

	public ArrayList<TaskInstance> taskInstances;

	Map<String, Task> graphTasks;

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
	public DMSrefactor(ArrayList<CPUTask> taskList, IntervalCategoryDataset chartDataset) {

		priorityTaskList = new ArrayList<CPUTask>();
		
		try{
			priorityTaskList = (ArrayList<CPUTask>) taskList.clone();			
		} catch (Exception e){
			System.err.println("Error cloning CPUTask list.");
		}
		
		numTasks = taskList.size();

		if(numTasks > 0){
			int[] periods = new int[numTasks];
			ArrayList<String> TaskNames = new ArrayList<String>();
			
			taskInstances = new ArrayList<TaskInstance>();
			graphTasks = new HashMap<String, Task>();
			
			// store the periods to find lcm, store names to get the order right later
			for(int i=0; i<numTasks; i++){
				periods[i]=priorityTaskList.get(i).period;
				TaskNames.add(priorityTaskList.get(i).getName());
			}
			
			//compute the least common multiple
			lcm = Util.lcm(periods);

			//setup tasks
			for(int i=0; i<numTasks; i++){
				graphTasks.put(TaskNames.get(i), Util.createTask(TaskNames.get(i), 1, lcm));
				s1.add(graphTasks.get(TaskNames.get(i)));
			}
			
			Collections.sort(priorityTaskList, new DMSComparatorCPUTask());
			
			//create a list of prioritized tasks
			for(int i = 0; i<numTasks;i++){				
				taskInstances.add(new TaskInstance(1, 1, priorityTaskList.get(i), i));
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
				//resort the task instances for dynamic priority algorithms
				Collections.sort(taskInstances, new DMSComparatorTaskInstance());
				
				if(taskInstances.get(j).readyTime <= now){
					int rt = taskInstances.get(j).remainingTime;

					//actual amount of computation to be allotted
					int c = rt;

					//allow for preemmption by higher priority tasks
					for(int higherPriority = j - 1; higherPriority >=0; higherPriority--){
						if(taskInstances.get(higherPriority).readyTime < now + c){
							c = taskInstances.get(higherPriority).readyTime - now;
						}
					}

					if(taskInstances.get(j).useComputationTime(now, now + c)){
						final Task st32 = Util.createTask(taskInstances.get(j).parentTask.getName(), now, now + c);
						st32.setPercentComplete(1.0);
						graphTasks.get(taskInstances.get(j).parentTask.name).addSubtask(st32);

						if(taskInstances.get(j).remainingTime <= 0){
							curTaskInstance = new TaskInstance(	taskInstances.get(j).instanceNumber + 1, 
									taskInstances.get(j).readyTime + taskInstances.get(j).parentTask.period, 
									taskInstances.get(j).parentTask, 
									j);
							taskInstances.remove(j);
							taskInstances.add(j, curTaskInstance);
						}
						System.out.println("now="+ now +", c="+c+", j="+j+", pre rt="+rt+", post rt="+taskInstances.get(j).remainingTime);
						now = now + c;
						curTimeUsed = true;
					} else {
						System.out.println("FAIL:" + "now="+ now +", c="+c+", j="+j+", pre rt="+rt+", post rt="+taskInstances.get(j).remainingTime);
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

