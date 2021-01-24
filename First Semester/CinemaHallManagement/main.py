def buy_Ticket(seat, name, hall_name, full, student, ftype):
    if ftype == "full":
        full.append(hall_name + "/" + seat) # add full fare
    elif ftype == "student":
        student.append(hall_name + "/" + seat) # add student fare
    else:
        print("Check your command! Are you full or student?")
        return 0

def CREATEHALL(Commands, halls):
    #argument control
    if len(Commands) < 3:
        print("Error: Not enough parameters for creating a hall!")
    elif len(Commands) > 3:
        print("Error: Too much parameters for creating a hall!")
    else:
        hall_name = Commands[1]
        AxB = Commands[2]
        if hall_name not in halls: # hall control
            # detecting rows and columns
            RowXColumn.append(hall_name+"/"+AxB)
            row_column = AxB.split("x")
            numberOfRows = row_column[0]
            numberOfColumns = row_column[1]
            # Control
            if "-" in row_column :
                print("Please don't use negative numbers")
            elif int(numberOfRows) > 26: # for Turkish characters change with "29" and change rows list.
                print("Please enter lower than 26(A to Z)")
            elif int(numberOfRows) == 0 :
                print("Please don't enter zero")
            elif int(numberOfColumns) > 100:
                print("Please enter lower than 100 (1 to 100)")
            elif int(numberOfColumns) == 0 :
                print("Please don't enter zero")
            else:
                # creating hall
                hall = []
                for number1 in range(0, int(numberOfRows)):
                    for row in rows[number1]:
                        for number2 in range(0, int(numberOfColumns)):
                            column = columns[number2]
                            hall.append(row+column)
                halls[hall_name] = str(hall)[1:-1]
                print("Hall '" + hall_name + "' having", int(numberOfRows)*int(numberOfColumns), "seats has been created")
        else:
            print("Warning: Cannot create the hall for the second time. The cinema has already", hall_name)


def SELLTICKET(Commands, halls, full, student):
    name, ftype, hall_name = Commands[1], Commands[2], Commands[3]
    if ftype == "student" or ftype == "full":
        if len(Commands) < 4:
            print("Not enough parameters")
        else:
            if hall_name in halls: # hall check
                hall_seats = list(halls[hall_name][1:-1].split("', '"))
                for number3 in range(4, len(Commands)):  # for multiple input
                    a_seat = Commands[number3]
                    seats = []
                    if "-" in a_seat:  # for example --> X1-4
                        # divide the X1-4 to X, 1, 4
                        seat_column = a_seat[0]
                        seat_finding = list(a_seat[1:].split("-"))
                        seat_row_first = int(seat_finding[0])
                        seat_between = int(seat_finding[1]) - int(seat_finding[0])
                        for number4 in range(0, seat_between):
                            # check sold or not
                            if hall_name + "/" + seat_column + str(seat_row_first + number4) in student or \
                                    hall_name + "/" + seat_column + str(seat_row_first + number4) in full:
                                seats = []
                                print("Warning: The seats", a_seat, "cannot be sold to", name, "due some of them have already been sold!")
                                break
                            # check is the seat in hall
                            elif seat_column + str(seat_row_first + number4) not in hall_seats:
                                seats = []
                                print("Error: The hall '" + hall_name + "' has less column than the specified index", a_seat,
                                      "! ")
                                break
                            else:
                                # saving seats with their hall names
                                seats.append(seat_column + str(seat_row_first + number4))
                        if seats != []:
                            buyTicket = 1
                            for seat in seats:
                                if buy_Ticket(seat, name, hall_name, full, student, ftype) == 0:
                                    buyTicket = 0
                                    break
                            if buyTicket != 0:
                                print("Success:", name, "has bought", a_seat, "at", hall_name)
                    else: # for just one seat
                        # check sold or not
                        if hall_name + "/" + a_seat in student or hall_name + "/" + a_seat in full:
                            print("The seat", a_seat, "cannot be sold to", name, "due already been sold!")
                        else:
                            # check is the seat in hall
                            if a_seat in hall_seats:
                                if buy_Ticket(a_seat, name, hall_name, full, student, ftype) != 0:
                                    print("Success:", name, "has bought", a_seat, "at", hall_name)
                            else:
                                print("Error: The hall '" + hall_name + "' has less column than the specified index",
                                      a_seat, "!")
            else:
                print("Error:", hall_name, "is not created yet so you can't sell ticket at this cinema hall")
    else:
        print("Error: You should define fare type as student or full")

