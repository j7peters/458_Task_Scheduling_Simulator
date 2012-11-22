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
	public RMSclass(ArrayList<CPUTask> taskList, IntervalCategoryDataset chartDataset) {

		priorityTaskList = new ArrayList<CPUTask>();

		ArrayList<CPUTask> tmpTasks = (ArrayList<CPUTask>) taskList.clone();
		numTasks = taskList.size();

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

	/**
	 * Create the chartDataset to store the schedule
	 * @return True if success, otherwise false
	 */
	public boolean createSchedule(){
		//setup tasks
		for(int i=0; i<numTasks; i++){
			graphTasks[i] = new Task(
					priorityTaskList.get(i).getName(), Util.dateYear(1), Util.dateYear(lcm)
					);
			s1.add(graphTasks[i]);
			taskInstances[i] = new TaskInstance(i, 1, priorityTaskList.get(i), i);
		}
		TaskInstance curTaskInstance;

		//loop through and schedule all the tasks based on priority
		for(int now = 1; now<lcm; now++){

			for(int j=0; j<numTasks; j++){
				if(taskInstances[j].readyTime <= now){
					int c = taskInstances[j].remainingTime;
					if(taskInstances[j].useComputationTime(now, now + c)){
						final Task st32 = new Task(
								taskInstances[j].parentTask.getName(), 
								Util.dateYear(now), Util.dateYear(now + c)
								);
						st32.setPercentComplete(1.0);
						graphTasks[j].addSubtask(st32);

						curTaskInstance = new TaskInstance(	j, 
								taskInstances[j].readyTime + taskInstances[j].parentTask.period, 
								taskInstances[j].parentTask, 
								j);
						taskInstances[j] = curTaskInstance;
						now = now + c;
					}
				} else {
					//TODO flag as not valid
				}
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
