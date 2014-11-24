package com.example.sockettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;


public class Ntest{
	public static boolean isChange=false;
	private String ip;
	private int port;
	private String msg;
	private MessageListener  messageListener;
	
 


	public Ntest(String ip,int port){
		this.ip=ip;
		this.port=port;
		
	}
    

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener=messageListener;
	}




	/**
	 * @param args
	 * @throws IOException 
	 */
	public  void init() throws IOException{
		SocketChannel channel=SocketChannel.open();
		channel.configureBlocking(false);
		SocketAddress target=new InetSocketAddress(ip,port);
		channel.connect(target);
		Selector selector=Selector.open();
		//用于套接字连接操作的操作集位
		channel.register(selector, SelectionKey.OP_CONNECT);
		
		
		//BufferedReader systemIn=new BufferedReader(new InputStreamReader(System.in));
		while(true){
			if(channel.isConnected()&&!isChange){			
				if(msg!=null){
					channel.write(Charset.forName("UTF-8").encode(msg));
				    msg=null;		
					/*if("quit".equalsIgnoreCase(command.trim())){
						systemIn.close();
						channel.close();
						selector.close();
						System.out.println("Client quit !");
						System.exit(0);
					}*/
				}	
			}
			int nKeys=0;
			try {
				nKeys=selector.select(1000);
			} catch (Exception e) {
				continue;
			}
			if(nKeys>0){
				for(SelectionKey key:selector.selectedKeys()){
					if(key.isConnectable()){
						System.out.println("c_s---------------------");
						SocketChannel sc=(SocketChannel) key.channel();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
						sc.finishConnect();
						System.out.println("c_end---------------------");
					}else if(key.isReadable()){
						System.out.println("ke_s---------------------");
						ByteBuffer buffer=ByteBuffer.allocate(1024);
						SocketChannel sc=(SocketChannel) key.channel();
						int readBytes=0;
						try{
							int ret=0;
							try{
								while((ret=sc.read(buffer))>0){
									readBytes+=ret;
								}
							}finally{
								buffer.flip();
							}
							if (readBytes > 0) {   
                                System.out.println(Charset.forName("UTF-8")   
                                        .decode(buffer).toString());   
                                messageListener.Message(Charset.forName("UTF-8")   
                                        .decode(buffer).toString());
                                buffer = null;   
                            } 
							
							System.out.println("ke_end---------------------");
						}finally {   
                            if (buffer != null) {   
                                buffer.clear();   
                            }
						}
					}
				}
					selector.selectedKeys().clear();   
			}	
		}
		
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
