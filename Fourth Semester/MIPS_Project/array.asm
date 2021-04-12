#int Array[5];
#for (int i = 0; i < 5; i++){
#	if(Array[i]%2==0)
#		Array[i] = Array[i]/2;
#	else
#		Array[i] = (Array[i]*3)+1;
#}

.data
A: .word 1, 2, 3, 4, 5
#A: .word 2, 4, 10, 0, 50
#A: .word 1, 3, 5, 9, 13
.text
main: la $t1, A

addi $s0, $0, 0	# i = 0
addi $t0, $0, 5
# $s0 = i , $t0 = 5 , $t1 = array base adress

for:	beq $s0, $t0, done
		sll $t2, $s0, 2		# byte offset
		add $t2, $t2, $t1	# $t2 = address of array[i]
		lw $t3, 0($t2)		# $t3 = value of array[i]
		rem $t4, $t3, 2		# $t4 = array[i] % 2
		if:		bne $t4, 0, else
				sra $t3, $t3, 1
				j endIf
		else:	addi $t4, $t4, -1	# we don't need remainder now, so reset it. $t4 = 0
				add $t4, $t4, $t3	# $t4 = array[i]
				sll $t3, $t3, 1		# array[i] * 3 = (array[i] * 2) + array[i]
				add $t3, $t3, $t4	# $t3 = array[i] * 3
				addi $t3, $t3, 1	# $t3 = (array[i]*3)+1
		endIf:	
		sw $t3, 0($t2)
		addi $s0, $s0, 1
		j for
done: