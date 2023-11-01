package com.example;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class Calculate {

        public static void initialize(List<Bar> bars, String file){
        try {
            FileReader reader = new FileReader(file);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                double open = Double.parseDouble(csvRecord.get("open"));
                double high = Double.parseDouble(csvRecord.get("high"));
                double low = Double.parseDouble(csvRecord.get("low"));
                double close = Double.parseDouble(csvRecord.get("close"));
                double volume = Double.parseDouble(csvRecord.get("volume"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");
                ZonedDateTime date = ZonedDateTime.parse(csvRecord.get("date"), formatter);

                Bar bar = new BaseBar(Duration.ofHours(1), date, open, high, low, close, volume);
                bars.add(bar);
            }

            csvParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean buyOrder(BarSeries series, int i, ATRIndicator ATR) {
        // implement buy conditions here
        return false;
    }
    
    public static boolean sellOrder(BarSeries series, int i, ATRIndicator ATR) {
        // implement sell conditions here
        return false;
    }

    // Automatically calculates stop loss and profit, can remove if want
    public static float buyOrderStop(BarSeries series, int i, ATRIndicator aTRs) {
        OpenPriceIndicator openPriceIndicator = new OpenPriceIndicator(series);
        float openPrice = openPriceIndicator.getValue(i+1).floatValue();
        float atrValue = aTRs.getValue(i).floatValue();
        float stopPrice = openPrice - (float)1 * atrValue;  
      
        return stopPrice;
    }
    
    public static float sellOrderStop(BarSeries series, int i, ATRIndicator ATR) {
        OpenPriceIndicator openPriceIndicator = new OpenPriceIndicator(series);
        float openPrice = openPriceIndicator.getValue(i+1).floatValue();
        float atrValue = ATR.getValue(i).floatValue();
        float stopPrice = openPrice + (float)1 * atrValue; 
      
        return stopPrice;
    }
    
    public static float buyOrderProfit(BarSeries series, int i, ATRIndicator ATR) {
        OpenPriceIndicator openPriceIndicator = new OpenPriceIndicator(series);  
        float openPrice = openPriceIndicator.getValue(i+1).floatValue();         
        float atrValue = ATR.getValue(i).floatValue();
        float profitPrice = openPrice + (float)2 * atrValue; 
      
        return profitPrice;
    }
    
    public static float sellOrderProfit(BarSeries series, int i, ATRIndicator aTRs) {
        OpenPriceIndicator openPriceIndicator = new OpenPriceIndicator(series);  
        float openPrice = openPriceIndicator.getValue(i+1).floatValue();     
        float atrValue = aTRs.getValue(i).floatValue();
        float profitPrice = openPrice - (float)2 * atrValue;  
      
        return profitPrice;
    }
}
