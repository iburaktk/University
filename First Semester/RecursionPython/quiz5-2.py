from sys import argv
number = int(argv[1])
list1 = [(" "*(number-i) + "*"*(i*2-1) + " "*(number-i)) for i in range(1, number+1)]  # up
list2 = [(" "*(number-i) + "*"*(i*2-1) + " "*(number-i)) for i in range(number-1, 0, -1)]  # down
list = list1 + list2
for element in list:
    print(element)
# İbrahim Burak Tanrıkuku
