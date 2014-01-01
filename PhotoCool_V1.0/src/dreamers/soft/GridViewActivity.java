package dreamers.soft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dreamers.soft.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.Window;
import android.view.SubMenu;


public class GridViewActivity extends Activity {
  private static List<String> lstFilePath = null;
  
  //清除LST 
  public static void unloadLstFilePath() {
    if (lstFilePath != null) {
      lstFilePath.clear();
      lstFilePath = null;
    }
  }

  private Intent mIntent;

  private ProgressDialog m_ProgressDialog = null;
  private ProgressDialog m_ProgressDialog1 = null;
  // 目录sdcard/
  private String mRootPath = "/sdcard/DCIM";
  // 当前选定的图片路径 
  private String mCurrPicPath = "";
  private Gallery gallery; 
  private ImageView mImageView;
  private ViewFlipper mFlipper ;
    
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mIntent = this.getIntent();
    
    setContentView(R.layout.show_gridview_pic);
    
    initFlipper();
    int temp1 = mIntent.getIntExtra("ShowIndex", 0); 
    if(temp1==1)
    {
    	setTitle("长按下方图片选择自动浏览");
    }
    else
    	{
    	setTitle("长按下方图进行操作");
    	}
   // setTitle("常按下图左键进行编辑");
  //搜索路径
    Runnable viewOrders = new Runnable() {
      public void run() {
        getValues();
      }
    };
    Thread thread = new Thread(null, viewOrders, "MagentoBackground");
    thread.start();
  //读取图片数据中
    m_ProgressDialog = ProgressDialog.show(GridViewActivity.this, "请稍后", "数据读取中...   ", true);
   
  }

  private void initFlipper(){
      mFlipper = (ViewFlipper)findViewById(R.id.viewflipper);  
      mFlipper.setInAnimation(this, R.anim.push_left_in);
      mFlipper.setOutAnimation(this, R.anim.push_left_out);
  }
  
  private void getValues() {
    if (lstFilePath == null) {
      Log.v("***InLstFilePath***", "OK");
      lstFilePath = new ArrayList<String>();
      lstFilePath.clear();
    //搜索图片，然后把图片的路径保存到lstFilePath里面。
      new FileTool().ListFiles(mRootPath, lstFilePath);
      
    } else {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ;
    }
    runOnUiThread(returnRes); //在线程中执行UI的更新
  }
//定义菜单项id
  final int MENU_ONE = Menu.FIRST;
  final int MENU_TWO = Menu.FIRST + 1;
  final int MENU_THREE = Menu.FIRST + 2;
  
  /** UI同步 */
  private Runnable returnRes = new Runnable() {

    public void run() {
    	//UI代码
      GridViewImageAdapter iadapter = new GridViewImageAdapter(GridViewActivity.this, lstFilePath);
      gallery = (Gallery) findViewById(R.id.sdcard_gridview_pic_grid);

      gallery.setAdapter(iadapter);
      gallery.setOnItemSelectedListener(new ItemSelectedListener());
      gallery.setOnItemLongClickListener(new ItemLongClickListener());
      gallery.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
    	//创建一个上下文菜单
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

          menu.setHeaderTitle("图片操作");
          int temp = mIntent.getIntExtra("ShowIndex", 0);
         // Log.i("temp","______________temp :"+temp);//打印
        if(temp==1)
        {
        	 
        	menu.add(0,MENU_ONE,0,"自动浏览").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // TODO Auto-generated method stub 
              	 // opentishiDialog();
              	 
                    Intent intenta = new Intent();
                    intenta.putExtra("picIndex", gallery.getSelectedItemPosition());
                    intenta.putStringArrayListExtra("picList", (ArrayList<String>) lstFilePath);
                    intenta.setClass(GridViewActivity.this, AutoPlayActivity.class);
                    startActivity(intenta);
                     return false;  
                     }
               });
               //分享照片
               menu.add(0,MENU_TWO,0,"分享到新浪微博").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                   
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       // TODO Auto-generated method stub 
                 	 // opentishiDialog();
                 	 
                  	 Intent intent = new Intent();
                  	 intent.putExtra("PATHKEY",mCurrPicPath );//
               		 intent.setClass(GridViewActivity.this, AndroidGeKey.class);
               		 startActivity(intent);
                        return false;  
                        
                        }
                  });
               menu.add(0, MENU_THREE, 0, "退出").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                   public boolean onMenuItemClick(MenuItem item) {
                 	  return false;
                   }
                 });
       }  
        else{
        	 
        	final SubMenu submenu  = 
        	         menu.addSubMenu("编辑");  
        	         submenu.add(0, 1, 0, "反色").setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        	     public boolean onMenuItemClick(MenuItem item) {
	        		            Intent intent6 = new Intent();
	                        	intent6.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
	                        	intent6.putExtra("editselect",6 );
	                        	intent6.setClass(GridViewActivity.this, Edit.class);
	                    		startActivity(intent6);
	                return false;
	              }
	          });
	                 submenu.add(0, 2, 0, "灰度化").setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        	     public boolean onMenuItemClick(MenuItem item) {
	        		            Intent intent7 = new Intent();
	                        	intent7.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息

	                        	intent7.putExtra("editselect",7 );
	                        	intent7.setClass(GridViewActivity.this, Edit.class);
	                    		startActivity(intent7);
	                return false;
	              }
	          });
	               submenu.add(0, 3, 0, "二值化").setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        	   public boolean onMenuItemClick(MenuItem item) {
	        		            Intent intent8 = new Intent();
	                        	intent8.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
	                        	intent8.putExtra("editselect",8 );
	                        	intent8.setClass(GridViewActivity.this, Edit.class);
	                    		startActivity(intent8);
	                return false;
	              }
	          });
	              submenu.add(0, 4, 0, "边缘检测").setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        	  public boolean onMenuItemClick(MenuItem item) {
	        		            Intent intent9= new Intent();
	                        	intent9.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
	                        	intent9.putExtra("editselect",9 );
	                        	intent9.setClass(GridViewActivity.this, Edit.class);
	                    		startActivity(intent9);
	                return false;
	              }
	          });
        	          submenu.add(0, 5, 0, "缩放").setOnMenuItemClickListener(new OnMenuItemClickListener(){
        	        	  public boolean onMenuItemClick(MenuItem item) {
        	        		            Intent intent1 = new Intent();
        	                        	intent1.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
        	                        	intent1.putExtra("editselect",1 );
        	                        	intent1.setClass(GridViewActivity.this, Edit.class);
        	                    		startActivity(intent1);
        	                return false;
        	              }
        	          });
        	          submenu.add(0, 6, 0, "圆角").setOnMenuItemClickListener(new OnMenuItemClickListener(){
        	        	  public boolean onMenuItemClick(MenuItem item) {
        	        		            Intent intent2 = new Intent();
        	                        	intent2.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
        	                        	intent2.putExtra("editselect",2 );
        	                        	intent2.setClass(GridViewActivity.this, Edit.class);
        	                    		startActivity(intent2);
        	                return false;
        	              }
        	          });
        	          submenu.add(0, 7, 0, "旋转").setOnMenuItemClickListener(new OnMenuItemClickListener(){
        	        	  public boolean onMenuItemClick(MenuItem item) {
        	        		            Intent intent3 = new Intent();
        	                        	intent3.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
        	                        	intent3.putExtra("editselect",3 );
        	                        	intent3.setClass(GridViewActivity.this, Edit.class);
        	                    		startActivity(intent3);
        	                return false;
        	              }
        	          });
        	          submenu.add(0, 8, 0, "倒影").setOnMenuItemClickListener(new OnMenuItemClickListener(){
        	        	  public boolean onMenuItemClick(MenuItem item) {
        	        		            Intent intent4 = new Intent();
        	                        	intent4.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
        	                        	intent4.putExtra("editselect",4 );
        	                        	intent4.setClass(GridViewActivity.this, Edit.class);
        	                    		startActivity(intent4);
        	                return false;
        	              }
        	          }); 
        	          submenu.add(0, 9, 0, "画边框").setOnMenuItemClickListener(new OnMenuItemClickListener(){
        	        	  public boolean onMenuItemClick(MenuItem item) {
        	        		            Intent intent5 = new Intent();
        	                        	intent5.putExtra("PATHKEY",mCurrPicPath );//获得当前的图片信息
        	                        	intent5.putExtra("editselect",5);
        	                        	intent5.setClass(GridViewActivity.this, Edit.class);
        	                    		startActivity(intent5);
        	                return false;
        	              }
        	          });
        	          submenu.add(0, 10, 0, "退出").setOnMenuItemClickListener(new OnMenuItemClickListener(){
        	        	  public boolean onMenuItemClick(MenuItem item) {
        	                return false;
        	              }
        	          });
          
