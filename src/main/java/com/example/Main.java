package com.example;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import com.example.OrderManager.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Bar> bars = new ArrayList<>();

        Calculate.initialize(bars, "currData.txt");
        BarSeries series = new BaseBarSeries("Chart", bars);
        float accountSize = 1000;
        int testCount = 5;
        int totalBars = series.getBarCount();
        int portionSize = totalBars / testCount;
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);
        // used for position sizing
        // Add your indicators for the strategy here
        ATRIndicator ATR = new ATRIndicator(series, 14);
        // Also update ordermanager
        OrderManager orderManager = new OrderManager(series, closePriceIndicator,accountSize, ATR);

        Statistics stats = orderManager.processOrders(0,portionSize);

        
        
        Display.displayStatistics(stats);
        Display.displayEquityCurve(stats.equityCurve);
        TradeUtility.exportTrades(stats.trades, "trades.csv");
        
    }
}