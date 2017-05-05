make
java lab3 lab3_fib.asm lab3_fib.script > lab3_fib.pls
diff -w -B lab3_fib.pls lab3_fib.expected
java lab3 lab3_test3.asm lab3_test3.script > lab3_test3.pls
diff -w -B lab3_test3.pls lab3_test3.expected
java lab3 sum_10.asm sum_10.script > sum_10.pls
diff -w -B sum_10.pls sum_10.expected
