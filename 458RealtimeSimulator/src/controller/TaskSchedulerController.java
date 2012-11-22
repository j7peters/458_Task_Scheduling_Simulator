package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import view.MainView;
import algorithms.RMSclass;
import dataObjects.CPUTask;
import dataObjects.TaskInstance;

public class TaskSchedulerController {
	public ArrayList<CPUTask> taskList = new ArrayList<CPUTask>();
	
	public MainView view;
	
	
	
	public TaskSchedulerController(MainView myMainView){
		view = myMainView;
	}
	
	/**
	 * This will take the task data 
	 */
	public void addTaskData(){
		try{
			String name = (String) this.view.ftfTaskName.getText();
			int compTime =  Integer.parseInt(this.view.ftfComputationTime.getText());
			int period = Integer.parseInt(this.view.ftfPeriod.getText());
			int deadline = Integer.parseInt(this.view.ftfDeadline.getText());
			
			CPUTask newTask = new CPUTask(name, compTime, period, deadline);
			taskList.add(newTask);
			
			this.view.ftfTaskName.setText("");
			this.view.ftfComputationTime.setText("");
			this.view.ftfPeriod.setText("");
			this.view.ftfDeadline.setText("");
		} catch (Exception e){
			JOptionPane.showMessageDialog(this.view.getContentPane(),
			    "Please fill out all the fields before adding a task",
			    "Input Error",
			    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void overwriteSelectedTask(){
		int selected = this.view.myTaskList.getSelectedIndex();
		if(selected >= 0){
			try{
				String name = (String) this.view.ftfTaskName.getText();
				int compTime =  Integer.parseInt(this.view.ftfComputationTime.getText());
				int period = Integer.parseInt(this.view.ftfPeriod.getText());
				int deadline = Integer.parseInt(this.view.ftfDeadline.getText());
				
				CPUTask newTask = new CPUTask(name, compTime, period, deadline);
				taskList.remove(selected);
				taskList.add(selected, newTask);
				
				this.view.ftfTaskName.setText("");
				this.view.ftfComputationTime.setText("");
				this.view.ftfPeriod.setText("");
				this.view.ftfDeadline.setText("");
			} catch (Exception e){
				JOptionPane.showMessageDialog(this.view.getContentPane(),
				    "Please fill out all the fields before overwriting a task",
				    "Input Error",
				    JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this.view.getContentPane(),
			    "No task selected, please select one from the list on the right.",
			    "Selection Error",
			    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void deleteSelectedTask(){
		int selected = this.view.myTaskList.getSelectedIndex();
		if(selected >= 0){
			taskList.remove(selected);
		} else {
			JOptionPane.showMessageDialog(this.view.getContentPane(),
				    "No task selected, please select one from the list on the right.",
				    "Selection Error",
				    JOptionPane.ERROR_MESSAGE);
			}
	}
	
	public void editTaskSelected(){
		int selected = this.view.myTaskList.getSelectedIndex();
		if(selected >= 0){
			CPUTask curTask = this.taskList.get(selected);
			this.view.ftfTaskName.setText(curTask.getName());
			this.view.ftfComputationTime.setText(String.valueOf(curTask.getComputationTime()));
			this.view.ftfPeriod.setText(String.valueOf(curTask.getPeriod()));
			this.view.ftfDeadline.setText(String.valueOf(curTask.getDeadline()));
		} else {
			JOptionPane.showMessageDialog(this.view.getContentPane(),
			    "No task selected, please select one from the list on the right.",
			    "Selection Error",
			    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void refreshTaskList(){
		this.view.myTaskListModel.removeAllElements();
		    for(CPUTask p : taskList){
		        this.view.myTaskListModel.addElement(p.toString());
		    }
		    this.view.myTaskList.setSelectedIndex(0);
	}
	
	public void scheduleTasks(){
		//TODO if RMS
		RMSclass scheduler = new RMSclass(this.taskList, this.view.chartDataset);
		
		if(scheduler.createSchedule()){
			this.view.chartDataset = scheduler.getChartDataset();
			this.view.refreshChartPanel();
		} else {
			JOptionPane.showMessageDialog(this.view.getContentPane(),
				    "Failed to make schedule.",
				    "Scheduling` Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		
		// setup the tasks
		for(int i = 0; i<taskList.size(); i++){
			
		}
		
	}
	
	
}
