package dreamers.soft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.bean.FeedParam;


import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.ConnectButton;
import com.renren.api.connect.android.view.ConnectButtonListener;
import com.renren.api.connect.android.view.RenrenAuthListener;

import dreamers.soft.R;

public class ActivityRenren extends Activity implements OnClickListener{
	Bitmap bitMap;
	private String apiKey = "07171b95ff5645779461233b79e41b13"; 
	//yulixfc@gmail.com   ,应用id= 197534 //"API Key：07171b95ff5645779461233b79e41b13 Secret Key：d78915b96f2a489bab38be42adc1e6c4
	
	private TextView titletext;
	private GridView grid;
	
    public static final String USER_FULL_FIELDS = "name,email_hash, sex,star,birthday,tinyurl,headurl,mainurl,hometown_location,hs_history,university_history,work_history,contact_info";
    public static final String USER_COMMON_FIELDS = "name,email_hash,sex,star,birthday,tinyurl,headurl,mainurl";
	
    String dataFormat = "json";
	ConnectButton login;
	Renren renren;
    AsyncRenren asyncRenren;
    SimpleRequestListener simpleRequestListener;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renren_photo);
		findViews();
		
        this.renren = new Renren(apiKey);
        this.renren.restorSessionKey(this);
        this.asyncRenren = new AsyncRenren(renren);
        
		setViews();
		
		this.simpleRequestListener = new SimpleRequestListener(this);
	
		//getUserInfo();
		//getFrindsList();

	}
	
	
	
	//获取个人信息	
	private void getUserInfo()
	{
        Bundle params = new Bundle();
        params.putString("method", "users.getInfo");
        String fields = USER_FULL_FIELDS;
        params.putString("fields", fields);
        simpleRequestListener.showProgress("个人信息");
        asyncRenren.requestJSON(params, simpleRequestListener);
	}
	
	//好友列表	
	private void getFrindsList()
	{
        Bundle params = new Bundle();
        params.putString("method", "friends.getFriends");
        params.putString("page", "1");
        params.putString("count", "10");
        simpleRequestListener.showProgress("好友列表");
        asyncRenren.requestJSON(params, simpleRequestListener);
        
        //setViewImage();
    
        //simpleRequestListener.showProgress("获取好友");
        //this.asyncRenren.request(params, simpleRequestListener, dataFormat);
 /*      FriendRequestListener listener = new FriendRequestListener(this, dataFormat);
        if ("xml".equalsIgnoreCase(dataFormat)) {
            this.asyncRenren.requestXML(params, listener);
        } else {
            this.asyncRenren.requestJSON(params, listener);
        }
  */      

	}

	//我的相册
	private void getPhotoList()
	{
        Bundle params = new Bundle();
        params.putString("method", "photos.getAlbums");
        params.putString("uid", "197534");
        params.putString("count", "10");
        simpleRequestListener.showProgress("我的相册");
        asyncRenren.requestJSON(params, simpleRequestListener);
       
	}
	
	private void findViews(){
		login = (ConnectButton) findViewById(R.id.login);
		titletext = (TextView) findViewById(R.id.renren_title);
		grid = (GridView) findViewById(R.id.photo_list);
		
	}
	private void setViews(){
		findViewById(R.id.ceshi_login).setOnClickListener(this);
		findViewById(R.id.my_info).setOnClickListener(this);
		findViewById(R.id.friend_list).setOnClickListener(this);
		findViewById(R.id.my_photo).setOnClickListener(this);
		findViewById(R.id.upload_photo).setOnClickListener(this);
		findViewById(R.id.download_photo).setOnClickListener(this);
		login.init(renren, this);
        login.setConnectButtonListener(new ConnectButtonListener() {
  		
			@Override
			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				// TODO Auto-generated method stub	
			}
			
			@Override
			public void onLogouted() {
				Toast.makeText(ActivityRenren.this, "退出成功", Toast.LENGTH_SHORT).show();
				titletext.setText(R.string.renren_login);	
			}
			
			@Override
			public void onLogined(Bundle values) {
				Toast.makeText(ActivityRenren.this, "登录成功", Toast.LENGTH_SHORT).show();
				titletext.setText(R.string.renren_logout);
			}
			
			@Override
			public void onException(Exception error) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onCancelLogin() {	
				titletext.setText(R.string.renren_logout);
			}
			
			@Override
			public void onCancelAuth(Bundle values) {
				// TODO Auto-generated method stub
			}
		});
    	
	}
	
    public void onClick(View v) {
    	titletext.setText( "sessionKey:" + renren.getSessionKey() );  
    	if(v.getId() == R.id.ceshi_login)
    	{
    		RenrenAuthListener listener = new SimpleRenrenAuthListener(this, "User Agent Flow");
            String[] req_auth={"read_user_album","photo_upload"};
            this.renren.authorize(this, req_auth, listener);
    		
    	}
    	else if (v.getId() == R.id.my_info) 
    	{
    		getUserInfo();
        }
    	else if (v.getId() == R.id.friend_list) 
        {
    		getFrindsList();
    		// 	myView = new ImageView(this);
    		//setViewImage(myView, " http://www.google.com.hk/images/srpr/nav_logo14.png");

        }     	
    	else if (v.getId() == R.id.my_photo){
        	
    		getPhotoList();
        } 
    	else if (v.getId() == R.id.upload_photo){
            long albumId = 463414613;
            String fileName = "111.jpg";
            String desc = "使用PhotoCool_V1.0上传" + new Date();
            try {
                InputStream is = this.getAssets().open(fileName);
                byte[] photo = ExampleUtil.getBytes(is);
                simpleRequestListener.showProgress("上传照片");
                this.asyncRenren.uploadPhoto(albumId, photo, fileName, desc, dataFormat,
                        simpleRequestListener);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            }
    	}
    	else if (v.getId() == R.id.download_photo){
    		
    		String picUrl = null;
    		String picName = null;
    		if( simpleRequestListener.curText != null)
    		{
	            try {
	                JSONArray friends = new JSONArray(simpleRequestListener.curText);
	                for (int i = 0; i < friends.length(); i++) {
	                    Map<String, String> d = new HashMap<String, String>();
	                    Object obj = friends.get(i);
	                    JSONObject jobj = new JSONObject(obj.toString());
	                    picUrl = jobj.getString("url");
	                    picName = jobj.getString("name");

	            		myView = new ImageView(this);
	            		//setViewImage(myView, " http://www.google.com.hk/images/srpr/nav_logo14.png");
	            		setViewImage(myView, picUrl);
	            		setContentView(myView);
	            		
	                    //break; // Only to Show the first Phote;
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
    		}
    	}
    }	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "退出登录");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			renren.logout(this);
		}
		return super.onOptionsItemSelected(item);
	}
	
    private ImageView myView;
    private void setViewImage(ImageView v, String headurl) {
        try {
            URL url = new URL(headurl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openStream());
            v.setImageBitmap(bmp);
            titletext.setText( "bmp:" + headurl ); 
            saveBmptoFile(bmp, Environment.getExternalStorageDirectory().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            setViewImage(v, headurl);
        }
    }

    private void saveBmptoFile(Bitmap bitMap, String filePath)
    {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyyMMdd_HHmmss");
			String str = sdf.format(new Date());
			File folder = new File(filePath	+ "/DCIM/");//设置存放目录
			if (!folder.exists()) {
				folder.mkdir();
			}
			String name = folder.getAbsolutePath() + "/"
					+ str + ".jpg";
			File file = new File(name);
			FileOutputStream outStream = new FileOutputStream(
					file);
			bitMap.compress(CompressFormat.JPEG, 75, outStream);
			Toast.makeText(this, "图片已保存至/sdcard/DCIM", Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
    
}
