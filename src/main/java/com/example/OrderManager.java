package com.example;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.List;

class OrderManager {
    private float accountSize;
    private BarSeries series;
    private ClosePriceIndicator closePriceIndicator;
    
    private ATRIndicator ATR;

    public OrderManager(BarSeries series, ClosePriceIndicator closePriceIndicator, float accountSize, ATRIndicator ATR) {
        this.series = series;
        this.closePriceIndicator = closePriceIndicator;
        this.ATR = ATR;
        this.accountSize = accountSize;
    }

    public class Statistics {
        public int win = 0;
        public int buyCount = 0;
        public int sellCount = 0;
        public int loss = 0;
        public float profitLoss = 0;
        public List<Float> equityCurve = new ArrayList<>();
        public float maxDrawdownPercentage = 0; // Store the maximum percentage drawdown
        public List<TradeUtility.TradeRecord> trades = new ArrayList<>();
    }
            
    public Statistics processOrders(int startIndex, int endIndex) {
        Statistics stats = new Statistics();
        List<TradeUtility.TradeRecord> trades = new ArrayList<>();
        stats.equityCurve.add(accountSize);
        if (startIndex < 0) startIndex = 0;
        if (endIndex > series.getBarCount()) endIndex = series.getBarCount();
        boolean newTrade = false;
        boolean liveBuy = false;
        boolean liveSell = false;
        float profitLoss = 0;
        String direction = "";
        String entryDate = "";
        String exitDate = "";
        
        float stopPrice = 0;
        float entryPrice = 0;
        float profitPrice = 0;

        int win = 0;
        int loss = 0;
        
        float runningMax = Float.NEGATIVE_INFINITY;  // Initialize running maximum

        for (int i = startIndex; i < endIndex; i++) {
            System.out.println(ATR+"\n");
            float currentPrice = closePriceIndicator.getValue(i).floatValue();
            if(!liveBuy && !liveSell){
                // update the buyOrder to inlcude your indicators
                if(Calculate.buyOrder(series, i, ATR)){
                    entryPrice = currentPrice;
                    profitPrice = Calculate.buyOrderProfit(series, i, ATR);
                    stopPrice = Calculate.buyOrderStop(series, i, ATR);
                    stats.buyCount++;
                    entryDate = series.getBar(i).getEndTime().toString();
                    direction = "Long";
                    liveBuy = true;
                // update the sellOrder to include your indicators
                }else if(Calculate.sellOrder(series, i, ATR)){
                    entryPrice = currentPrice;
                    stopPrice = Calculate.sellOrderStop(series, i, ATR);
                    profitPrice = Calculate.sellOrderProfit(series, i, ATR);
                    stats.sellCount++;
                    entryDate = series.getBar(i).getEndTime().toString();
                    direction = "Short";
                    liveSell = true;
                }
            }

                        /// Update running maximum
            runningMax = Math.max(runningMax, stats.equityCurve.get(stats.equityCurve.size() - 1));


            // Calculate drawdown and update maximum percentage drawdown
            float drawdown = runningMax - stats.equityCurve.get(stats.equityCurve.size() - 1);
            float drawdownPercentage = (runningMax != 0) ? (drawdown / runningMax) * 100 : 0;
            if (drawdownPercentage > stats.maxDrawdownPercentage) {
                stats.maxDrawdownPercentage = drawdownPercentage;
            }

            if (liveBuy && currentPrice <= stopPrice) {
                // closing buy due to loss
                // closing buy due to loss
                float currentProfitLoss = (entryPrice - currentPrice);  // Loss from buying
                float newBalance = stats.equityCurve.get(stats.equityCurve.size() - 1) - currentProfitLoss;  // Subtract loss from the last equity balance
                stats.equityCurve.add(newBalance);  // Update equity curve
                loss++;
                liveBuy = false;
                newTrade = true;    
            }

            if (liveBuy && currentPrice >= profitPrice) { 
                // closing buy due to win
                float currentProfitLoss = (currentPrice - entryPrice);  // Profit from buying
                float newBalance = stats.equityCurve.get(stats.equityCurve.size() - 1) + currentProfitLoss;  // Add profit to the last equity balance
                stats.equityCurve.add(newBalance);  // Update equity curve
                win++;
                liveBuy = false;
                newTrade = true;
            }

            if (liveSell && currentPrice >= stopPrice) { 
                // closing sell due to loss
                float currentProfitLoss = (currentPrice - entryPrice);  // Loss from selling
                float newBalance = stats.equityCurve.get(stats.equityCurve.size() - 1) - currentProfitLoss;  // Subtract loss from the last equity balance
                stats.equityCurve.add(newBalance);  // Update equity curve
                loss++;
                liveSell = false;
                newTrade = true;
            }

            if (liveSell && currentPrice <= profitPrice) { 
                // closing sell due to win
                float currentProfitLoss = (entryPrice - currentPrice);  // Profit from selling
                float newBalance = stats.equityCurve.get(stats.equityCurve.size() - 1) + currentProfitLoss;  // Add profit to the last equity balance
                stats.equityCurve.add(newBalance);  // Update equity curve
                win++;
                liveSell = false;
                newTrade = true;
            } 

            if (newTrade){
                exitDate = series.getBar(i).getEndTime().toString();
                TradeUtility.TradeRecord record = new TradeUtility.TradeRecord(entryPrice, currentPrice, entryPrice - currentPrice, direction, entryDate, exitDate, drawdownPercentage);
                stats.trades.add(record);
                newTrade = false;
            }
        }
        
        stats.win = win;
        stats.loss = loss;
        stats.profitLoss = stats.equityCurve.get(stats.equityCurve.size() - 1) - accountSize;
        return stats; 
    }
}
