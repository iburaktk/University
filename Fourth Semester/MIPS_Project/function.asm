#int main(){
#	int a, b, result;
#	if(a == b)
#		result = a*b;
#	else
#		result = assess(a, b);
#	return result;
#}
#int assess(int a, int b){
#	if(b<a)
#		return upgrade(a, b);
#	else
#		return demote(a, b);
#}
#int upgrade(int a, int b)
#	{return 4*(a+b);}
#int demote(int a, int b)
#	{return 4*(b-a);}

# $s0 = a , $s1 = b , $s2 = result
# test 1:
#addi $s0, $0, 8
#addi $s1, $0, 8
# test 2:
addi $s0, $0, 3
addi $s1, $0, 5
# test 3:
#addi $s0, $0, 5
#addi $s1, $0, 3

if:		bne $s0, $s1, else  # if a = b
		mul $s2, $s0, $s1
		j done
else:	add $a0, $s0, $0    # else return assess
		add $a1, $s1, $0
		jal assess
		add $s2, $v0, $0
		j done
assess:	
		assessIf:	ble $a0, $a1, assessElse    # if b < a
					addi $sp, $sp, -4
					sw $ra, 0($sp)  # store return address
					jal upgrade
					lw $ra, 0($sp)  # load return address
					addi $sp, $sp, 4
					jr $ra
		assessElse:	addi $sp, $sp, -4           # b >= a
					sw $ra, 0($sp)  # store return address
					jal demote
					lw $ra, 0($sp)  # load return address
					addi $sp, $sp, 4
					jr $ra
upgrade:    # upgrade function
		add $t0, $a0, $a1
		sll $t0, $t0, 2
		add $v0, $t0, $0
		jr $ra
demote:     # demote function
		sub $t0, $a1, $a0
		sll $t0, $t0, 2
		add $v0, $t0, $0
		jr $ra
done: