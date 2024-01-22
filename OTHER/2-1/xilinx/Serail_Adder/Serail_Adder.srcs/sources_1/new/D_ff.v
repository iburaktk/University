`timescale 1ns / 1ps


module D_ff(control,clock,D,Q);

    input D,control,clock;
    output reg Q;
    
    always @(posedge clock) begin
        if (control) begin
            Q <= D;
        end
        else begin
            Q = 0;
        end
    end
    
endmodule