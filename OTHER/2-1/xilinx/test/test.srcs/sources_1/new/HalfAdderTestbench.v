`timescale 1ns / 1ps


module HalfAdderTestbench;
    
    reg A, B;
    wire Sum, Carry;
    
    HalfAdder UUT(Sum,Carry,A,B);
    
    initial
        begin
            #10 A=0; B=0;
            #10 A=1; B=0;
            #10 A=1; B=1;
            #10 $finish;
        end
        
endmodule
