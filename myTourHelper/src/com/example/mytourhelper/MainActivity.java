package com.example.mytourhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private Button createBtn,openBtn,voiceBtn;
	
	private MyDialog dialog = new MyDialog();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createBtn = (Button)findViewById(R.id.CreateMap);
		openBtn = (Button)findViewById(R.id.OpenMap);
		voiceBtn = (Button)findViewById(R.id.voiceShare);
		
		createBtn.setOnClickListener(this);
		openBtn.setOnClickListener(this);
		voiceBtn.setOnClickListener(this);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add() Parameters
//		groupId	The group identifier that this item should be part of. This can be used to define groups of items for batch state changes. Normally use NONE if an item should not be in a group.
//		itemId	Unique item ID. Use NONE if you do not need a unique ID.
//		order	The order for the item. Use NONE if you do not care about the order. See getOrder().
//		title	The text to display for the item.
		menu.add(menu.NONE,1,menu.NONE,"°ïÖú");
		menu.add(menu.NONE,2,menu.NONE,"¹ØÓÚ");
		menu.add(menu.NONE,3,menu.NONE,"ÍË³ö");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			dialog.createMessageDialogHelp(this);
			break;
		case 2:
			dialog.createMessageDialoAbout(this);
			break;
		case 3:
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onClick(View v) {
		Bundle data = new Bundle();
		switch(v.getId()){
		case R.id.CreateMap:
			data.putInt("type", 0);
			startActivity(new Intent(this,BaseMapActivity.class).putExtras(data));
			break;
		case R.id.OpenMap:
			data.putInt("type", 1);
			startActivity(new Intent(this,BaseMapActivity.class).putExtras(data));
			break;
		case R.id.voiceShare:
//			startActivity(new Intent(this,ShareVoice.class));
		}
	}
	
	



}
