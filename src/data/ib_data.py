import time
from ib_insync import *
from datetime import datetime, timedelta
import pandas as pd

# Get today's date
today = datetime.now()

def combine_and_deduplicate(csv_files):
    """
    Combine, deduplicate, and sort data from a list of CSV files.

    Args:
    - csv_files (list of str): List of CSV filenames to process.

    Returns:
    - str: Path to the combined and deduplicated CSV file.
    """
    # Create an empty DataFrame to store the combined data
    combined_data = pd.DataFrame()

    # Iterate through the CSV files and append their data to the combined_data DataFrame
    for file in csv_files:
        df = pd.read_csv(file)
        combined_data = pd.concat([combined_data, df], ignore_index=True)

    # Remove duplicates based on all columns (you can specify columns if needed)
    combined_data = combined_data.drop_duplicates()

    # Sort the combined data by the 'date' column (assuming 'date' is the first column)
    combined_data = combined_data.sort_values(by='date')

    # Save the combined and deduplicated data to a new CSV file
    combined_filename = 'combined_data.txt'
    combined_data.to_csv(combined_filename, index=False)

    print(f"Combined, deduplicated, and sorted data saved to '{combined_filename}'")
    return combined_filename

ib = IB()

try:
    # Connect with a longer timeout of 30 seconds
    ib.connect('127.0.0.1', 4001, clientId=1, timeout=30)
except Exception as e:
    print(f"Failed to connect: {e}")
    exit()

contract = Contract()
contract.symbol = "BTC"
contract.secType = "CRYPTO"
contract.currency = "USD"
contract.exchange = "PAXOS"
filenames = []
for i in range(24):
    print(i)
    # Initialize a variable to keep track of how many weekdays we've subtracted
    weekdays_to_subtract = i*365
    filename = f'currData_{i}.txt'
    filenames.append(filename)

# Start subtracting weekdays one by one, skipping weekends
    while weekdays_to_subtract > 0:
        today -= timedelta(days=1)
        if today.weekday() < 5:  # Monday (0) to Friday (4)
            weekdays_to_subtract -= 1
    # Format the result as 'YYYYMMDD HH:MM:SS'
    formatted_date = today.strftime('%Y%m%d %H:%M:%S')

    try:
        bars = ib.reqHistoricalData(
            contract,
            endDateTime=formatted_date,
            durationStr='1 Y',
            barSizeSetting='4 Hours',
            whatToShow='AGGTRADES',
            useRTH=True
        )
    except Exception as e:
        print(f"An error occurred while fetching data: {e}")
    else:
        if bars:
            df = util.df(bars)
            df.to_csv(filename, sep=',', index=False)
        else:
            print("bars is None, could not fetch data.")
time.sleep(5)
    
ib.disconnect()

combined_file = combine_and_deduplicate(filenames)






