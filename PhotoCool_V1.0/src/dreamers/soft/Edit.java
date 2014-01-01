package dreamers.soft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Edit extends Activity {
	String path ;
	Bitmap Editbitmap ;
	int select;
	Bitmap bitMap;
	public void onCreate(Bundle icicle) {	
		super.onCreate(icicle);
		setTitle("编辑");
		LinearLayout linLayout = new LinearLayout(this);
   
		// 获得需要操作的当前图片

		Intent intent = this.getIntent();
		path = intent.getStringExtra("PATHKEY");
		select=intent.getIntExtra("editselect", 0);
		Log.i("select","_______select :"+select);//打印
	    Bitmap bitMap_ro =BitmapFactory.decodeFile(path);
	     bitMap =  Bitmap.createScaledBitmap(bitMap_ro, bitMap_ro.getWidth(), bitMap_ro.getHeight(),true);
	      int width = bitMap.getWidth();//获得图像的宽
		  int height = bitMap.getHeight();//获得图像的高		
		  int[] pix = new int[width * height];  
	    switch(select)
	    {
		case 1:
			setTitle("缩放");
			bitMap = ImageUtils.zoomBitmap(bitMap_ro, 200, 200);
			break;
		case 2:
			setTitle("圆角");
			bitMap = ImageUtils.getRoundedCornerBitmap(bitMap_ro, 20.0f);
			
			break;
		case 3:
			setTitle("旋转");
			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();

			//旋转图片 动作   degree为: 正数表示向右(顺时针), 负数表示向左(逆时针)
		     matrix.postRotate(45);	
		  // 创建新的图片
		     bitMap = Bitmap.createBitmap(bitMap_ro, 0, 0,width, height, matrix, true);
			break;
		case 4:
			setTitle("倒影");
		    Bitmap zoombitmap = ImageUtils.zoomBitmap(bitMap_ro, 200, 200);
		    bitMap = ImageUtils.createReflectionImageWithOrigin(zoombitmap);		
			break;
		case 5:
			setTitle("画边框");
	          for (int y = 0; y < 5; y++)  
		          for (int x = 0; x < width; x++)  
		              {    
		        	  bitMap.setPixel(x, y, Color.BLUE);
		              }  
		       for (int y = height-5; y < height; y++)  
			          for (int x = 0; x < width; x++)  
			              {  
			        	  bitMap.setPixel(x, y, Color.BLUE);
			              } 
		       for (int y = 0; y < height; y++)  
			          for (int x = 0; x < 5; x++)  
			              {  
			        	  bitMap.setPixel(x, y, Color.BLUE);
			              } 
		       for (int y = 0; y < height; y++)  
			          for (int x = width-5; x < width; x++)  
			              {  
			        	  bitMap.setPixel(x, y, Color.BLUE);
			              } 
			break;
		case 6:
			setTitle("反色");
			 bitMap.getPixels(pix, 0, width, 0, 0, width, height);
			    for (int y = 0; y < width*height; y++)  
			    {   
			   
			    		pix[y]=~pix[y];    	
			    }
			    bitMap.setPixels(pix, 0, width, 0, 0, width, height);
			break;
		case 7:
			setTitle("灰度化");
			bitMap.getPixels(pix, 0, width, 0, 0, width, height);
		    for (int y = 0; y < width*height; y++)  
		    {   
		    		int r,g,b,temp;
		    		r=Color.red(pix[y]);
		    		g=Color.green(pix[y]);
		    		b=Color.blue(pix[y]);
		    		temp=(int)(r+b+g)/3;
		    		pix[y]=Color.rgb(temp, temp, temp);   	
		    }
		    bitMap.setPixels(pix, 0, width, 0, 0, width, height);
			break;
		case 8:
			setTitle("二值化");
			bitMap.getPixels(pix, 0, width, 0, 0, width, height);
		    for (int y = 0; y < width*height; y++)  
		    {   
		    	int r,g,b,temp;
	    		r=Color.red(pix[y]);
	    		g=Color.green(pix[y]);
	    		b=Color.blue(pix[y]);
	    		temp=(int) (0.3*r+0.6*g+0.1*b);
	    		if(temp>100)
	    			temp=255;
	    		else temp=0;
	    		pix[y]=Color.rgb(temp, temp, temp);
		    		   	
		    }
		    bitMap.setPixels(pix, 0, width, 0, 0, width, height);
			break;
		case 9:
			setTitle("边缘检测");
			int[] pix1=new int[width*height];
		  	   bitMap.getPixels(pix, 0, width, 0, 0, width, height);
		  	 for (int y = 0; y < width*height; y++)  
			    {   
			    	int r,g,b,temp;
		    		r=Color.red(pix[y]);
		    		g=Color.green(pix[y]);
		    		b=Color.blue(pix[y]);
		    		temp=(int) (0.3*r+0.6*g+0.1*b);
		    		pix[y]=Color.rgb(temp, temp, temp);
			    		   	
			    }
			    for (int y = 1; y < height-1; y++)
			        for (int x = 1; x < width-1; x++)  
			    {   
			    	int temp;
		    		temp=Math.abs(8*Color.red(pix[y*width+x])- Color.red(pix[y*width+x-1])-Color.red(pix[y*width+x+1])
		    				-Color.red(pix[(y-1)*width+x])-Color.red(pix[(y-1)*width+x-1])-Color.red(pix[(y-1)*width+x+1])
		    				-Color.red(pix[(y+1)*width+x])-Color.red(pix[(y+1)*width+x-1])-Color.red(pix[(y+1)*width+x+1]));  
		    		if(temp/8>120)
		    			temp=255;
		    		else temp=0;
		    		pix1[y*width+x]=Color.rgb(temp, temp, temp);
			    		   	
			    }
			    
			    bitMap.setPixels(pix1, 0, width, 0, 0, width, height);
			break;
			
	    }
	
		  
	   // Editbitmap = ImageUtils.zoomBitmap(bitmapOrg, 200, 200);
		BitmapDrawable bmd = new BitmapDrawable(bitMap);
		
		ImageView imageView = new ImageView(this);
		// 设置ImageView的图片为上面转换的图片
		imageView.setImageDrawable(bmd);
		//将图片居中显示
		imageView.setScaleType(ScaleType.CENTER);
		Toast.makeText(this, "按menu键保存或返回", Toast.LENGTH_SHORT).show();
		Toast.makeText(this, "按menu键保存或返回", Toast.LENGTH_SHORT).show();
		//Toast.makeText(this, "按menu键保存或返回", Toast.LENGTH_SHORT).show();
		//将ImageView添加到布局模板中
		linLayout.addView(imageView,
		new LinearLayout.LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT
		)
		);
		// 设置为本activity的模板
		setContentView(linLayout);
		
}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "保存");
		menu.add(0, 2, 0, "返回");
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case 1:
			FileOutputStream fos = null;
			try {
				//fos = new FileOutputStream(new File(path));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyyMMdd_HHmmss");
				String str = sdf.format(new Date());
				File folder = new File(Environment
						.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/DCIM/");//设置存放目录
				if (!folder.exists()) {
					folder.mkdir();
				}
				String name = folder.getAbsolutePath() + "/"
						+ str + ".jpg";
				File file = new File(name);
			FileOutputStream outStream = new FileOutputStream(
						file);
				bitMap.compress(CompressFormat.JPEG, 75, outStream);
				//bitMap.compress(CompressFormat.JPEG, 75, fos);
				Toast.makeText(this, "图片已保存至/sdcard/DCIM", Toast.LENGTH_SHORT).show();
				finish();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			break;
		case 2:
			finish();
			break;
		}

		
		return super.onOptionsItemSelected(item);
	}
	



}

