package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
//import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

import com.example.OrderManager.Statistics;

import java.util.Date;


public class Display {

    public static void displayBars(BarSeries series) {
        int dataSize = series.getBarCount();

        Date[] date = new Date[dataSize];
        double[] high = new double[dataSize];
        double[] low = new double[dataSize];
        double[] volume = new double[dataSize];
        double[] open = new double[dataSize];
        double[] close = new double[dataSize];

        int index = 0;
        for (Bar bar : series.getBarData()) {
            date[index] = Date.from(bar.getEndTime().toInstant());
            high[index] = bar.getHighPrice().doubleValue();
            low[index] = bar.getLowPrice().doubleValue();
            open[index] = bar.getOpenPrice().doubleValue();
            close[index] = bar.getClosePrice().doubleValue();
            volume[index] = bar.getVolume().doubleValue();
            index++;
        }

        DefaultHighLowDataset dataset = new DefaultHighLowDataset("BTC Minutes", date, high, low, open, close, volume);

        DateAxis dateAxis = new DateAxis("Date");
        NumberAxis yAxis = new NumberAxis("Price");
        CandlestickRenderer renderer = new CandlestickRenderer();
        
        XYPlot plot = new XYPlot(dataset, dateAxis, yAxis, renderer);

        JFreeChart chart = new JFreeChart(
            "BTC Hourly Candlestick Chart",
            JFreeChart.DEFAULT_TITLE_FONT,
            plot,
            false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Enable horizontal scrolling
        chartPanel.setDomainZoomable(true);
        chartPanel.setMouseWheelEnabled(true);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void displayStatistics(Statistics stats) {
        // Create column names
        String[] columnNames = {"Category", "Value"};

        // Create data
        Object[][] data = {
                {"Winning trade count:\t", stats.win},
                {"Lossing trade count:\t", stats.loss},
                {"Profit/Loss:\t", stats.profitLoss},
                {"Win rate:\t", Math.round((double) (stats.win) / (stats.loss) * 100.0) / 100.0},
                {"Number of Buys:\t", stats.buyCount},
                {"Number of Sells:\t", stats.sellCount},
                {"Total trades:\t", stats.sellCount+stats.buyCount},
                {"Max drawdown(%):\t", stats.maxDrawdownPercentage}
        };

        // Create a table model
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        // Create a table using the model
        JTable table = new JTable(tableModel);

        // Set up a custom cell renderer to add an outline
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Add a border to each cell
                if (row % 2 == 0) {
                    ((JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
                }
                return c;
            }
        });

        // Wrap the table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Create and set up the window
        JFrame frame = new JFrame("Trading Statistics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the scroll pane to the frame
        frame.add(scrollPane);

        // Display the window
        frame.pack();
        frame.setVisible(true);
    }



    public static void displayEquityCurve(List<Float> equityValues) {
        XYSeries series = new XYSeries("Equity Curve");
        for (int i = 0; i < equityValues.size(); i++) {
            series.add(i, equityValues.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
            "Equity Curve",
            "Time",
            "Equity",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}


       
