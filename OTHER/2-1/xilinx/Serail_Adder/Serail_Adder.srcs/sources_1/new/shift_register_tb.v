`timescale 1ns / 1ps

module shift_register_tb;
    reg clock,control,in;
    wire out;
    
    shift_register UUT(in,clock,control,out);
    
    initial begin
        control = 1;
        clock = 1;
            forever #5 clock = ~clock;
    end
    
    always begin
        control = 1; in = 1'b0; #10;
        control = 1; in = 1'b0; #10;        
        control = 1; in = 1'b0; #10;
        control = 1; in = 1'b1; #10;
        control = 1; in = 1'b1; #10;
        control = 1; in = 1'b1; #10;
        control = 1; in = 1'b1; #10;
        control = 1; in = 1'b0; #10;
        control = 1; #100;
        $finish;     
    end
    
    
endmodule
