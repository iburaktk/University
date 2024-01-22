`timescale 1ns / 1ps


module counter_testbench;
    
    reg [24:0] testbenchData;
    integer shift_amount;
    reg M , clock;
    wire [2:0] theOutput;
    
    counter UUT(M,clock,theOutput);
    
    initial begin
        testbenchData = 25'b1111111111000000000000111;
        shift_amount = 0;
    end
    
    initial begin
        clock = 0;
        forever begin
            #20;
            clock = ~clock;
        end    
    end
    always @(posedge clock) begin
        M = testbenchData >> shift_amount;
        shift_amount = shift_amount + 1;
    end
    
endmodule
