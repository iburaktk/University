`timescale 1ns / 1ps

module decoder (A,B,C,D,a,b,c,d,e,f,g);
    input A,B,C,D;
    output a,b,c,d,e,f,g;
    
    assign a= (B & ~D) | (~A & ~B & ~C & D);
    assign b= (B & ~C & D) | (B & C & ~D);
    assign c= ~B & C & ~D;
    assign d= (B & C & D) | (B & ~C & ~D) | (~B & ~C & D) | (A & D);
    assign e= (B & ~C & ~D) | (~C & D) | (B & C & D);
    assign f= (~A & ~B & D) | (C & D) | (~B & C & ~D);
    assign g= (~A & ~B & ~C) | (C & D);
   
endmodule
