/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ---------------
 * GanttDemo2.java
 * ---------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: GanttDemo2.java,v 1.12 2004/04/26 19:11:54 taqua Exp $
 *
 * Changes
 * -------
 * 10-Jan-2003 : Version 1 (based on GanttDemo1) (DG);
 * 16-Sep-2003 : Transferred dataset creation from DemoDatasetFactory to this class (DG);
 *
 */

package ganttChartDemo;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a Gantt chart with multiple bars per
 * task. 
 *
 */
public class GanttDemo2 extends ApplicationFrame {
	
	private final int majorTicks = 10;
	
	private final int minorTicks = 10;

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public GanttDemo2(final String title) {

        super(title);

        final IntervalCategoryDataset dataset = createSampleDataset();

        
        // create the chart...
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Gantt Chart Demo",  // chart title
            "Task",              // domain axis label
            "Date",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );
        // set format so displays years, looks like integers allows us large range
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setDateFormatOverride(new SimpleDateFormat("YYYY"));
        
        
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setTickUnit(new DateTickUnit(DateTickUnitType.YEAR, majorTicks));
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinorTickCount(minorTicks);
        ((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setMinorTickMarksVisible(true);
        
        
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        final CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Creates a sample dataset for a Gantt chart, using sub-tasks.  In general, you won't 
     * hard-code the dataset in this way - it's done here so that the demo is self-contained.
     *
     * @return The dataset.
     */
    private IntervalCategoryDataset createSampleDataset() {

        final TaskSeries s1 = new TaskSeries("Scheduled");
        
        final Task t1 = new Task(
            "Write Proposal", dateYear(0), dateYear(3)
        );
        t1.setPercentComplete(1.00);
        s1.add(t1);
        
        final Task t2 = new Task(
            "Obtain Approval", dateYear(3), dateYear(5)
        );
        t2.setPercentComplete(1.00);
        s1.add(t2);

        // here is a task split into two subtasks...
        final Task t3 = new Task(
            "Requirements Analysis", 
            dateYear(5), dateYear(20)
        );
        final Task st31 = new Task(
            "Requirements 1", 
            dateYear(15), dateYear(20)
        );
        st31.setPercentComplete(1.0);
        final Task st32 = new Task(
            "Requirements 1", 
            dateYear(30), dateYear(50)
        );
        st32.setPercentComplete(1.0);
        t3.addSubtask(st31);
        t3.addSubtask(st32);
        s1.add(t3);

        // and another...
        final Task t4 = new Task(
            "Design Phase", 
            dateYear(30), dateYear(50)
        );
        final Task st41 = new Task(
             "Design 1", 
             dateYear(30), dateYear(50)
        );
        st41.setPercentComplete(1.0);
        final Task st42 = new Task(
            "Design 2", 
            dateYear(30), dateYear(50)
        );
        st42.setPercentComplete(1.0);
        final Task st43 = new Task(
            "Design 3", 
            dateYear(30), dateYear(50)
        );
        st43.setPercentComplete(0.50);
        t4.addSubtask(st41);
        t4.addSubtask(st42);
        t4.addSubtask(st43);
        s1.add(t4);

        final Task t5 = new Task(
            "Design Signoff", dateYear(30), dateYear(50)
        ); 
        s1.add(t5);
                        
        final Task t6 = new Task(
            "Alpha Implementation", dateYear(30), dateYear(50)
        );
        t6.setPercentComplete(0.60);
        
        s1.add(t6);
        
        final Task t7 = new Task(
            "Design Review", dateYear(30), dateYear(50)
        );
        t7.setPercentComplete(0.0);
        s1.add(t7);
        

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);

        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
    
    private static Date dateYear(final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, 01, 01, 01, 0, 0);
        final Date result = calendar.getTime();
        return result;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final GanttDemo2 demo = new GanttDemo2("Gantt Chart Demo 2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
