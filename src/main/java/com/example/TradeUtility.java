package com.example;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TradeUtility {

    // Inner class to represent a trade record
    public static class TradeRecord {
        private float entryPrice;
        private float exitPrice;
        private float profitLoss;
        private String direction; // "Buy" or "Sell"
        private String entryDate;
        private String exitDate;
        private float drawdownPercentage;

        public TradeRecord(float entryPrice, float exitPrice, float profitLoss, String direction, String entryDate, String exitDate, float drawdownPercentage) {
            this.entryPrice = entryPrice;
            this.exitPrice = exitPrice;
            this.profitLoss = profitLoss;
            this.direction = direction;
            this.entryDate = entryDate;
            this.exitDate = exitDate;
            this.drawdownPercentage = drawdownPercentage;
        }

        // Getters
        public float getEntryPrice() {
            return entryPrice;
        }

        public float getExitPrice() {
            return exitPrice;
        }

        public float getProfitLoss() {
            return profitLoss;
        }

        public String getDirection() {
            return direction;
        }

        public String getEntryDate() {
            return entryDate;
        }

        public String getExitDate() {
            return exitDate;
        }

        public float getdrawdownPercentage(){
            return drawdownPercentage;
        }

        // Setters
        public void setEntryPrice(float entryPrice) {
            this.entryPrice = entryPrice;
        }

        public void setExitPrice(float exitPrice) {
            this.exitPrice = exitPrice;
        }

        public void setProfitLoss(float profitLoss) {
            this.profitLoss = profitLoss;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public void setEntryDate(String entryDate) {
            this.entryDate = entryDate;
        }

        public void setExitDate(String exitDate) {
            this.exitDate = exitDate;
        }

        public void setdrawdownPercentage(Float drawdownPercentage) {
            this.drawdownPercentage = drawdownPercentage;
        }
    }
    
    // Method to export trades to a CSV file
    public static void exportTrades(List<TradeRecord> trades, String filePath) throws IOException {
        FileWriter out = new FileWriter(filePath);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("Entry Price", "Exit Price", "Profit/Loss", "Direction", "Entry Date", "Exit Date","Drawdown Percent"))) {
            for (TradeRecord trade : trades) {
                printer.printRecord(trade.getEntryPrice(), trade.getExitPrice(), trade.getProfitLoss(), trade.getDirection(), trade.getEntryDate(), trade.getExitDate(), trade.getdrawdownPercentage());
            }
        }
    }
}
