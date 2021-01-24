import sys
# Problem3: Set Operations
if len(sys.argv) != 3 : # argument number check
    sys.exit("Please run with 2 argument!")
allowed_chars = "0123456789,-"  # argument characters check
def char_check(Set):
    for char in Set:
        if char not in allowed_chars:
            sys.exit("Please check the arguments.")
A = sys.argv[1]
char_check(A)
A = A.split(",") # Set A created
B = sys.argv[2]
char_check(B)
B = B.split(",") # Set B created
Union = [] # Union
for element in A :
    Union.append(element)
for element in B :
    if element not in Union :
        Union.append(element)
Intersection = [] # Intersection
for element in A :
    if element in B :
        Intersection.append(element)
Difference = [] # Difference
for element in A :
    if element not in B :
        Difference.append(element)
# Print it.
print("Set A :", A)
print("Set B :", B)
print("Intersection of A and B: ", Intersection)
print("Union of A and B: ", Union)
print("Difference of A and B: ", Difference)