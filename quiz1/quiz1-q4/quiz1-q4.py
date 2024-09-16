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
    # set city names to title case 
    df['city'] = df['city'].str.title()
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

    # get average age for default value- convert back to a birthday in m/d/y format
    df['dob'] = pd.to_datetime(df['dob'], errors='coerce')
    df['class_date'] = pd.to_datetime(df['class_date'])
    # check for birthdays after class date (not possible) and make null
    df.loc[df['class_date'] < df['dob'], 'dob'] = ''
    # get average age
    today = pd.to_datetime(datetime.now())
    df['age'] = (today - df['dob']).dt.days // 365
    average_age = df['age'].mean()
    # replace as default value
    df.fillna({'age':average_age},inplace=True)
    def_dob = today - pd.DateOffset(years=int(average_age))
    df.fillna({'dob':def_dob.strftime('%m/%d/%Y')},inplace=True)


    df.info()

    # return percentages for male and female
    pct_gender = df['gender'].value_counts(normalize=True)*100
    print(pct_gender)

    # percentages for gender by month
    df['class_date'] = pd.to_datetime(df['class_date'])
    # get month
    df['month'] = df['class_date'].dt.month_name()
    # group by month and gender, and reshape dataframe for visualization
    gender_month = df.groupby(['month', 'gender']).size().unstack()
    gender_month_pct = gender_month.div(gender_month.sum(axis=1), axis=0) * 100
    print(gender_month_pct)

    # get age by gender
    age_gender = df.groupby('gender')['age'].mean()
    print(age_gender)

    # get % taking by city
    by_city = df['city'].value_counts(normalize=True)*100
    # pd.set_option('display.max_rows', None)  
    # pd.set_option('display.max_columns', None)
    print(by_city)


path = 'C:\\Users\\tyler\\CSCE580-Fall2024-tbeasley-Repo\\quiz1\\quiz1-q4\\data\\DriverTraining-ForInClassLearning-2024.csv'
datacheck(path)
datacleaning(path)
