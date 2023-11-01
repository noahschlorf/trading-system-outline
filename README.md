# Trading System Outline

A comprehensive trading system project designed to fetch historical data, process buy/sell orders, and display trading statistics.

## Table of Contents

- [Overview](#overview)
- [How to Input Your System](#how-to-input-your-system)
- [Running the System](#running-the-system)
- [Additional Resources](#additional-resources)

## Overview

1. **Maven Configuration**: The project uses Maven for dependency management. Dependencies include `ta4j-core`, `ta4j-examples`, and `commons-csv`.

2. **Data Retrieval**: Scripts connect to Interactive Brokers to fetch historical data for Bitcoin (BTC) traded on the PAXOS exchange.

3. **Java Classes**:
   - **Main Execution**: The main entry point of the application. Initializes trading bars, sets up indicators, and processes orders. Results are displayed.
   - **Order Management**: Manages trading orders, processes buy/sell orders, and calculates statistics.
   - **Testing Period Calculation**: Determines the in-sample and out-of-sample testing periods.
   - **Trade Utility**: Provides utility functions related to trades, including exporting trade records to a CSV file.

## How to Input Your System

1. **Data Input**: Modify the `ib_data.py` and `ib_dd.py` scripts if you wish to use different data sources.
   
2. **Java Implementation**:
   - Modify buy/sell order conditions in the `Calculate.java` class.
   - Adjust parameters or the file for trading bars initialization in the `Main.java` class.
   - Adjust testing periods in the `TestingPeriodCalc.java` class.

## Running the System

1. Ensure Maven is installed and run the `pom.xml` to fetch dependencies.
2. Execute the `Main.java` class to start the trading system.
