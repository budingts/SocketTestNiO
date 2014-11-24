package com.example.sockettest;

import java.io.IOException;

public class MainTest {
	static Ntest ntest;
  public static void main(String[] args) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ntest=new Ntest("192.168.200,133",50000);
				try {
					ntest.init();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		sendMsg("xx");
		
		
	}
  public static  void sendMsg(final String msg){
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					if(ntest==null)
						continue;
					ntest.setMsg("11");
				    ntest.isChange=true;
					ntest.setMsg(msg);;
					ntest.isChange=false;
					break;    
				}
	
			}
		}).start();
		
	}
}

