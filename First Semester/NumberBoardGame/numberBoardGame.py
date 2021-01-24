def roll(number):
    rolled = (number*10)%10
    if 9 > rolled >= 5:
        return int((number*10) % 10 + 1)/10.0
    elif rolled > 9:
        return 0.9
    return int((number*10) % 10)/10.0


def random_create(inputName):
    import random
    the_file = open(inputName, "w")
    rowxcolumn = input("How many row and column do you want?\n")
    row = rowxcolumn.split(" ")[0]
    column = rowxcolumn.split(" ")[1]
    for i in range(0, int(row)):
        for j in range(0, int(column)):
            if j == int(column)-1:
                print(str(int(roll(random.random()) * 10)), file=the_file, end="")
                break
            print(str(int(roll(random.random())*10))+" ", file=the_file, end="")
        if i == int(row) - 1 :
            break
        print("", file=the_file)
    the_file.close()
    

def print_it(rowN, columnN, rowXcolumn):  # print function
    for rowX in range(1, rowN + 1):
        for columnX in range(1, columnN + 1):
            for number in rowXcolumn:
                if str(rowX) + "-" + str(columnX) == number.split("-")[0]+"-"+number.split("-")[1]:
                    print(number.split("-")[2], end=" ")
        print("")


def check_it():  # checks neighbourhood
    other_number = 0
    for number in range(0, len(rowXcolumn)):
        row = rowXcolumn[number][0]
        column = rowXcolumn[number][2]
        the_number = rowXcolumn[number][4]
        the_other_number = 0
        if the_number != " ":
            for number2 in range(0, len(rowXcolumn)):
                if rowXcolumn[number2] == str(int(row) - 1) + "-" + column + "-" + the_number:  # above
                    the_other_number = 1
                    break
                if rowXcolumn[number2] == str(int(row) + 1) + "-" + column + "-" + the_number:  # below
                    the_other_number = 1
                    break
                if rowXcolumn[number2] == row + "-" + str(int(column) - 1) + "-" + the_number:  # left
                    the_other_number = 1
                    break
                if rowXcolumn[number2] == row + "-" + str(int(column) + 1) + "-" + the_number:  # right
                    the_other_number = 1
                    break
            if the_other_number == 1:
                other_number = 1
                break
    if other_number == 1:
        return 1


def fibonacci(the_other_number):  # recursive fibonacci calculator
    if the_other_number == 2:
        return 1
    elif the_other_number == 1:
        return 1
    else:
        return fibonacci(the_other_number-2)+fibonacci(the_other_number-1)


def neighbour_numbers(row, column, rowXcolumn, rowN):  # appends neighbours to coordinates list
    abcd = 1
    coordinates = [row + "-" + column]
    while abcd != 0:
        myList = []
        for element in coordinates:
            myList.append(element)  # myList is a copy of coordinates
        for coordinate in coordinates:
            row = coordinate.split("-")[0]
            column = coordinate.split("-")[1]
            for element in rowXcolumn:
                if element[0:3] == str(int(row) - 1) + "-" + column:
                    if element[4] == the_number:
                        if str(int(row) - 1) + "-" + column not in coordinates:
                            coordinates.append(str(int(row) - 1) + "-" + column)
                if element[0:3] == str(int(row) + 1) + "-" + column:
                    if element[4] == the_number:
                        if str(int(row) + 1) + "-" + column not in coordinates:
                            coordinates.append(str(int(row) + 1) + "-" + column)
                if element[0:3] == str(row) + "-" + str(int(column) - 1):
                    if element[4] == the_number:
                        if row + "-" + str(int(column) - 1) not in coordinates:
                            coordinates.append(row + "-" + str(int(column) - 1))
                if element[0:3] == row + "-" + str(int(column) + 1):
                    if element[4] == the_number:
                        if row + "-" + str(int(column) + 1) not in coordinates:
                            coordinates.append(row + "-" + str(int(column) + 1))
        if myList == coordinates:  # if coordinates isn't change break searching new neighbours
            abcd = 0
    the_other_number = len(coordinates)
    global point
    if the_other_number > 1:
        point += int(the_number) * fibonacci(the_other_number)  # calculating the point
        erase_this(coordinates, rowXcolumn, rowN)
    else:
        return 0


def erase_this(coordinates, rowXcolumn, rowN):  # firstly change numbers to " " and drop and slide
    for element in coordinates:
        for number in range(0, len(rowXcolumn)):
            if rowXcolumn[number][0:3] == element:
                rowXcolumn[number] = rowXcolumn[number][0:4] + " "
    print("")
    i = 0
    while i != rowN:
        i += 1
        drop_it(rowXcolumn)
    i = 0
    while i != columnN:
        i += 1
        slide_it(rowXcolumn, rowN)


def refresh_row(rowXcolumn, rowN, columnN):
    for number in range(1, columnN+1):
        if "1-"+str(number)+"- " in rowXcolumn:
            if number == columnN:
                for number in range(0, columnN):
                    del rowXcolumn[0]
                for number in range(0, len(rowXcolumn)):
                    rowXcolumn[number] = str(int(rowXcolumn[number][0])-1) + rowXcolumn[number][1:5]
                return 1
        else:
            break


def refresh_column(rowXcolumn, rowN, columnN):
    for number in range(1, rowN+1):
        if str(number)+"-"+str(columnN)+"- " in rowXcolumn:
            if number == rowN:
                for number in range(rowN, 0, -1):
                    del rowXcolumn[number*columnN-1]
                return 1
        else:
            break


def slide_it(rowXcolumn, rowN):  # if the number ,which in bottom row, is empty; replace all numbers with right column
    for number in range(0, len(rowXcolumn)):
        column = rowXcolumn[number][2]
        if str(rowN)+"-"+column+"- " in rowXcolumn:
            for row in range(1, int(rowN) + 1):
                for number2 in range(0, len(rowXcolumn)):
                    for number3 in range(0, len(rowXcolumn)):
                        if str(row) + "-" + str(int(column) + 1) + "-" in rowXcolumn[number2]:
                            if rowXcolumn[number3] == str(row) + "-" + str(int(column)) + "- ":
                                rowXcolumn[number3] = rowXcolumn[number3][0:4] + rowXcolumn[number2][4]
                                rowXcolumn[number2] = rowXcolumn[number2][0:4] + " "
                                break
        else:
            continue


def drop_it(rowXcolumn):  # drop down if the number is empty
    for number in range(0, len(rowXcolumn)):
        row = rowXcolumn[number][0]
        column = rowXcolumn[number][2]
        if str(int(row)+1)+"-"+column+"- " in rowXcolumn:
            rowXcolumn[rowXcolumn.index(str(int(row)+1)+"-"+column+"- ")] = rowXcolumn[rowXcolumn.index(str(int(row)+1)+"-"+column+"- ")][0:4] + rowXcolumn[number][4]
            rowXcolumn[number] = rowXcolumn[number][0:4]+" "


import sys
inputName = sys.argv[1]
random_create(inputName)
inputFile = open(inputName, "r")
rowXcolumn = []
myList = []
global rowN
rowN = 0
for row in inputFile.readlines():
    rowN += 1
    row = row.strip("\n")
    global columnN
    columnN = 0
    for column in row.split(" "):
        columnN += 1
        rowXcolumn.append(str(rowN)+"-"+str(columnN)+"-"+column)
point = 0
print_it(rowN, columnN, rowXcolumn)
while check_it() == 1:
    row_column = input("Please enter row and column:")
    if len(row_column.split(" ")) != 2:
        row = "0"  # this gives error
        column = "0"  # this gives error
    elif len(row_column.split(" ")[0]) == 0:
        row = "0"  # this gives error
        column = "0"  # this gives error
    elif len(row_column.split(" ")[1]) == 0:
        row = "0"  # this gives error
        column = "0"  # this gives error
    else:
        row = row_column.split(" ")[0]
        column = row_column.split(" ")[1]
    for number in rowXcolumn:
        if row + "-" + column == number[0:3]:
            if number[4] == " ":
                print("Please enter a correct size!")
            else:
                the_number = number[4]
                if neighbour_numbers(row, column, rowXcolumn, rowN) == 0:
                    print_it(rowN, columnN, rowXcolumn)
                    print("Please enter a number which has a neighbour!")
                else:
                    i = 0
                    while i != rowN:
                        if refresh_row(rowXcolumn, rowN, columnN) == 1:
                            rowN -= 1
                            i += 1
                        else:
                            i = rowN
                    i = 0
                    while i != columnN:
                        if refresh_column(rowXcolumn, rowN, columnN) == 1:
                            columnN -= 1
                            i += 1
                        else:
                            i = columnN
                    print_it(rowN, columnN, rowXcolumn)
                    print("Your score is:" + str(point))
            abc = 1
            break
        else:
            abc = 0
    if abc == 0:
        print("Please enter a correct size!")
print("Game over!")
# İbrahim Burak Tanrıkulu