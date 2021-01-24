# The program that takes 3 words and seperates its letters.
# Makes a list of this letters and prints the list.
# I find longest word using by these letters. Program gives points to me.
def append_it(Wordx):
	for element in Wordx:
		if element not in List:
			List.append(element)
def non_word_detect(Wordx, non_word,letters):
	for letter in Wordx:
		if letter not in letters:
			if non_word == "" :
				non_word = Wordx
			else:
				non_word = non_word + " and " + Wordx
			break
	return non_word
import sys # for taking argumemts and exit.
import random # for random letters
if len(sys.argv) != 4 : # 3 arguments check
    sys.exit("Please run with 3 arguments!")
Word1 = sys.argv[1] # take in the arguments
Word2 = sys.argv[2]
Word3 = sys.argv[3]
List = [] # making letters list
append_it(Word1)
append_it(Word2)
append_it(Word3)
letters = "abcdefghijklmnopqrstuvwxyz"
non_word = "" # non word check
non_word = non_word_detect(Word1, non_word, letters)
non_word = non_word_detect(Word2, non_word, letters)
non_word = non_word_detect(Word3, non_word, letters)
if non_word != "":
	print("Argument", non_word, "is not a word.", end=" ")
	sys.exit("All arguments should be word.")
Len1 = len(Word1) # same length check
Len2 = len(Word2)
Len3 = len(Word3)
if Len1 == Len2 or Len2 == Len3 or Len1 == Len3 :
	sys.exit("Arguments should be a different length.")
i = 0
while i < 9 : # add random letters to letters list
    List.append(random.choice(List))
    i = i + 1
List.sort() # sort the letters a to z
print(str(List)[1:-1]) # question
print("Find longest word using letters given below.")
Answer = input("Guess the longest word: ")
for letter in Answer:
	if letter not in List:
		sys.exit("This answer is invalid.\nYou Lost !")
if Answer != Word1 and Answer != Word2 and Answer != Word3 : # wrong answer!
    print("The word you guessed is not in the list.")
    sys.exit("You Lost !")
Len = [Len1, Len2, Len3] # Calculating the point
Len.sort(reverse=True)
point = 0
if len(Answer) == Len[0] :
    point = point + 50
elif len(Answer) == Len[1] :
    point = point + 30
elif len(Answer) == Len[2] :
    point = point + 10
else :
    print("Unexpected error!")
print("You found a word from list.\nYou won", point, "points!") # result
