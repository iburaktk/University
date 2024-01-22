`timescale 1ns / 1ps


module full_adder(A,B,Cin,S,Cout);

    input A,B,Cin;
    output S,Cout;
    
    
    assign #10 Cout = (A & B) | (A & Cin) | (B & Cin) ;
    assign #10 S = A ^ B ^ Cin; 
    
endmodule