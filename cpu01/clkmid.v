module clkmid(
input iclk,
output oclk
);

reg [31:0] clkcount;
reg mclk;

initial
clkcount <= 0 ;

always @(posedge iclk)
begin
	if(clkcount==25000000) begin
		mclk<=~mclk;
		clkcount <= 0;
	end else 
		clkcount <= clkcount + 1;
end

assign oclk = mclk;

endmodule
