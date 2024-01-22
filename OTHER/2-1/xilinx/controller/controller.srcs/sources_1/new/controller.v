`timescale 1ms / 1ms

module controller(
    input greenTraffic,
    input grayTraffic,
    input clock,
    output reg [2:0] greenRoadLights,
    output reg [2:0] grayRoadLights
    );
    
    reg [2:0] nextGreenRoadLights;
    reg [2:0] nextGrayRoadLights;
    
    parameter s0 = 3'b100, s1 = 3'b110, s2 = 3'b011, s3 = 3'b001;
    reg greenCongestionTimeout,grayCongestionTimeout;
    integer count;
    
    initial begin
        greenRoadLights = s0;
        grayRoadLights = s3;
        nextGreenRoadLights = s0;
        nextGrayRoadLights = s3;
        count = 0;
        greenCongestionTimeout = 0;
        grayCongestionTimeout = 0;
    end
    
    always @(posedge clock,negedge clock) begin
        greenRoadLights = nextGreenRoadLights;
        grayRoadLights = nextGrayRoadLights;
    end
    
    always @(posedge clock,negedge clock) begin
        if (greenTraffic == 1) begin
            count = count + 1;
            case(greenRoadLights)
                s0: if (count == 40) begin
                        nextGreenRoadLights = s1;
                        nextGrayRoadLights = s2;
                        count = 0;
                        greenCongestionTimeout = 1;
                    end
                s1: if (count == 3) begin
                        nextGreenRoadLights = s3;
                        nextGrayRoadLights = s0;
                        count = 0;
                    end
                s2: if (count == 3) begin
                        nextGreenRoadLights = s0;
                        nextGrayRoadLights = s3;
                        count = 0;
                    end
                s3: if (greenCongestionTimeout == 1) begin
                        if (count == 10) begin
                            nextGreenRoadLights = s2;
                            nextGrayRoadLights = s1;
                            count = 0;
                            greenCongestionTimeout = 0;
                        end
                    end
                    else begin
                        nextGreenRoadLights = s2;
                        nextGrayRoadLights = s1;
                        count = 0;
                        greenCongestionTimeout = 0;
                    end
            endcase
        end
        else if (grayTraffic == 1) begin
            count = count + 1;
            case(grayRoadLights)
                s0: if (count == 10) begin
                        nextGreenRoadLights = s2;
                        nextGrayRoadLights = s1;
                        count = 0;
                        grayCongestionTimeout = 1;
                    end
                s1: if (count == 3) begin
                        nextGreenRoadLights = s0;
                        nextGrayRoadLights = s3;
                        count = 0;
                    end
                s2: if (count == 3) begin
                        nextGreenRoadLights = s3;
                        nextGrayRoadLights = s0;
                        count = 0;
                    end
                s3: if (grayCongestionTimeout == 1) begin
                        if (count == 20) begin
                            nextGreenRoadLights = s1;
                            nextGrayRoadLights = s2;
                            count = 0;
                            grayCongestionTimeout = 0;
                        end
                    end
                    else begin
                        nextGreenRoadLights = s1;
                        nextGrayRoadLights = s2;
                        count = 0;
                        grayCongestionTimeout = 0;
                    end
            endcase
        end
        else begin
            count = count + 1;
            case(grayRoadLights)
                s0: if (count == 10) begin
                        nextGreenRoadLights = s2; 
                        nextGrayRoadLights = s1;
                        count = 0;
                    end
                s1: if (count == 3) begin
                        nextGreenRoadLights = s0;
                        nextGrayRoadLights = s3;
                        count = 0;
                    end
                s2: if (count == 3) begin
                        nextGreenRoadLights = s3;
                        nextGrayRoadLights = s0;
                        count = 0;
                    end
                s3: if (count == 20) begin
                        nextGreenRoadLights = s1;
                        nextGrayRoadLights = s2;
                        count = 0;
                    end
            endcase
        end
    end
    
endmodule
