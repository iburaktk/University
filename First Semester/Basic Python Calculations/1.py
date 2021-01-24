#Problem1: Quadratic Equation Solver
import math
import sys
#ax^2 + bx + c = 0
#This program finds the roots of this equation
a = int(sys.argv[1])
if a == 0 :
    sys.exit("'a' cannot be zero!")
b = int(sys.argv[2])
c = int(sys.argv[3])
# Finding the roots with quadratic formula
disc = b**2 - 4*a*c
if disc < 0 :
    sys.exit("The solution is not real.")
root1 = round((-b + math.sqrt(disc)) / (2*a),2)
root2 = round((-b - math.sqrt(disc)) / (2*a),2)
if disc > 0:
    print("There are two solutions.\nSolutions:", root1, root2)
else:
    print("There is one solution.\nSolution:", root1)