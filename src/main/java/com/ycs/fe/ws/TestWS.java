package com.ycs.fe.ws;

public class TestWS {
	public static void main(String args[]){
		SPCallService spcallsvc = new SPCallService();
		SPCall spcallendpoint  = spcallsvc.getSPCallPort();
		System.out.println(spcallendpoint.callSP("samarjit"));
	}
}
