`timescale 1s / 1s
`include "controller.v"

module controller_tb;
    
    reg clock,greenTraffic,grayTraffic;
    wire [2:0] greenRoadLights,grayRoadLights;
    
    initial begin
        clock = 1;
        forever #1000 clock = ~clock;
    end
    //#1000 = 1000 ms = 1 s
    controller UUT(greenTraffic,grayTraffic,clock,greenRoadLights,grayRoadLights);
    
    initial begin
        greenTraffic = 0; grayTraffic = 0; #40000; //no congestion
        greenTraffic = 0; grayTraffic = 1; #44000; //gray road congestion
        greenTraffic = 1; #64000;                  //both roads congestion
        greenTraffic = 0; grayTraffic = 0; #60000; //no congestion
        greenTraffic = 1; grayTraffic = 0; #80000; //green road congestion
        $finish;
    end
    
endmodule
