# My First Data Miner Program

'''
This program
a) performs data cleaning process to remove missing attribute values present in the database.
b) calculates probability of being breast cancer of an imaginary patient
   by evaluationg his/her sample results provided as command-line argument.
'''

# Starter code that reads database named 'WBC.data' and loads it into a dictionary 'dataDic'

# Reads the datafile. Note: WBC.data should be located where this file belongs.
dataFile = open('WBC.data','r').read()

# Makes data file ready to use by assigning every record to a dictionary class name dataDic.
dataDic = {i.split(',')[0]: i.split(',')[1:]  for i in dataFile.split('\n')} 

def funDataClean():
    Attr_Sum_benign = {}
    Attr_Sum_malignant = {}
    Attr_Num_benign = {}
    Attr_Num_malignant = {}
    Unknown_vals = []
    for i in range(1, len(dataDic.values())+1): # reading all lines
        Attr_List = dataDic.get(str(i))
        for k in range(0,9): # attribute 1 to attribute 9
            attr = Attr_List[k]
            if attr != "?": # calculating average
                if Attr_List[-1] == "benign":
                    if Attr_Sum_benign.get(k) == None:
                        Attr_Sum_benign[k] = int(attr)
                        Attr_Num_benign[k] = 1
                    else:
                        sum = Attr_Sum_benign.get(k)
                        sum += int(attr)
                        Attr_Sum_benign[k] = sum
                        Attr_Num_benign[k] = Attr_Num_benign.get(k)+1
                elif Attr_List[-1] == "malignant":
                    if Attr_Sum_malignant.get(k) == None:
                        Attr_Sum_malignant[k] = int(attr)
                        Attr_Num_malignant[k] = 1
                    else:
                        sum = Attr_Sum_malignant.get(k)
                        sum += int(attr)
                        Attr_Sum_malignant[k] = sum
                        Attr_Num_malignant[k] = Attr_Num_malignant.get(k) + 1
            else: # collecting unknown values
                Unknown_vals.append(str(i)+","+str(k))
    Sum_of_misval = 0
    Num_of_misval = 0
    for j in range(0, len(Unknown_vals)): # finding unknown values
        List1 = Unknown_vals[j].split(",")
        ID = List1[0] # line which has unknown value
        attrX = List1[1] # value position
        List2 = dataDic.get(str(ID))
        if List2[-1] == "benign":
            List2[int(attrX)] = round(Attr_Sum_benign[int(attrX)] / Attr_Num_benign[int(attrX)])
            Attr_Sum_benign[int(attrX)] += Attr_Sum_benign[int(attrX)]/Attr_Num_benign[int(attrX)]
            Attr_Num_benign[int(attrX)] += 1
            Sum_of_misval += List2[int(attrX)]
            Num_of_misval += 1
        elif List2[-1] == "malignant":
            List2[int(attrX)] = round(Attr_Sum_malignant[int(attrX)]/Attr_Num_malignant[int(attrX)])
            Attr_Sum_malignant[int(attrX)] += Attr_Sum_malignant[int(attrX)]/Attr_Num_malignant[int(attrX)]
            Attr_Num_malignant[int(attrX)] += 1
            Sum_of_misval += List2[int(attrX)]
            Num_of_misval += 1
        dataDic[str(ID)] = List2
    avg_misval = round(Sum_of_misval / Num_of_misval, 4)
    print("The average of all missing values is :", avg_misval)

# Performas step-wise search in WBC database

def performStepWiseSearch():
    import sys
    sample = sys.argv[1]
    sample_list1 = sample.split(",")
    pos_case = 0
    neg_case = 0
    for l in range(1, len(dataDic.values())+1): # reading all lines
        number_try = 0
        Attr_list = dataDic.get(str(l))
        for attrX in range(0, 9): # attribute 1 to attribute 9
            sample_list = sample_list1[int(attrX)].split(":")
            if sample_list[0] == "<":
                if int(Attr_list[attrX]) < int(sample_list[1]):
                    number_try += 1
            if sample_list[0] == "<=":
                if int(Attr_list[attrX]) <= int(sample_list[1]):
                    number_try += 1
            if sample_list[0] == ">":
                if int(Attr_list[attrX]) > int(sample_list[1]):
                    number_try += 1
            if sample_list[0] == ">=":
                if int(Attr_list[attrX]) >= int(sample_list[1]):
                    number_try += 1
            if sample_list[0] == "!=":
                if int(Attr_list[attrX]) != int(sample_list[1]):
                    number_try += 1
            if sample_list[0] == "=":
                if int(Attr_list[attrX]) == int(sample_list[1]):
                    number_try += 1
            if sample_list[0] == "?":
                number_try += 1
            if number_try == 9:
                if Attr_list[-1] == "benign":
                    neg_case = neg_case + 1
                if Attr_list[-1] == "malignant":
                    pos_case += 1
    print('\nTest Results:\n'
      '----------------------------------------------'
      '\nPositive (malignant) cases            : ' + str(pos_case) +
      '\nNegative (benign) cases               : ' + str(neg_case) +
      '\nThe probability of being positive     : ' + str(round(pos_case/(pos_case+neg_case),4)) +
      '\n----------------------------------------------')

# 1st phase: Cleaning WBC Database

funDataClean()

# 2nd phase: Retrieving knowledge from WBC dataset

performStepWiseSearch()
