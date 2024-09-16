import csv
import pandas as pd
import numpy as np
from datetime import datetime

def datacheck(file):
    print("reading from: " + file)
    totalcells = {}
    missing_data = {}

    with open(file, mode='r', newline='') as csvfile:
        reader = csv.reader(csvfile)
        headers = next(reader)
        column_headers = headers
        #initialize total cells and missing data
        for col in range(len(column_headers)):
            totalcells[col] = 0
            missing_data[col] = 0
        for row in reader:
            for col, cell in enumerate(row):
                if col < len(column_headers):  
                    totalcells[col] += 1
                    if cell.strip() == '':
                        missing_data[col] += 1
            
    for col,header in enumerate(column_headers):
        pct_missing = missing_data[col]/(totalcells[col] -1) * 100
        print(f"Column '{header}' is missing {pct_missing:.2f}% of its data")

def datacleaning(file):
    df = pd.read_csv(file)
    df.info()

    # replace empty cells with nan
    df.replace('',np.nan, inplace=True)
    #get most common city imputate with mode
    def_city = df['city'].mode()[0]
    df.fillna({'city': def_city}, inplace=True)
    # default state is South Carolina
    df.fillna({'state':"South Carolina"}, inplace=True)
    # make zip codes the same format - then find most common to imputate
    df['zip_code']= df['zip_code'].str.split('-').str[0]
    def_zipcode = df['zip_code'].mode()[0]
    df.fillna({'zip_code':def_zipcode},inplace=True)
    # imputate gender with mode
    def_gender = df['gender'].mode()[0]
    df.fillna({'gender':def_gender},inplace=True)

    # get average age for default value
    df['dob'] = pd.to_datetime(df['dob'])
    today = pd.to_datetime(datetime.now())
    df['age'] = (today - df['dob']).dt.days // 365
    average_age = df['age'].mean()
    def_dob = today - pd.DateOffset(years=int(average_age))
    print(f"Average bday is {def_dob.strftime('%Y/%m/%d')}")
    df.info()


path = './data/DriverTraining-ForInClassLearning-2024.csv'
datacleaning(path)
