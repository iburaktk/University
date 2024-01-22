`timescale 1ns / 1ps
`include "shift_register.v"
`include "full_adder.v"
`include "D_ff.v"

module serial_adder(clock,control,Input,out);
    input clock,control,Input;
    
    output out;
    wire Cin;
                
    full_adder full_adder_1(out,y,Cin,Sum,Cout);
    shift_register addend(Input,clock,control,y);
    D_ff D_ff_carry(control,clock,Cout,Cin);
    shift_register augend(Sum,clock,control,out);
    
        
endmodule