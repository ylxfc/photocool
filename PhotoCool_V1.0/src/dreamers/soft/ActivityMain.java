package dreamers.soft;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;

import dreamers.soft.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


public class ActivityMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        NetWorkStatus();
        Views();
        
    }
    
    private void Views(){
    	 GridView gridview=(GridView)findViewById(R.id.gridview);
         ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
         for(int i=1;i<8;i++)   
         {   
           HashMap<String, Object> map = new HashMap<String, Object>(); 
           if(i==1){
                 map.put("ItemImage", R.drawable.g11);
                 map.put("ItemText", getResources().getString(R.string.takePic));
           }
           if(i==2){  
               map.put("ItemImage", R.drawable.g12);
               map.put("ItemText", getResources().getString(R.string.showPic));
         }
           if(i==3){  
               map.put("ItemImage", R.drawable.g13);
               map.put("ItemText", getResources().getString(R.string.editPic));
         }
           if(i==4){
        	   map.put("ItemImage", R.drawable.g10);
        	   map.put("ItemText", getResources().getString(R.string.shibie));
           }
           if(i==5){  
               map.put("ItemImage", R.drawable.g14);
               map.put("ItemText", getResources().getString(R.string.extend));   
         }
           if(i==6){  
               map.put("ItemImage", R.drawable.g16);
               map.put("ItemText", getResources().getString(R.string.help));
         }
           if(i==7){  
               map.put("ItemImage", R.drawable.g17);
               map.put("ItemText", getResources().getString(R.string.about));
         }
           lstImageItem.add(map); 
           
         }   
         SimpleAdapter saImageItems = new SimpleAdapter(this, 
                 lstImageItem, 
                 R.layout.grid_item,      
                 new String[] {"ItemImage","ItemText"},    
                 new int[] {R.id.ItemImage,R.id.ItemText});   

gridview.setAdapter(saImageItems);   
gridview.setOnItemClickListener(new ItemClickListener());   
}   


class  ItemClickListener implements OnItemClickListener   
{   

@SuppressWarnings("unchecked")
public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened    
     View arg1,//The view within the AdapterView that was clicked   
     int arg2,//The position of the view in the adapter   
     long arg3//The row id of the item that was clicked   
   ) {   

HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);   

if(item.get("ItemText").equals(getResources().getString(R.string.takePic))){
	Toast.makeText(ActivityMain.this, R.string.takePic, Toast.LENGTH_LONG).show();
	opentakePicDialog();
}
if(item.get("ItemText").equals(getResources().getString(R.string.showPic))){
	Toast.makeText(ActivityMain.this, R.string.showPic, Toast.LENGTH_LONG).show();
	openshowPicDialog();
}
if(item.get("ItemText").equals(getResources().getString(R.string.editPic))){
	Toast.makeText(ActivityMain.this, R.string.editPic, Toast.LENGTH_LONG).show();
	openseditPicDialog();
}
if(item.get("ItemText").equals(getResources().getString(R.string.shibie))){
	Toast.makeText(ActivityMain.this, R.string.shibie, Toast.LENGTH_LONG).show();
	opensShiBieDialog();
}
if(item.get("ItemText").equals(getResources().getString(R.string.extend))){
	Toast.makeText(ActivityMain.this, R.string.extend, Toast.LENGTH_LONG).show();
	openextendDialog();
}
if(item.get("ItemText").equals(getResources().getString(R.string.help))){
	Toast.makeText(ActivityMain.this, R.string.help, Toast.LENGTH_LONG).show();
	openhelpDialog();
}
if(item.get("ItemText").equals(getResources().getString(R.string.about))){ 
	Toast.makeText(ActivityMain.this, R.string.about, Toast.LENGTH_LONG).show();
	openaboutDialog();
}

}   
}
         
    
    
    
//拍照
	private void opentakePicDialog(){
		Intent intent = new Intent();
		intent.setClass(ActivityMain.this, ActivityTakePic.class);
		startActivity(intent);
		
	}
//浏览
	private void openshowPicDialog(){
		Intent intent = new Intent();
		intent.putExtra("ShowIndex",1);
		intent.setClass(ActivityMain.this, GridViewActivity.class);
		startActivity(intent);
		
	}
//编辑
		private void openseditPicDialog(){
			Intent intent = new Intent();
			intent.putExtra("ShowIndex",2);
			intent.setClass(ActivityMain.this, GridViewActivity.class);
			startActivity(intent);
			
		}
//人脸识别
		private void opensShiBieDialog() {
			Intent intent = new Intent();
			intent.setClass(ActivityMain.this, Load.class);
			startActivity(intent);
		}
//人人	
	private void openextendDialog(){
		Intent intent = new Intent();
		intent.setClass(ActivityMain.this,ActivityRenren.class);
		startActivity(intent);
	}
	private void openhelpDialog(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.help_title)
		.setMessage(R.string.help_msg)
		.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){ 
			public void onClick( DialogInterface diagloginterface , int i){
			}	
		})
		.show();
	}
	private void openaboutDialog(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.about_title)
		.setMessage(R.string.about_msg)
		.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){ 
			public void onClick( DialogInterface diagloginterface , int i){
			}	
		})
		.show();
	}
	
	private boolean NetWorkStatus() {
		//进行网络连接判定
        boolean netSataus = false;
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        cwjManager.getActiveNetworkInfo();
        if (cwjManager.getActiveNetworkInfo() != null) {
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
        }
        if (netSataus) {
            android.app.AlertDialog.Builder b = new AlertDialog.Builder(this).setTitle("没有可用的网络")
                    .setMessage("是否对网络进行设置？");
            b.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent mIntent = new Intent("/");
                    ComponentName comp = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.VIEW");
                    startActivityForResult(mIntent,0);  // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
                }
            }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            }).show();
        }
        return netSataus;
    }

	
}