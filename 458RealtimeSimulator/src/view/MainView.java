package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;

public class MainView extends JFrame implements ActionListener{
	
	public IntervalCategoryDataset chartDataset;
	
	public JTabbedPane myTabbedPane = new JTabbedPane();
	
	public ChartPanel myChartPanel;
	
	public JPanel myJPanel;
	
	private final int majorTicks = 10;
	
	private final int minorTicks = 10;
	
	/**
	 * Formatted Text Fields
	 */
	JFormattedTextField ftfTaskName;
	
	JFormattedTextField ftfTaskName;

	
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
		setSize(800,500);
		
		this.myJPanel = getTaskEditorPanel();
		this.myTabbedPane.add("Task Editor", this.myJPanel);
		
		this.myChartPanel = getChartPanel();
		this.myTabbedPane.add("Task Schedule", this.myChartPanel);
		
		setContentPane(this.myTabbedPane);
		//TODO add views here
	}
	
	public JPanel getTaskEditorPanel(){
		JPanel tmpJPanel = new JPanel(new java.awt.BorderLayout());
		
		des[4] = "currency";
	    JFormattedTextField taskName = new JFormattedTextField(java.text.NumberFormat
	        .getCurrencyInstance());
		
		tmpJPanel.setBorder(new javax.swing.border.TitledBorder("Description"));
		tmpJPanel.add(/*Component*/, java.awt.BorderLayout.CENTER);
		
		return tmpJPanel;
	}
	
	
	public ChartPanel getChartPanel(){

        final IntervalCategoryDataset dataset = chartDataset;

        
        // create the chart...
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Task Schedule",  // chart title
            "Task",              // domain axis label
            "Time",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );
        // set format so displays years, looks like integers allows us large range
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setDateFormatOverride(new SimpleDateFormat("YYYY"));
        
        // set major ticks to be every 10 years which is every 10 spaces
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setTickUnit(new DateTickUnit(DateTickUnitType.YEAR, majorTicks));
        
        // set minor ticks to be every 10 so there is a tick for every year, set minor ticks visible
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinorTickCount(minorTicks);
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinorTickMarksVisible(true);
        
        
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        final CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        return chartPanel;
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
