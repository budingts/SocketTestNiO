package com.example.sockettest;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	 static Ntest ntest;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		Intent in=new Intent(this, MessageService.class);
		startService(in);
	    findViewById(R.id.test).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				EditText tv=(EditText) findViewById(R.id.editText1);
				sendMsg("msg0003/2/1/1/你好/1");	
				tv.setText("");
			}
		});
	    sendMsg("msg0001/2/1/1/1/1/1/2014-5-22");
	}

	void init(){
    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public  void sendMsg(final String msg){
				while(true){
					ntest=((MainApplication)getApplication()).getNtest();
					if(ntest==null)
						continue;
					ntest.setMsg("11");
				    ntest.isChange=true;
					ntest.setMsg(msg);;
					ntest.isChange=false;
					break;    
				}
	
			}
		
		
	

}
