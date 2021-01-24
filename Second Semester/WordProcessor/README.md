In this assignment, i will try to learn dynamic memory allocation with malloc, realloc, free functions in C.  
My target is zero memory leak. I used valgrind to detect leaks and i reached my target (zero memory leaks).  
This assignment is a word processor (like Microsoft Office Word). This word processor has commands like  
Grow -> uses realloc to grow an array's space.  
Shrink -> uses realloc to shrink an array's space.  
Create -> creates a new file according to parameters with malloc.  
Append -> appends new words to existing file with realloc.  
Print -> prints file.  
Replace -> replaces all words to new word.  
Remove -> removes all words which equals to given word.  
Delete -> deletes file with free.
