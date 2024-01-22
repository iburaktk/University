print("""
<================-===X=>
|                      |
|    Welcome to my     |
|                      |
|       Program!       |
|                      |
|    İbrahim Burak     |
|      Tanrıkulu       |
|                      |
<======================>""")
for i in range(5,10): # exercise 1
	print(i)
numbers = "0123456789" # my exercise
for i in numbers:
	print(i)
	if i == "0" :
		print("it is zero.")
	else:
		print("it is pozitive.")
for i in numbers: # exercise 2
	if int(i) > 3 :
		print(i)
first_text = "İbrahim Burak Tanrıkulu" # exercise 3
second_text = "Burak Tanrıkulu"
for letter in first_text:
	if letter not in second_text:
		print("-",letter, end=" -\n")
for number in range(2,30): # exercise 4
	if int(number) % 6 == 0 :
		print(number)
# exercise 5
#try:
#	x = int(input("Input an integer: "))
#except ValueError as e:
#	print("Hata. Lütfen sayı giriniz!")
x = int(input("Input an integer: "))
answer = None
cuberoot_found = False
for i in range(0, abs(x) + 1):
	if i**3 == abs(x):
		answer = i
		cuberoot_found = True
if not cuberoot_found :
	print(x, "is not perfect cube.")
else:
	if x < 0 :
		answer = -answer
	print("Cuberoot of ", x, "is ", answer)
sentence = "Yürüdüğümüz yol bitmiş , bir başka sokağa açılmıştı ." 
# exercise 6
for word in first_text.split() :
	print(word)
for word in sentence.split() :
	print(word)
for word in sentence.split(",") :
	print(word)
for word in sentence.split("ş") :
	print(word)
numbers = "12,15,47,86,98" # exercise 7
list = numbers.split(",")
print(list)
for number in numbers.split(","):
	if int(number) % 2 == 0 :
		print(number, "is even.")
	if int(number) % 2 == 1 :
		print(number, "is odd.")
colors = "red,green,blue"
for color in colors.split(','):
	print(color)
a, b, c = colors.split(",")
print(a, b, c, sep=("\n"))
print("a is", a)
first_name , second_name , last_name = first_text.split()
print("Your first name is:  ", first_name)
print("Your second name is:  ", second_name)
print("Your last name is:  ", last_name)
# exercise 8
myName = input("Please enter your name: ")
myName2 = myName.split()
if myName2[0] != "My" :
	print('Please write like this: "My name is Burak."')
else:
	try:
		print("Hello ", myName2[3], "!")
	except IndexError as e:
		print('Please write like this: "My name is Burak."')













