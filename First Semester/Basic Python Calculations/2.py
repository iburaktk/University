# Problem2: Even Number Evaluator
import sys
# This program finds even numbers and sums them and calculates even number rate.
if len(sys.argv) != 2 : # argument number check
    sys.exit("Please run with 1 argument!")
L = sys.argv[1] # L is a list of numbers
allowed_chars = "0123456789,-" # argument characters check
for char in L:
    if char not in allowed_chars:
        sys.exit("Please check the argument.")
L = L.split(",") # taking numbers from argument
E = [] # E is only even numbers list
for element in L: # Evens list making
    if int(element) % 2 == 0 :
        if int(element) > 0:
            E.append(element)
print("Even Numbers: ", str(E).replace("'",""))
sum_even = 0
for element in E :
    sum_even = sum_even + int(element)
print("Sum of Even Numbers: ", sum_even)
sum_all = 0
for element in L:
    if int(element) > 0 :
        sum_all = sum_all + int(element)
number_rate = sum_even / sum_all
print("Even Number Rate: ", round(number_rate , 3))