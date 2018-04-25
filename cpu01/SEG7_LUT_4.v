module SEG7_LUT_4 (	oSEG,iDIG,oDIG,iCLK );
input	[15:0]	iDIG;
input iCLK;
output	[6:0]	oSEG;
output [3:0] oDIG;
reg [1:0] count;
wire [6:0] oSEG0;
wire [6:0] oSEG1;
wire [6:0] oSEG2;
wire [6:0] oSEG3;
reg [6:0] mseg;
reg [3:0] modig;

always @(posedge iCLK) begin
	count<=count+1;
	case (count)
	0:begin mseg<=oSEG0; modig<=4'b0001; end
	1:begin mseg<=oSEG1; modig<=4'b0010; end
	2:begin mseg<=oSEG2; modig<=4'b0100; end
	3:begin mseg<=oSEG3; modig<=4'b1000; end
	endcase
end

SEG7_LUT	u0	(	oSEG0,iDIG[3:0]		);
SEG7_LUT	u1	(	oSEG1,iDIG[7:4]		);
SEG7_LUT	u2	(	oSEG2,iDIG[11:8]	);
SEG7_LUT	u3	(	oSEG3,iDIG[15:12]	);

assign oSEG = mseg;
assign oDIG = ~modig;

endmodule