`timescale 1ns / 1ps

module counter(M,clock,theOutput);

    input M;
    input clock;
    output [2:0] theOutput;
    
    parameter s0 = 3'b000, s1 = 3'b001, s2 = 3'b010, s3 = 3'b011, s4 = 3'b100, s5 = 3'b101, s6 = 3'b110, s7 = 3'b111;
    reg [2:0] present_state = 3'b000;
    reg [2:0] next_state;
    
    always@(posedge clock)
        present_state <= next_state;
    
    always@(present_state, M)
        begin
        case(present_state)
            s0: if(M == 0) next_state = s7;
            else if(M == 1) next_state = s1;
            s1: if(M == 0) next_state= s0;
            else if(M == 1) next_state= s2;
            s2: if(M == 0) next_state= s1;
            else if(M == 1) next_state= s3;
            s3: if(M == 0) next_state= s2;
            else if(M == 1) next_state= s4;
            s4: if(M == 0) next_state= s3;
            else if(M == 1) next_state= s5;
            s5: if(M == 0) next_state= s4;
            else if(M == 1) next_state= s6;
            s6: if(M == 0) next_state= s5;
            else if(M == 1) next_state= s7;
            s7: if(M == 0) next_state= s6;
            else if(M == 1) next_state= s0;
        endcase
    end
    
    assign theOutput[2] = next_state[2];
    assign theOutput[1] = next_state[1];
    assign theOutput[0] = next_state[0];
    
endmodule            
