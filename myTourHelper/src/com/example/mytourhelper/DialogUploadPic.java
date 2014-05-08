package com.example.mytourhelper;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DialogUploadPic extends Dialog implements OnClickListener{

	private String PicUrl;
	private Context mContext;
		
	public String getPicUrl()
	{
		return this.PicUrl;
	}
	public DialogUploadPic(Context context) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
		setContentView(R.layout.uploadpictrue);

        Button openCamere = (Button) findViewById(R.id.open_camera);
        Button openLocal = (Button) findViewById(R.id.open_local);
        openCamere.setOnClickListener(this);
        openLocal.setOnClickListener(this);

	}
	public DialogUploadPic(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		 case R.id.open_camera:
//			   Message msg = new Message();
//			   msg.what = CONSTANT.OPEN_CAMERA;
//			   Handler myhandler = new Handler();
//			   myhandler.sendMessage(msg);
//             Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//             mContext.startActivity(intent);
			 ((BaseMapActivity)mContext).openNewActivity(CONSTANT.OPEN_CAMERA);
			 
			    dismiss();
			 break;
		 case R.id.open_local:
//			   Message msg2 = new Message();
//			   msg2.what = CONSTANT.OPEN_LOCAL_PICTURE;
//			   Handler myhandler2 = new Handler();
//			   myhandler2.sendMessage(msg2);
//			    Intent intent2 = new Intent();
//			    intent2.setType("image/*");
//			    intent2.setAction(Intent.ACTION_GET_CONTENT);
//			    ((Activity)mContext).startActivityForResult(intent2, 1);
			 	((BaseMapActivity)mContext).openNewActivity(CONSTANT.OPEN_LOCAL_PICTURE);
			    dismiss();
			 break;
		}
	}
	
}
