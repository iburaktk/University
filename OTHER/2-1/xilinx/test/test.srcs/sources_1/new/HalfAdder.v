`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: Hacettepe University
// Engineer: Ýbrahim Burak Tanrýkulu
// 
// Create Date: 29.11.2019 20:52:37
// Design Name: Half Adder
// Module Name: HalfAdder
// 
//////////////////////////////////////////////////////////////////////////////////


module HalfAdder(Sum, Carry, A, B);
    
    input A, B;
    output Sum, Carry;

    assign Sum = A ^ B;
    assign Carry = A & B;

endmodule
