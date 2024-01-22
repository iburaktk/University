`timescale 1ns / 1ps


module full_adder_tb;
    
    reg A,B,Cin;
    wire S,Cout;
    
    full_adder UUT(A,B,Cin,Cout,S);
    
    initial begin
        A = 0; B = 0; #10;
        A = 1; B = 1; #10;
        $finish;
    end
    
endmodule
