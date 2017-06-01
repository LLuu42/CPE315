# figure.asm
# Author = Lara Luu
#
# Utilizes Bresenham line and circle drawing algorithms to plot coordinates and draw a stick figure
# Input = Nothing
# Output = Coordinates into a .csv file

# Line(x0, y0, x1, y1)            Circle(xc, yc, r)
# $a0 = x0                       $a0 = xc
# $a1 = y0                       $a1 = yc
# $a2 = x1                       $a2 = r
# $a3 = y1                       
###                              ###
# $t0 = st                       $t0 = x
# $t1 = deltax                   $t1 = y
# $t2 = deltay                   $t2 = g
# $t3 = error                    $t3 = diagonalInc
# $t4 = y                        $t4 = rightInc
# $t5 = ystep                    
# $t6 = spIndex


         ### Call Circle and Line Functions ###
main:          
   #head
   addi   $a0, $0, 30
   addi   $a1, $0, 100
   addi   $a2, $0, 20
   jal    circle

   #body
   addi   $a0, $0, 30
   addi   $a1, $0, 80
   addi   $a2, $0, 30
   addi    $a3, $0, 30
   jal    line
   
   #left leg
   addi   $a0, $0, 20
   addi   $a1, $0, 1
   addi   $a2, $0, 30
   addi    $a3, $0, 30
   jal    line
   
   #right leg
   addi   $a0, $0, 40
   addi   $a1, $0, 1
   addi   $a2, $0, 30
   addi    $a3, $0, 30
   jal    line
   
   #left arm
   addi   $a0, $0, 15
   addi   $a1, $0, 60
   addi   $a2, $0, 30
   addi    $a3, $0, 50
   jal    line
   
   #right arm
   addi   $a0, $0, 30
   addi   $a1, $0, 50
   addi   $a2, $0, 45
   addi    $a3, $0, 60
   jal    line
   
   #left eye
   addi   $a0, $0, 24
   addi   $a1, $0, 105
   addi   $a2, $0, 3
   jal    circle
   
   #right eye
   addi   $a0, $0, 36
   addi   $a1, $0, 105
   addi   $a2, $0, 3
   jal    circle
   
   #mouth center
   addi   $a0, $0, 25
   addi   $a1, $0, 90
   addi   $a2, $0, 35
   addi    $a3, $0, 90
   jal    line
   
   #mouth left
   addi   $a0, $0, 25
   addi   $a1, $0, 90
   addi   $a2, $0, 20
   addi    $a3, $0, 95
   jal    line
   
   #mouth right
   addi   $a0, $0, 35
   addi   $a1, $0, 90
   addi   $a2, $0, 40
   addi    $a3, $0, 95
   jal    line
   
   j      end

  
line: #a0 = x0, a1 = y0, a2 = x1, a3 = y1

   slt    $t0, $a2, $a0         # Check if x1 < x0
   beq    $t0, $0, skipabsdx    # Skip abs

   sub    $t0, $a0, $a2         # dx = x0 - x1
   j      L1

   skipabsdx:
      sub    $t0, $a2, $a0      # dx = x1 - x0

   L1:

      slt    $t1, $a3, $a1      # Check if y1 < y0
      beq    $t1, $0, skipabsdy # Skip abs

      sub    $t1, $a1, $a3      # dy = y0 - y1
      j       L2

   skipabsdy:
      sub    $t1, $a3, $a1      # dy = y1 - y0

   L2:
      addi   $t3, $0, 1         # st = 1
      slt    $t2, $t0, $t1      # Checks if dx < dy
      bne    $t2, $0, skipst

      add    $t3, $0, $0        # st = 0

      skipst:

      beq    $t3, $0, skip1

      addi    $t4, $a0, 0
      addi    $a0, $a1, 0
      addi    $a1, $t4, 0

      addi    $t4, $a2, 0
      addi    $a2, $a3, 0
      addi    $a3, $t4, 0

      skip1:

      slt    $t4, $a2, $a0
      beq    $t4, $0, skip2

      addi    $t4, $a0, 0
      addi    $a0, $a2, 0
      addi    $a2, $t4, 0

      addi    $t4, $a1, 0
      addi    $a1, $a3, 0
      addi    $a3, $t4, 0

      skip2:

      sub    $t0, $a2, $a0       # x1 - x0

      slt    $t1, $a3, $a1       # y1 < y0?
      beq    $t1, $0, skipabsdy2 # skip absolute again

      sub    $t1, $a1, $a3       # dy = y0 - y1
      j       L3

      skipabsdy2:
      sub    $t1, $a3, $a1       # dy = y1 - y0

      L3:

      add    $t2, $0, $0         # error = 0
      add    $t4, $a1, $0        # y = y0

      addi   $t5, $0, 1          # ystep = 1
      slt    $t6, $a1, $a3       # Check if y0 < y1
      bne    $t6, $0, skip3

      addi   $t5, $0, -1         # ystep = -1

      skip3:

      add    $t6, $a0, $0        # i = x0
      addi   $t7, $a2, 1

      forLoop:
         beq   $t6, $t7, endline

         beq   $t3, $0, elsest

         sw    $t4, 0($sp)
         sw    $t6, 1($sp)
         addi  $sp, $sp, 2

         j     atLeastTheYearIsAlmostOver

         elsest:
            sw    $t4, 1($sp)
            sw    $t6, 0($sp)
            addi  $sp, $sp, 2

         atLeastTheYearIsAlmostOver:

         add   $t2, $t2, $t1      # error = error + dy
         sll   $t8, $t2,  1       # error * 2
         slt   $t9, $t8, $t0      # Checks if error * 2 < dx

         bne   $t9, $0, skip4

         add   $t4, $t4, $t5
         sub   $t2, $t2, $t0

         skip4:

         addi   $t6, $t6, 1       #i++
         j      forLoop

         endline:
            jr   $ra

circle:
   add    $t0, $0, $0           # x = 0
   add    $t1, $a2, $0          # y = r
   addi   $t3, $0, 3            # store 3 in t0 (because subi does not exist)
   sll    $t2, $a2, 1           # r * 2
   sub    $t2, $t3, $t2         # g = 3 - 2 * r

   addi   $t4, $0, 10           # store 10 in t4
   sll    $t3, $a2, 2           # r * 4
   sub    $t3, $t4, $t3         # diagonalInc = 10 - 4 * r
   
   addi   $t4, $0, 6            # rightInc = 0
   
   addi   $t5, $t1, 1           # y + 1
   
circleWhile:
   beq    $t0, $t5, endcircle
     
   add    $t6, $a0, $t0   # plot(xc + x, yc + y)
   add    $t7, $a1, $t1
      
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
     
   add    $t6, $a0, $t0    # plot(xc + x, yc - y)
   sub    $t7, $a1, $t1
    
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
    
      
   sub    $t6, $a0, $t0    # plot(xc - x, yc + y)
   add    $t7, $a1, $t1
    
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
     
      
   sub    $t6, $a0, $t0    # plot(xc - x, yc - y)
   sub    $t7, $a1, $t1
      
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
      
   add    $t6, $a0, $t1    # plot(xc + y, yc + x)
   add    $t7, $a1, $t0
      
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
      
      
   add    $t6, $a0, $t1    # plot(xc + y, yc - x)
   sub    $t7, $a1, $t0
      
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
      

   sub    $t6, $a0, $t1    # plot(xc - y, yc + x)
   add    $t7, $a1, $t0
      
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2
      
      
   sub    $t6, $a0, $t1    # plot(xc - y, yc - x)
   sub    $t7, $a1, $t0
     
   sw     $t6, 0($sp)
   sw     $t7, 1($sp)
   addi   $sp, $sp, 2


   slt    $t6, $t2, $0
   bne    $t6, $0, gRight
      
   add    $t2, $t2, $t3
   addi   $t3, $t3, 8
   addi    $t5, $t1, 1
   addi   $t1, $t1, -1
      
   j      skipgRight
      
   gRight:
      add    $t2, $t2, $t4
      addi   $t3, $t3, 4
      
   skipgRight:
      
   addi   $t4, $t4, 4        # rightInc += 4
   addi   $t0, $t0, 1        # x += 1
     
   j      circleWhile

      
   endcircle:
      jr     $ra

plot:
   jr $ra

end:
   add $0, $0, $0