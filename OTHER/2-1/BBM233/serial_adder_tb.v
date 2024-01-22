`timescale 1ns / 1ps
`include "serial_adder.v"

module serial_adder_tb;
    
    reg clock,control,Input;
    wire Result;
    
    serial_adder UUT(clock,control,Input,Result);
    
    initial begin
        control = 0;
        clock = 1;
            forever #5 clock = ~clock;
    end
    //testbench not working :-(
    always begin
        #10;
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b1; #10;        
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b1; #10;
        control = 1; Input = 1'b0;
        control = 1; #80;
        $finish;     
    end
    
endmodule