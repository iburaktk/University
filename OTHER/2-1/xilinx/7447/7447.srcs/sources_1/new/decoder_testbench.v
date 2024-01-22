`timescale 1ns / 1ps

module decoder_testbench;

reg A,B,C,D;
wire a,b,c,d,e,f,g;
    
decoder UUT(A,B,C,D,a,b,c,d,e,f,g);

initial
    begin
        #10 A=0; B=0; C=0; D=0;
        #10 A=0; B=0; C=0; D=1;
        #10 A=0; B=0; C=1; D=0;
        #10 A=0; B=0; C=1; D=1;
        #10 A=0; B=1; C=0; D=0;
        #10 A=0; B=1; C=0; D=1;
        #10 A=0; B=1; C=1; D=0;
        #10 A=0; B=1; C=1; D=1;
        #10 A=1; B=0; C=0; D=0;
        #10 A=1; B=0; C=0; D=1;
        #10 $finish;
    end

endmodule