/////////2 删除图片操作
          menu.add(0, MENU_TWO, 0, "删除").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                      public boolean onMenuItemClick(MenuItem item) {
                          opencheckDialog();
                          return false;
                      }
                  });
          menu.add(0, MENU_ONE, 0, "退出").setOnMenuItemClickListener(new OnMenuItemClickListener() {
              public boolean onMenuItemClick(MenuItem item) {
            	  return false;
              }
            });
          
        }
//////////////////////
//////////////3退出操作
         
        }
      });
      m_ProgressDialog.dismiss();
    }
  };
  private void opentishiDialog(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("shkjks")
		.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){ 
			public void onClick( DialogInterface diagloginterface , int i){
			}	
		})
		.show();
	}
//长按进入图片操作
  private class ItemLongClickListener implements OnItemLongClickListener {

    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      if (GridViewActivity.lstFilePath.size() > 0) {
        String path = GridViewActivity.lstFilePath.get(arg2);

        mCurrPicPath = path;

        Toast.makeText(GridViewActivity.this, path, Toast.LENGTH_SHORT).show();
      }
      return false;
    }

  }
  //点击图片
  private class ItemSelectedListener implements OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      String path = GridViewActivity.lstFilePath.get(arg2);
      Bitmap bitmap = BitmapFactory.decodeFile(path);
//      mImageView = (ImageView) findViewById(R.id.imview);
//      mImageView.setImageBitmap(bitmap);
      
      ImageView child = (ImageView) mFlipper.getChildAt(1-mFlipper.getDisplayedChild());
      child.setImageBitmap(bitmap);
      mFlipper.showNext();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}
  }
//删除图片对话框
  private void opencheckDialog() {
      new AlertDialog.Builder(this)
              .setTitle("温馨提示")
              .setMessage("真的要删除这张图片吗？")
              .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface diagloginterface, int i) {
                      boolean errFlag = true;
                      if (!mCurrPicPath.equals("")) {
                          File f = new File(mCurrPicPath);
                          if (f.exists()) {
                              f.delete();

                              lstFilePath.remove(mCurrPicPath);
                              // 是不是转到GridViewImageAdapter中去？？？？？？？？？？？？？
                              GridViewImageAdapter iadapter = new GridViewImageAdapter(
                                      GridViewActivity.this, lstFilePath);
                              gallery.setAdapter(iadapter);
                          } else {
                              errFlag = false;
                          }
                      } else {
                          errFlag = false;
                      }

                      if (!errFlag) {
                          Toast.makeText(GridViewActivity.this, "操作异常，请重新选择",
                                  Toast.LENGTH_LONG).show();
                      }
                  }
              }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int i) {

                  }
              }).show();
  }
}
