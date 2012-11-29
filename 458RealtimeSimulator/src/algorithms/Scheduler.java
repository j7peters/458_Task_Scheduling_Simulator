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

public class Scheduler {

	/**
	 * Create the chartDataset to store the schedule
	 * @return True if success, otherwise false
	 */
	public static TaskSeriesCollection createSchedule(ArrayList<CPUTask> taskList,
			Comparator<CPUTask> parentComparator, Comparator<TaskInstance> instanceComparator){

		//create all variables needed
		TaskSeriesCollection toReturnTaskSeries = new TaskSeriesCollection();

		ArrayList<TaskInstance> taskInstances = new ArrayList<TaskInstance>();

		Map<String, Task> graphTasks = new HashMap<String, Task>();

		ArrayList<CPUTask> priorityTaskList = new ArrayList<CPUTask>();

		final TaskSeries s1 = new TaskSeries("Scheduled");

		int lcm = 0;

		int numTasks = 0;
		
		TaskInstance curTaskInstance;
		
		boolean curTimeUsed = false;

	/* Set up tasks and background information */
		try{
			priorityTaskList = (ArrayList<CPUTask>) taskList.clone();			
		} catch (Exception e){
			System.err.println("Error cloning CPUTask list.");
		}

		numTasks = taskList.size();

		//no tasks in list nothing to schedule
		if(numTasks <= 0){
			return toReturnTaskSeries;
		}
		
		int[] periods = new int[numTasks];
		ArrayList<String> TaskNames = new ArrayList<String>();

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

		//Sort the parent tasks based on comparator
		Collections.sort(priorityTaskList, parentComparator);

		//create a list of prioritized tasks
		for(int i = 0; i<numTasks;i++){				
			taskInstances.add(new TaskInstance(1, 1, priorityTaskList.get(i), i));
		}


	/* Scheduling by looping through tasks */
		//loop through and schedule all the tasks based on priority
		for(int now = 1; now<=lcm;){
			curTimeUsed = false;
			
			//recalculate laxity for each task incase this is running LLF
			computeTaskLaxities(taskInstances, now);
			
			//resort the task instances for dynamic priority algorithms
			Collections.sort(taskInstances, instanceComparator);

			for(int j=0; j<numTasks && curTimeUsed == false; j++){

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


		//return the taskseries with the graphed tasks added to it.
		toReturnTaskSeries.add(s1);

		return toReturnTaskSeries;
	}
	
	public static void computeTaskLaxities(ArrayList<TaskInstance> taskInstances, int curTime){
		for( TaskInstance ti : taskInstances){
			ti.computeLaxity(curTime);
		}
	}

}

