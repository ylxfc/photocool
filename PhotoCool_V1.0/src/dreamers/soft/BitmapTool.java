package dreamers.soft;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapTool {
	
	public BitmapTool() {		
	}
	
	/** 图片适配大小 */
  public Bitmap change(String path) {
//    Log.v("Pic_path", path);
    Bitmap bm = null;   
    
    BitmapFactory.Options optsa = new BitmapFactory.Options();
    optsa.inSampleSize = 10;
    bm = BitmapFactory.decodeFile(path, optsa);
    if(bm != null) {
//      Log.v("***BitmapChange_Heigth_Width***", bm.getHeight() + "-" + bm.getWidth());
    } else {
      return null;
    }
//    Log.v("***Pic_Bitmap***", "Is OK"); 
    
    
    //原图片高小于480,宽小于320
    if((bm.getHeight() * 8) <= 480 && (bm.getWidth() * 8) <= 320) {
      //还原
      bm = BitmapFactory.decodeFile(path); 
//      Log.v("***BitmapDefault_Heigth_Width***", bm.getHeight() + "-" + bm.getWidth());
    } 
    
    return bm;
  }
  
  /** 图片适配大小 ,同时缩放比例*/
  public Bitmap charge(String path, int sHeigth, int sWidth) {
    Bitmap bm = this.change(path);
    
    if(bm != null) {
      bm = this.zoomImage(bm, sHeigth, sWidth);
    } 
    
    return bm;
  }

  /***
   * 图片的缩放方法
   *
   * @param bgimage
   *            ：源图片资源
   * @param newWidth
   *            ：缩放后宽度
   * @param newHeight
   *            ：缩放后高度
   * @return
   */
    public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
    	// 获取这个图片的宽和高
            int width = bgimage.getWidth();
            int height = bgimage.getHeight();
//            Log.v("ZoomImage", height + ":" + width);
         // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
         // 计算缩放率，新尺寸除原始尺寸
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
//            Log.v("ChangeZoomImage", scaleHeight + ":" + scaleWidth);
         // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight);            
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
                            matrix, true);
            return bitmap;
    }
}