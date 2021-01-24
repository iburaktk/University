def diamond(number, number2, direction):
	if number2 == number:  # for mid to down
		direction = 0
	if number2 != 0:
		print(" "*(number-number2)+ "*"*(number2*2-1)+ " "*(number-number2))
		if direction == 1:
			diamond(number, number2+1, direction)  # up
		if direction == 0:
			diamond(number, number2-1, direction)  # down
from sys import argv
number = int(argv[1])
if number > 0:
	diamond(number, 1, 1)
else:
    print("Please enter positive integers!")
# İbrahim Burak Tanrıkulu
