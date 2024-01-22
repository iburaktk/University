.data
array:	.word  1, 2, 3, 4, 5, 5, 2, 3, ,4, 5
Key:	.word 5
array_size:	.word 10
.text
main:

	la $a0, array		#load the address of first array 						#element to a0
	lw $s0, Key
	lw $t0, array_size
	addi $s1, $0, 0		#s1 holds number of Key ocurrances in 						#array
	addi $t1, $0, 0		#i=0
loop:	beq $t0, $t1, done	#if i=array_size, done
	lw $t2, 0($a0)		#load the current array element to 						#register t2
	bne $s0, $t2, not_eq	#if Key != array[i], do not increment
	addi $s1, $s1, 1
not_eq:	addi $t1, $t1, 1		#i=i+1
	addi $a0, $a0, 4		#point to the next array element
	j loop
done: 	 li $v0, 10
    syscall
	