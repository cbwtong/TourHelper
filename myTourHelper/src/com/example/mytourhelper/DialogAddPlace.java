package com.example.mytourhelper;

import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class DialogAddPlace extends Dialog {
	
	private boolean isEditMode = false;

    private GeoPoint geoPoint;
    
    private int markType;
    
    private MapLocation location;

    private EditText editTextName;

    private EditText editTextDescription;
    
    private VoiceController voiceController;
    
    private Context mContext;
    
    private String resURL;
    
    private boolean isCancel = false;
    
    private String address;
    
    public String getAddress() {
		return address;
	}
	public String getResURL()
    {
    	return this.resURL;
    }
    public void setResURL( String url)
    {
    	this.resURL = url;
    	
    	try{ 
            Bitmap bitmap2 = BitmapFactory.decodeFile(resURL);  
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            /* 将Bitmap设定到ImageView */  
            imageView.setImageBitmap(bitmap2);  
    		}catch(Exception e){ 
    		}
    }
    public DialogAddPlace(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        init(2);
    }
    public DialogAddPlace(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init(2);
    }
    public DialogAddPlace(Context context, boolean isEditMode ,MapLocation location) {
        super(context);
        this.mContext = context;
        this.isEditMode = isEditMode;
        this.location = location;
        init(location.getMarkType());
    }
    public DialogAddPlace(Context context, boolean isEditMode ,int type) {
        super(context);
        this.mContext = context;
        this.isEditMode = isEditMode;
        init(type);
    }
    public String getTitle()
    {
    	return this.editTextName.getText().toString().trim();
    }
    public String getDiscription(){
    	return this.editTextDescription.getText().toString().trim();
    }
    public boolean isCancel(){
    	return this.isCancel;
    }
    private void initReminder()
    {
    	setContentView(R.layout.reminder);
    	Button button1 = (Button)findViewById(R.id.button1);
    	Button button2 = (Button)findViewById(R.id.button2);
    	
    	button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    	button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //创建声音标记对话框
    private void initVoiceDialog()
    {
    	setContentView(R.layout.voicedialog);
    	Button buttonAdvance = (Button) findViewById(R.id.advance);
    	buttonAdvance.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			DialogAddPlace mDialogAddPlace = new DialogAddPlace(mContext, true, -1);
    			mDialogAddPlace.show();
    		}
    	});
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveNewPlace();
                
            }
        });
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
     
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	isCancel = true;
                dismiss();
            }
        });
        Button voicePlay = (Button) findViewById(R.id.voice_play);
        
        Button voiceRecord = (Button) findViewById(R.id.voice_record);
        
        if(location != null)
        {
        	voiceController = new VoiceController(location.getResURL());
        }
        else {
        	 voiceController = new VoiceController();
		}
       
        
        
        voiceRecord.setOnTouchListener(voiceController);
        voicePlay.setOnClickListener(voiceController);
        
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        if (!isEditMode) {
            editTextName.setEnabled(false);
            editTextDescription.setEnabled(false);
            buttonSave.setVisibility(View.GONE);
            voiceRecord.setVisibility(View.GONE);
        }else {
        	voicePlay.setVisibility(View.GONE);
        	buttonAdvance.setVisibility(View.GONE);
		}
    }
    //创建图片标记对话框
    private void initPicDialog()
    {
    	setContentView(R.layout.picdialog);

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveNewPlace();
            }
        });
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        Button openCamera = (Button) findViewById(R.id.open_camera);
        
        openCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogUploadPic dialogUploadPic = new DialogUploadPic(mContext);
				dialogUploadPic.show();
			}
		});
        if(location!=null)
        {
        	try{ 

                
                Bitmap bitmap2 = BitmapFactory.decodeFile(location.getResURL());  
               
                ImageView imageView = (ImageView) findViewById(R.id.imageView);  
                /* 将Bitmap设定到ImageView */  
                imageView.setImageBitmap(bitmap2);  
            

        		}catch(Exception e){ 
        			
        		}
        }
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	isCancel = true;
                dismiss();
            }
        });
        
        
        
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        if (!isEditMode) {
            editTextName.setEnabled(false);
            editTextDescription.setEnabled(false);
            buttonSave.setVisibility(View.GONE);
            openCamera.setVisibility(View.GONE);
        }else {
        	openCamera.setVisibility(View.VISIBLE);
		}
    }
    //创建文本标记对话框
    private void initTextDialog()
    {
    	setContentView(R.layout.textdialog);

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveNewPlace();
            }
        });
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	isCancel = true;
                dismiss();
            }
        });

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        
        if (!isEditMode) {
            editTextName.setEnabled(false);
            editTextDescription.setEnabled(false);
            buttonSave.setVisibility(View.GONE);
        }
    }
    
    private void init(int type) {
        if (isEditMode) {
            setTitle(R.string.label_add_place);
        } else {
            setTitle(R.string.label_place_info);
        }
        switch(type){
        case CONSTANT.TEXT_MARKER:       	
        	initTextDialog();
        	break;
        case CONSTANT.PIC_MARKER:
        	initPicDialog();
        	break;
        case CONSTANT.VOICE_MARKER:
        	initVoiceDialog();
        	break;
        default:
        	setTitle("语音高级配置");
        	initReminder();
        	break;
        }
       
    }
    
    private void saveNewPlace() {
    	if(markType == CONSTANT.VOICE_MARKER)
    	{
    		this.resURL = voiceController.getResURL();
    	}
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        	
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), getContext().getString(R.string.label_add_name_desc),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (doesNameExist(name)) {
            Toast.makeText(getContext(),
                    getContext().getString(R.string.label_title_already_exists), Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Log.i(getClass().getName(), "Location: " + address);
        Log.i(getClass().getName(), "Name: " + name);
        Log.i(getClass().getName(), "Description: " + description);

        location = new MapLocation();
        location.setLatitude(geoPoint.getLatitudeE6()*1E6);
        location.setLongitude(geoPoint.getLongitudeE6()*1E6);
        location.setMarkType(this.markType);
        location.setName(name);
        location.setDescription(description);
        location.setResURL(this.resURL);

        DBHelper helper = new DBHelper(getContext());
        helper.open();
        location.set_id(helper.addNewLocation(location));
        helper.close();

        dismiss();
    }
    
    public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	private boolean doesNameExist(String name) {
        boolean exists = false;
        DBHelper helper = new DBHelper(getContext());
        helper.open();
        exists = helper.doesNameExist(name);
        helper.close();
        return exists;
    }
    
    public void setAddress(String address) {
        
        TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        textViewLocation.setText(address);
        this.address = address;
        
    }
    
    public void setMarkerType( int type )
    {
    	this.markType = type;
    }
    
    public void displayLocationInfo(MapLocation location) {
        editTextName.setText(location.getName());
        editTextDescription.setText(location.getDescription());     
        setMarkerType(location.getMarkType());
    }

    public MapLocation getLocation() {
        return location;
    }
}
