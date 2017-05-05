mips> c
	Simulator reset
mips> d

pc = 0
$0 = 0 		$v0 = 0  	$v1 = 0 	$a0 = 0
$a1 = 0 	$a2 = 0 	$a3 = 0 	$t0 = 0
$t1 = 0 	$t2 = 0 	$t3 = 0 	$t4 = 0
$t5 = 0 	$t6 = 0 	$t7 = 0 	$s0 = 0
$s1 = 0 	$s2 = 0 	$s3 = 0 	$s4 = 0
$s5 = 0 	$s6 = 0 	$s7 = 0 	$t8 = 0
$t9 = 0 	$sp = 0 	$ra = 0
mips> s
	1 instruction(s) executed
mips> d

pc = 1
$0 = 0 		$v0 = 0  	$v1 = 0 	$a0 = 10
$a1 = 0 	$a2 = 0 	$a3 = 0 	$t0 = 0
$t1 = 0 	$t2 = 0 	$t3 = 0 	$t4 = 0
$t5 = 0 	$t6 = 0 	$t7 = 0 	$s0 = 0
$s1 = 0 	$s2 = 0 	$s3 = 0 	$s4 = 0
$s5 = 0 	$s6 = 0 	$s7 = 0 	$t8 = 0
$t9 = 0 	$sp = 0 	$ra = 0
mips> s 5
	5 instruction(s) executed
mips> d

pc = 2
$0 = 0 		$v0 = 0  	$v1 = 0 	$a0 = 9
$a1 = 0 	$a2 = 0 	$a3 = 0 	$t0 = 0
$t1 = 0 	$t2 = 0 	$t3 = 0 	$t4 = 0
$t5 = 0 	$t6 = 0 	$t7 = 0 	$s0 = 19
$s1 = 0 	$s2 = 0 	$s3 = 0 	$s4 = 0
$s5 = 0 	$s6 = 0 	$s7 = 0 	$t8 = 0
$t9 = 0 	$sp = 0 	$ra = 0
mips> r
mips> d

pc = 5
$0 = 0 		$v0 = 0  	$v1 = 0 	$a0 = 0
$a1 = 0 	$a2 = 0 	$a3 = 0 	$t0 = 0
$t1 = 0 	$t2 = 0 	$t3 = 0 	$t4 = 0
$t5 = 0 	$t6 = 0 	$t7 = 0 	$s0 = 55
$s1 = 0 	$s2 = 0 	$s3 = 0 	$s4 = 0
$s5 = 0 	$s6 = 0 	$s7 = 0 	$t8 = 0
$t9 = 0 	$sp = 0 	$ra = 0
mips> h

h = show help
d = dump register state
s = single step through the program (i.e. execute 1 instruction and stop)
s num = step through num instructions of the program
r = run until the program ends
m num1 num2 = display data memory from location num1 to num2
c = clear all registers, memory, and the program counter to 0
q = exit the program

mips> q