def CANCELTICKET(Commands, halls, full, student):
    hall_name = Commands[1]
    if hall_name in halls:  # hall check
        hall_seats = list(halls[hall_name][1:-1].split("', '"))
        seats = []
        for number3 in range(2, len(Commands)):  # for multiple input
            a_seat = Commands[number3]
            if "-" in a_seat:  # for example --> X1-4
                seat_column = a_seat[0]
                seat_finding = list(a_seat[1:].split("-"))
                seat_row_first = int(seat_finding[0])
                seat_between = int(seat_finding[1]) - int(seat_finding[0])
                for number4 in range(0, seat_between + 1):
                    seats.append(seat_column + str(seat_row_first + number4))
            else: # for just one seat
                seats.append(a_seat)
        for seat in seats:
            if hall_name + "/" + seat in student:
                student.remove(hall_name + "/" + seat)  # remove from student fare
                print("Success: The seat", seat, "at", hall_name, "has been canceled and now ready to be sell again")
            elif hall_name + "/" + seat in full:
                full.remove(hall_name + "/" + seat)  # remove from full fare
                print("Success: The seat", seat, "at", hall_name, "has been canceled and now ready to be sell again")
            elif seat in hall_seats: # check is the seat in hall
                print("The seat", seat, "at", hall_name, "has already been free! Nothing to cancel")
            else:
                print("Error: The hall '" + hall_name + "' has less column than the specified index", seat, "!")
    else:
        print("Error:", hall_name, "is not created yet so you can't cancel ticket at this cinema hall")

def BALANCE(Commands, full, student):
    for number10 in range(1,len(Commands)): # for multiple inputs
        hall_name = Commands[number10]
        num_of_full, num_of_student = 0, 0
        for hall_F in full:
            if hall_name in hall_F.split("/"):
                num_of_full += 1  # calculating how many full fare
        for hall_S in student:
            if hall_name in hall_S.split("/"):
                num_of_student += 1  # calculating how many student fare
        if hall_name in halls:  # hall check
            sum_of_student = num_of_student * 5
            sum_of_full = num_of_full * 10
            print("Hall report of", hall_name, "\n" + "-" * (len(hall_name) + 15),
                  "\nSum of students = ", sum_of_student, "Sum of full fares =", sum_of_full, "Overall =",
                  int(sum_of_student + sum_of_full))
        else:
            print("Error:", hall_name, "is not created yet so you can't show balance at this cinema hall")

def SHOWHALL(Commands, halls, full, student):
    while len(Commands) == 2:
        hall_name = Commands[1]
        break
    if len(Commands) > 2:
        print("Too much parameters for showing hall!")
    elif len(Commands) < 2:
        print("Not enough parameters for showing hall!")
    elif hall_name in halls: # hall check
        for hall_row_column in RowXColumn: # taking hall limits(rows and columns)
            if hall_row_column.split("/")[0] == hall_name:
                AxB = hall_row_column.split("/")[1]
        row_column = AxB.split("x")
        numberOfRows = row_column[0]
        numberOfColumns = row_column[1]
        hall_seats = list(halls[hall_name][1:-1].split("', '"))
        student_list, full_list = [], []
        for ticket_F in full: # making full list
            if hall_name == ticket_F.split("/")[0]:
                full_list.append(ticket_F.split("/")[1]) # create full fares list
        for ticket_S in student: # making student list
            if hall_name == ticket_S.split("/")[0]:
                student_list.append(ticket_S.split("/")[1]) #create student fares list
        print("Printing hall layout of", hall_name)
        for number1 in range(int(numberOfRows) - 1, -1, -1): # reversed alphabet
            for row in rows[number1]:
                print(row, end="") # print letter to start
                for number2 in range(0, int(numberOfColumns)):
                    column = columns[number2]
                    # print X or F or S
                    if row + column in full_list:
                        print("  F", end="")
                    elif (row + column) in student_list:
                        print("  S", end="")
                    elif (row + column) in hall_seats:
                        print("  X", end="")
                    else:
                        print("Error.")
                print("\n", end="") # next line
        print("   ", end="")
        for number2 in range(0, int(numberOfColumns)): # print numbers to bottom
            if number2 < 9:
                print(number2, end="  ")
            elif number2 > 8:
                print(number2, end=" ")
        print("")
    else:
        print("Error:", hall_name, "is not created yet so you can't show this cinema hall")

# MAIN FUNCTION
import sys
for Out in 1,2:
    if Out == 2:
        sys.stdout = open("out.txt", "w+") # write all outputs to the file
    input_File_name = sys.argv[1]  # taking input file name
    inputFile = open(input_File_name, "r")  # reading input file
    columns = [str(i) for i in range(0, 51)]
    rows = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
    # for Turkish letters:
    # rows = [ "A", "B", "C", "Ç" "D", "E", "F", "G", "Ğ" "H", "I", "İ", "J", "K", "L" ,"M" ,"N", "O", "Ö", "P", "R", "S", "Ş", "T", "U", "Ü", "V", "Y", "Z"]
    halls = {}
    RowXColumn = []
    full, student = [], []
    for line in inputFile.readlines():
        Commands = line.split(" ")
        Commands[len(Commands) - 1] = Commands[len(Commands) - 1].strip("\n")  # for remove \n
        if Commands[0] == "CREATEHALL":
            CREATEHALL(Commands, halls)
        elif Commands[0] == "SELLTICKET":
            SELLTICKET(Commands, halls, full, student)
        elif Commands[0] == "CANCELTICKET":
            CANCELTICKET(Commands, halls, full, student)
        elif Commands[0] == "BALANCE":
            BALANCE(Commands, full, student)
        elif Commands[0] == "SHOWHALL":
            SHOWHALL(Commands, halls, full, student)
        else:
            print("Check your commands!")
inputFile.close()
# İbrahim Burak Tanrıkulu
