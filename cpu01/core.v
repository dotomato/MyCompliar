module core(
	input wire clk,
	input wire rst,
	output wire [255:0] gpio
);

parameter addrwide=8;
parameter datawide=8;
parameter stop_pc=250;
//*******************rom side*****************
rom0 si0(
	.address(si0_addr),
	.clock(rom_ram_clk),
	.q(si0_at_pc)
);
rom1 si1(
	.address(si1_addr),
	.clock(rom_ram_clk),
	.q(si1_at_pc)
);
//****************switch pc -> si address
wire [addrwide-1:0] si0_addr;
wire [addrwide-1:0] si1_addr;
assign si0_addr=pc;
assign si1_addr=pc;

//*******************si0_at_pc side*****************
wire [datawide-1:0] si0_at_pc;

//*******************si1_at_pc side*****************
wire [datawide-1:0] si1_at_pc;

//*******************ram side*****************
ram1 sta(
	.address(sta_addr),
	.clock(rom_ram_clk),
	.data(sta_data),
	.wren(sta_wren),
	.q(sta_q)
);
reg [addrwide-1:0] sta_addr;
wire [datawide-1:0] sta_q;
reg [datawide-1:0] sta_data;
reg sta_wren;
wire rom_ram_clk;

//********************rom_ram_clk side **********************
assign rom_ram_clk=~clk;

//*******************sp,fp,pc side*****************
reg [datawide-1:0] sp;
reg [datawide-1:0] fp;
reg [datawide-1:0] pc;



//*******************reg_at_si1 side*****************
reg [datawide-1:0] reg_at_si1;
always @(si1_at_pc,sp,fp,pc)
begin
	case(si1_at_pc)
		0:reg_at_si1=sp;
		1:reg_at_si1=fp;
		2:reg_at_si1=pc;
		default reg_at_si1=sp;
	endcase
end 

//*******************m _add side*****************
wire [datawide-1:0] m_addr;
assign m_addr = fp + 3 + si1_at_pc;

//******************regf tempA side **************
reg [datawide-1:0] regf;
reg [datawide-1:0] tempA;

//********************** core_clock side *********
wire core_clock;
assign core_clock=clk;

reg [3:0] ready_step;
reg not_ready;
reg ready_ques;

//********************* GPIO ********************
reg [7:0] mgpio [0:31];
genvar i;
generate
for(i=0;i<32;i=i+1)begin:	pack_pin
	assign gpio[i*8+7:i*8]=mgpio[i];
end
endgenerate

