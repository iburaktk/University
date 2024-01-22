print("Hello world!")

name = input("What is your name? ")
print("Hello " + name + "!\n")

number = int(input("Please enter a number: "))
print ("The square of the number: " , number ** 2)

question = input("Please enter a fruit name: ")
if question == "apple":
	print("Yes, apple is a fruit.")
elif question == "banana":
	print("Yes, banana is a fruit.")
elif question == "strawberry":
	print("Yes, strawberry is a fruit.")
else:
	print("Your input isn't a fruit")

def function1():
	choice = input("What do you want?\n1-Convert Celcius to Fahrenheit\n2-Fahrenheit to Celcius\n3-Body Mass Index calculation")
	if choice == "1":
		cel = int(input("Enter Celcius: "))
		fahr = (cel-32)*(5/9)
		print(cel, "Celcius is ", fahr, "Fahrenheit.")
	elif choice == "2":
		fahr = int(input("Enter Fahrenheit: "))
		cel = fahr * (9/5) + 32
		print(fahr, "Fahrenheit is ", cel, "Celcius.")
	elif choice == "3":
		kilo = int(input("Enter your mass(kg): "))
		height = float(input("Enter your height(m): "))
		bmi = kilo / (height ** 2)
		print("Your Body Mass Index is ", bmi, ".")
		if bmi <= 18.5 :
			print("Too skinny.")
		elif 18.5 < bmi < 25 :
			print("You are normal.")
		elif 25 <= bmi :
			print("Too fat.")
		else:
			print("Error.")
	else:
		print("Error.")
function1()
