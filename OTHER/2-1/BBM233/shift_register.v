`timescale 1ns / 1ps
`include "D_ff.v"

module shift_register(in,clock,control,out);
    
    input clock,control,in;
    wire bit1,bit2,bit3;
    output out;
    
    D_ff D_ff_4(control,clock,bit3,out);
    D_ff D_ff_3(control,clock,bit2,bit3);
    D_ff D_ff_2(control,clock,bit1,bit2);
    D_ff D_ff_1(control,clock,in,bit1);
    
    
endmodule
