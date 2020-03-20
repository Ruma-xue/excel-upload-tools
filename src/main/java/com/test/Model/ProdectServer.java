package com.test.Model;


public class ProdectServer {
	
	private	String	cd_id;
	
	private	String	Product_brand;
	
	private	String	Manufacturers;
	
	private	String	Enterprise_Credit_Code_Produce;
	
	private	String	Production_Date;
	
	private	String	Test_Time;
	
	private	String	Equipment_Units_Used;
	
	private	String	Enterprise_Credit_Code_Use;
	
	private	String	Jurisdiction_Area;
	
	private	String	Next_Test_Time;
	
	private	String	Equipment_Validity;
	
	private	String	Inspection_Unit;
	
	private	String	Equipment_Code;
	
	private	String	cd_time;
	
	private	String	cd_source;
	
	private	String	cd_batch;
	
	private	String	cd_operation;


    @Override
    public String toString() {
        return "ProdectServer{" +
		        "cd_id='"+cd_id	+'\'' +
		        ",Product_brand='" +Product_brand+ '\'' +
		        ",Manufacturers='" +Manufacturers+ '\'' +
		        ",Enterprise_Credit_Code_Produce='" +Enterprise_Credit_Code_Produce+ '\'' +
		        ",Production_Date='" +Production_Date+ '\'' +
		        ",Test_Time='" +Test_Time+ '\'' +
		        ",Equipment_Units_Used='" +Equipment_Units_Used+ '\'' +
		        ",Enterprise_Credit_Code_Use='" +Enterprise_Credit_Code_Use+ '\'' +
		        ",Jurisdiction_Area='" +Jurisdiction_Area+ '\'' +
		        ",Next_Test_Time='" +Next_Test_Time+ '\'' +
		        ",Equipment_Validity='" +Equipment_Validity+ '\'' +
		        ",Inspection_Unit='" +Inspection_Unit+ '\'' +
		        ",Equipment_Code='" +Equipment_Code+ '\'' +
		        ",cd_time='" +cd_time+ '\'' +
		        ",cd_source='" +cd_source+ '\'' +
		        ",cd_batch='" +cd_batch+ '\'' +
		        ",cd_operation='" +cd_operation+ '\'' +
        '}';
    }

}
