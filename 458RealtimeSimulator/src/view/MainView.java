package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;

import algorithms.Util;
import controller.TaskSchedulerController;

public class MainView extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public IntervalCategoryDataset RMSchartDataset;
	public IntervalCategoryDataset EDFchartDataset;
	public IntervalCategoryDataset DMSchartDataset;
	public IntervalCategoryDataset LLFchartDataset;

	public JTabbedPane myTabbedPane = new JTabbedPane();

	public ChartPanel RMSChartPanel;
	public ChartPanel EDFChartPanel;
	public ChartPanel DMSChartPanel;
	public ChartPanel LLFChartPanel;

	public JSplitPane RMSGanttPanel;
	public JSplitPane EDFGanttPanel;
	public JSplitPane DMSGanttPanel;
	public JSplitPane LLFGanttPanel;

	public JTextArea RMSTextArea = new JTextArea();
	public JTextArea EDFTextArea = new JTextArea();
	public JTextArea DMSTextArea = new JTextArea();
	public JTextArea LLFTextArea = new JTextArea();

	public JScrollPane RMSScrollPane = new JScrollPane(this.RMSTextArea);
	public JScrollPane EDFScrollPane = new JScrollPane(this.EDFTextArea);
	public JScrollPane DMSScrollPane = new JScrollPane(this.DMSTextArea);
	public JScrollPane LLFScrollPane = new JScrollPane(this.LLFTextArea);

	public JPanel myTaskEditorPanel;

	public JPanel myTaskListPane;

	public JList<String> myTaskList;

	public DefaultListModel<String> myTaskListModel;

	public TaskSchedulerController controller;

	// split pane
	public JSplitPane myTaskSplitPane;



	private final int majorTicks = 5;

	private final int minorTicks = 5;

	/**
	 * Formatted Text Fields
	 */
	public JFormattedTextField ftfTaskName;

	public JFormattedTextField ftfComputationTime;

	public JFormattedTextField ftfPeriod;

	public JFormattedTextField ftfDeadline;


	public static void main(String[] args) 
	{
		final MainView frame = new MainView();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		frame.setVisible(true);	
	}

	MainView()
	{	
		super("Real Time Systems Simulator: CprE 458");
		setSize(Util.tabWindowWidth, Util.tabWindowHeight);

		this.myTaskEditorPanel = getTaskEditorPanel();
		this.myTaskListPane = getTaskListPane();

		this.myTaskSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myTaskEditorPanel, myTaskListPane);

		this.myTabbedPane.add("Task Editor", this.myTaskSplitPane);

		// Initialize Chart Panels
		this.RMSChartPanel = getChartPanel(this.RMSchartDataset, "RMS Schedule");
		this.EDFChartPanel = getChartPanel(this.EDFchartDataset, "EDF Schedule");
		this.DMSChartPanel = getChartPanel(this.DMSchartDataset, "DMS Schedule");
		this.LLFChartPanel = getChartPanel(this.LLFchartDataset, "LLF Schedule");

		// RMS
		this.RMSGanttPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.RMSChartPanel, RMSScrollPane);

		// EDF
		this.EDFGanttPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.EDFChartPanel, EDFScrollPane);

		// DMS
		this.DMSGanttPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.DMSChartPanel, DMSScrollPane);

		// LLF
		this.LLFGanttPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.LLFChartPanel, LLFScrollPane);

		this.myTabbedPane.add("RMS Schedule", this.RMSGanttPanel);
		this.myTabbedPane.add("EDF Schedule", this.EDFGanttPanel);
		this.myTabbedPane.add("DMS Schedule", this.DMSGanttPanel);
		this.myTabbedPane.add("LLF Schedule", this.LLFGanttPanel);

		refreshChartPanel();

		this.controller = new TaskSchedulerController(this);

		setContentPane(this.myTabbedPane);

		this.myTabbedPane.setSelectedIndex(0);
	}

	public void refreshChartPanel(){
		// RMS
		this.RMSGanttPanel.remove(this.RMSChartPanel);
		this.RMSChartPanel = getChartPanel(this.RMSchartDataset, "RMS Schedule");
		this.RMSGanttPanel.add(this.RMSChartPanel);

		// EDF
		this.EDFGanttPanel.remove(this.EDFChartPanel);
		this.EDFChartPanel = getChartPanel(this.EDFchartDataset, "EDF Schedule");
		this.EDFGanttPanel.add(this.EDFChartPanel);

		// DMS
		this.DMSGanttPanel.remove(this.DMSChartPanel);
		this.DMSChartPanel = getChartPanel(this.DMSchartDataset, "DMS Schedule");
		this.DMSGanttPanel.add(this.DMSChartPanel);

		// LLF
		this.LLFGanttPanel.remove(this.LLFChartPanel);
		this.LLFChartPanel = getChartPanel(this.LLFchartDataset, "LLF Schedule");
		this.LLFGanttPanel.add(this.LLFChartPanel);

		this.myTabbedPane.repaint();
		
		// resize scroll panes for text areas
		this.RMSScrollPane.setPreferredSize(new Dimension(Util.tabWindowWidth, Util.textAreaHeight));
		this.EDFScrollPane.setPreferredSize(new Dimension(Util.tabWindowWidth, Util.textAreaHeight));
		this.DMSScrollPane.setPreferredSize(new Dimension(Util.tabWindowWidth, Util.textAreaHeight));
		this.LLFScrollPane.setPreferredSize(new Dimension(Util.tabWindowWidth, Util.textAreaHeight));

		
		this.myTabbedPane.setSelectedIndex(1);
	}

	public JPanel getTaskListPane(){
		this.myTaskListModel = new DefaultListModel<String>();
		this.myTaskList = new JList<String>(this.myTaskListModel); //data has type Object[]
		this.myTaskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.myTaskList.setLayoutOrientation(JList.VERTICAL);
		this.myTaskList.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(this.myTaskList);
		listScroller.setPreferredSize(new Dimension(250, 80));

		JPanel ListViewPanel = new JPanel(new java.awt.BorderLayout());
		ListViewPanel.setBorder(new javax.swing.border.TitledBorder("TaskName = { c, p, d }"));
		ListViewPanel.add(listScroller);

		return ListViewPanel;
	}

	public JPanel getTaskEditorPanel(){
		JPanel tmpJPanel = new JPanel();

		// Task Name
		ftfTaskName = new JFormattedTextField();
		ftfTaskName.setPreferredSize(new Dimension(200, 20));
		JPanel borderPanel1 = new JPanel(new java.awt.BorderLayout());
		borderPanel1.setBorder(new javax.swing.border.TitledBorder("Task Name"));
		borderPanel1.add(ftfTaskName, java.awt.BorderLayout.WEST);
		tmpJPanel.add(borderPanel1);

		// Computation Time
		ftfComputationTime = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
		ftfComputationTime.setPreferredSize(new Dimension(200, 20));
		JPanel borderPanel2 = new JPanel(new java.awt.BorderLayout());
		borderPanel2.setBorder(new javax.swing.border.TitledBorder("Computation Time"));
		borderPanel2.add(ftfComputationTime, java.awt.BorderLayout.WEST);
		tmpJPanel.add(borderPanel2);

		// Period
		ftfPeriod = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
		ftfPeriod.setPreferredSize(new Dimension(200, 20));
		JPanel borderPanel3 = new JPanel(new java.awt.BorderLayout());
		borderPanel3.setBorder(new javax.swing.border.TitledBorder("Period Time"));
		borderPanel3.add(ftfPeriod, java.awt.BorderLayout.WEST);
		tmpJPanel.add(borderPanel3);

		// Deadline
		ftfDeadline = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
		ftfDeadline.setPreferredSize(new Dimension(200, 20));
		JPanel borderPanel4 = new JPanel(new java.awt.BorderLayout());
		borderPanel4.setBorder(new javax.swing.border.TitledBorder("Deadline (same as period usually)"));
		borderPanel4.add(ftfDeadline, java.awt.BorderLayout.WEST);
		tmpJPanel.add(borderPanel4);

		/**
		 * Buttons for editing
		 */
		JPanel buttonPanel= new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


		//add
		JButton btnAdd = new JButton("Add Task");
		btnAdd.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnAdd.setHorizontalTextPosition(AbstractButton.CENTER);
		btnAdd.setMnemonic(KeyEvent.VK_A);
		btnAdd.setActionCommand("AddTask");
		btnAdd.addActionListener(this);
		btnAdd.setSize(new Dimension(200, 30));
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnAdd);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

		//edit
		JButton btnEdit = new JButton("Edit Selected Task");
		btnEdit.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnEdit.setHorizontalTextPosition(AbstractButton.CENTER);
		btnEdit.setMnemonic(KeyEvent.VK_E);
		btnEdit.setActionCommand("EditTask");
		btnEdit.addActionListener(this);
		btnEdit.setSize(new Dimension(200, 30));
		btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnEdit);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

		//overwrite
		JButton btnOverwrite = new JButton("Overwrite Selected Task");
		btnOverwrite.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnOverwrite.setHorizontalTextPosition(AbstractButton.CENTER);
		btnOverwrite.setMnemonic(KeyEvent.VK_O);
		btnOverwrite.setActionCommand("OverwriteTask");
		btnOverwrite.addActionListener(this);
		btnOverwrite.setSize(new Dimension(200, 30));
		btnOverwrite.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnOverwrite);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

		//delete
		JButton btnDelete = new JButton("Delete Selected Task");
		btnDelete.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnDelete.setHorizontalTextPosition(AbstractButton.CENTER);
		btnDelete.setMnemonic(KeyEvent.VK_D);
		btnDelete.setActionCommand("DeleteTask");
		btnDelete.addActionListener(this);
		btnDelete.setSize(new Dimension(200, 30));
		btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnDelete);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

		//schedule
		JButton btnSchedule = new JButton("SCHEDULE TASKS!!");
		btnSchedule.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnSchedule.setHorizontalTextPosition(AbstractButton.CENTER);
		btnSchedule.setMnemonic(KeyEvent.VK_S);
		btnSchedule.setActionCommand("ScheduleTasks");
		btnSchedule.addActionListener(this);
		btnSchedule.setSize(new Dimension(200, 50));
		btnSchedule.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnSchedule);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));


		tmpJPanel.add(buttonPanel);

		tmpJPanel.setPreferredSize(new Dimension(250, 400));
		return tmpJPanel;
	}


	public ChartPanel getChartPanel(IntervalCategoryDataset chartInput, String title){

		final IntervalCategoryDataset dataset = chartInput;


		// create the chart...
		final JFreeChart chart = ChartFactory.createGanttChart(
				title,  // chart title
				"Task",              // domain axis label
				"Time",              // range axis label
				dataset,             // data
				true,                // include legend
				false,               // tooltips
				false                // urls
				);
		// set format so displays years, looks like integers allows us large range
		((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setDateFormatOverride(new SimpleDateFormat("YYYY"));

		// set major ticks to be every 10 years which is every 10 spaces
		((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setTickUnit(new DateTickUnit(DateTickUnitType.YEAR, majorTicks));

		// set minor ticks to be every 10 so there is a tick for every year, set minor ticks visible
		((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinorTickCount(minorTicks);
		((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinorTickMarksVisible(true);

		// set minimum date
		((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinimumDate(Util.dateYear(1));

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		final CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);

		// add the chart to a panel...
		final ChartPanel chartPanel = new ChartPanel(chart);
		
		chartPanel.setPreferredSize(new java.awt.Dimension(Util.tabWindowWidth-50, Util.chartHeight));
		return chartPanel;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if ("AddTask".equals(e.getActionCommand())) {
			controller.addTaskData();
			controller.refreshTaskList();
		} else if ("EditTask".equals(e.getActionCommand())) {
			controller.editTaskSelected();
			controller.refreshTaskList();
		} else if ("OverwriteTask".equals(e.getActionCommand())) {
			controller.overwriteSelectedTask();
			controller.refreshTaskList();
		} else if ("DeleteTask".equals(e.getActionCommand())) {
			controller.deleteSelectedTask();
			controller.refreshTaskList();
		} else if ("ScheduleTasks".equals(e.getActionCommand())) {
			controller.scheduleTasks();
		} else {
			//TODO
		}

	}

}
