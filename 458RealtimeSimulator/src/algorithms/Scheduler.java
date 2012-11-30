package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
	@SuppressWarnings("unchecked")
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

		TaskInstance curTaskInstance = null;

		int timeCurTaskStarted=0;

		boolean curTimeUsed = false;

		TaskInstance newTaskInstance;

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

					if(taskInstances.get(j).remainingTime > 0 && taskInstances.get(j).isPastDeadline(now) ){
						//This is past the deadline
						System.err.println("Fail: now="+ (now) +", name = " + taskInstances.get(j).parentTask.name);
						
						newTaskInstance = new TaskInstance(	taskInstances.get(j).instanceNumber + 1, 
								taskInstances.get(j).readyTime + taskInstances.get(j).parentTask.period, 
								taskInstances.get(j).parentTask, 
								j);
						taskInstances.remove(j);
						taskInstances.add(j, newTaskInstance);
						
						//redo this round of the loop
						j--;
						continue;
					}
					
					if(taskInstances.get(j).equals(curTaskInstance)){
						//TODO nothing??
					} else {
						if(curTaskInstance != null){
							//finish up previous task
							final Task st32 = Util.createTask(curTaskInstance.parentTask.getName(), timeCurTaskStarted, now);
							st32.setPercentComplete(1.0);
							graphTasks.get(curTaskInstance.parentTask.name).addSubtask(st32);
						}

						//save cur task
						timeCurTaskStarted = now;
						curTaskInstance = taskInstances.get(j);
					}

					if(taskInstances.get(j).useComputationTime(now, now + 1)){
						curTimeUsed = true;
						now = now + 1;
					} 

					//finish up previous task if it is finished
					if(taskInstances.get(j).remainingTime < 1){
						final Task st32 = Util.createTask(curTaskInstance.parentTask.getName(), timeCurTaskStarted, now);
						st32.setPercentComplete(1.0);
						graphTasks.get(curTaskInstance.parentTask.name).addSubtask(st32);

						newTaskInstance = new TaskInstance(	taskInstances.get(j).instanceNumber + 1, 
								taskInstances.get(j).readyTime + taskInstances.get(j).parentTask.period, 
								taskInstances.get(j).parentTask, 
								j);
						taskInstances.remove(j);
						taskInstances.add(j, newTaskInstance);

						curTaskInstance = null;
					}
					
					

					System.out.println("now="+ (now-1) +", name = " + taskInstances.get(j).parentTask.name);

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