//********************** core *******************
reg [3:0] core_step;
always @(posedge core_clock or negedge rst)
begin 
	if (rst==0) begin
		not_ready<=1;
		ready_step<=0;
	end else if(not_ready==1) begin
		case (ready_step)
			0: begin
				core_step<=0;
				sp<=2;
				fp<=0;
				pc<=0;
				regf<=0;
				tempA<=0;
				sta_addr<=1;
				sta_data<=stop_pc;
				sta_wren<=1;
				ready_step<=1;
			end
			1: begin
				not_ready<=0; ready_step<=0; sta_wren<=0;
			end
			default: begin 	end
		endcase
	end else if (pc!=stop_pc+1) begin
		case (core_step)
			//************************** step 0 *******
			0: begin
				case (si0_at_pc)
					1: 	begin sta_addr<=sp+1; sp<=sp+1; sta_data<=si1_at_pc; sta_wren<=1; pc<=pc+1;end
					101: 	begin sta_addr<=sp+1; sp<=sp+1; sta_data<=reg_at_si1; sta_wren<=1; pc<=pc+1; end
					201: 	begin sta_addr<=m_addr; sp<=sp+1; sta_wren<=0; core_step<=1; end
					102,202,15,18,19: 	begin sta_addr<=sp; sta_wren<=0; core_step<=1; end
					3:		begin pc<=si1_at_pc+1; end
					4:		begin	sta_addr<=sp-1; sta_wren<=0; core_step<=1; end
					5:		begin if ((regf)>0) pc<=si1_at_pc+1; else pc<=pc+1; end
					6:		begin if ((regf)>=0) pc<=si1_at_pc+1; else pc<=pc+1; end
					7:		begin if ((regf)<0) pc<=si1_at_pc+1; else pc<=pc+1; end
					8:		begin if ((regf)<=0) pc<=si1_at_pc+1; else pc<=pc+1; end
					9:		begin if ((regf)==0) pc<=si1_at_pc+1; else pc<=pc+1; end
					10:	begin if ((regf)!=0) pc<=si1_at_pc+1; else pc<=pc+1; end
					11,12,13,14:	begin sta_addr<=sp-1; sta_wren<=0; core_step<=1; end
					220:	begin sta_addr<=sp; sta_data<=m_addr; sta_wren<=1; sp<=sp+1; pc<=pc+1;  end
					21:	begin sp<=sp+1; pc<=pc+1;  end
					22: 	begin sp<=sp-1; pc<=pc+1;  end
					0: pc<=pc+1; 
					16,17,24: pc<=pc+1;
					23: 	begin sta_addr<=sp; sta_wren<=0; core_step<=1; end
					default: begin 	end
				endcase
			end
			//************************** step 1 *******
			1: begin
				case (si0_at_pc)
					201:	begin sta_addr<=sp; sta_data<=sta_q; sta_wren<=1; core_step<=0; pc<=pc+1; end
					102:	begin
						case (si1_at_pc)
							0:	begin sp<=sta_q-1; pc<=pc+1; end
							1:	begin fp<=sta_q; sp<=sp-1;  pc<=pc+1; end
							2:	begin sp<=sp-1;  pc<=sta_q+1; end
						endcase
						core_step<=0;
					end
					202:	begin sta_addr<=m_addr; sta_data<=sta_q; sta_wren<=1; sp<=sp-1; core_step<=0; pc<=pc+1; end
					4,11,12,13,14: 	begin tempA<=sta_q; sta_addr<=sp; core_step<=2; end
					15:	begin	sta_data<=sta_q; sta_addr<=fp; sta_wren<=1; core_step<=0; pc<=pc+1;  end
					18:	begin	sta_addr<=sta_q; core_step<=2; end
					19:	begin	tempA<=sta_q; sta_addr<=sp-1; core_step<=2; end
					23:	begin tempA<=sta_q; sta_addr<=sp-1; core_step<=2; end
					default: begin 	end
				endcase
			end 
			//************************** step 2 *******
			2:begin
				case (si0_at_pc)
					4:		begin	regf<=tempA-sta_q; sp<=sp-2; core_step<=0; pc<=pc+1; end
					11:	begin	sta_addr<=sp-1; sta_data<=tempA+sta_q; sta_wren<=1; sp<=sp-1; core_step<=0; pc<=pc+1;  end
					12:	begin	sta_addr<=sp-1; sta_data<=tempA-sta_q; sta_wren<=1; sp<=sp-1; core_step<=0; pc<=pc+1;  end
					13:	begin	sta_addr<=sp-1; sta_data<=tempA/sta_q; sta_wren<=1; sp<=sp-1; core_step<=0; pc<=pc+1;  end
					14:	begin	sta_addr<=sp-1; sta_data<=tempA*sta_q; sta_wren<=1; sp<=sp-1; core_step<=0; pc<=pc+1;  end
					18: 	begin sta_addr<=sp; sta_data<=sta_q; sta_wren<=1; core_step<=0; pc<=pc+1;  end
					19:	begin sta_addr<=sta_q; sta_data<=tempA; sta_wren<=1; core_step<=0; pc<=pc+1;  end
					23:	begin mgpio[sta_q]<=tempA; sp<=sp-2; core_step<=0; pc<=pc+1; 
					end
					default: begin 	end
				endcase
			end
			default: begin 	end
		endcase
	end
end

endmodule
