module top(
input wire clk,
input wire rst,
input wire pllrst,
output wire [3:0] oled,
output [6:0] oseg,
output [3:0] odig
);

wire [255:0] odata;
core cpu(
	.clk(clk),
	.rst(rst),
	.gpio(odata)
);

SEG7_LUT_4	u1(
.iCLK(ledcount[15]),
.iDIG(odata[15:0]),
.oSEG(oseg),
.oDIG(odig)	);

reg [25:0] ledcount;
always @(posedge clk)
ledcount<=ledcount+1;

assign oled[0]=~((ledcount<50000) || (ledcount>12000000) && (ledcount<12050000)) ;
assign oled[3:1]=3'b111;

endmodule
