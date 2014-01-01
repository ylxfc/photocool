package dreamers.soft;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dreamers.soft.R;

//import com.newding.hunter.android.R;

import android.content.Context;
import android.util.Log;

public class FileTool {
	/** 图片内存最小值 5K */
  private final static int PIC_FILE_SIZE_MIN = 5;
  /** 图片内存最大值 1000K */
  private final static int PIC_FILE_SIZE_MAX = 1000;
  
  
  /**
   * 遍历目录,得到所有图片文件路径集群
   * @param path
   * @return
   */
    public void ListFiles(String path, List<String> lstPaths){
      File file = new File(path); 
      if(file == null)
        return;
      
      if(file.isDirectory() && path.length() > 4) {       
        String reName = path.substring(0, 4);
        if(reName.equals("/sys") || reName.equals("/tmp") ||
            reName.equals("/pro"))
          return;
      
      }
      
      File[] fs = file.listFiles();
      if(fs==null)
        return;
    for(File f : fs){
      if(f==null)
        continue;
        
      String fName = f.getName();     
      String htx = fName.substring(fName.lastIndexOf(".") + 1,
          fName.length()).toLowerCase();  ////得到扩展名
      
      if(htx.equals("png") || htx.equals("jpg") ||
          htx.equals("gif") || htx.equals("bmp")) {
        
        if(fileSizeValidity(f.getPath())) {
          lstPaths.add(f.getPath());
          Log.v("***PIC_FILE***", fName);
        }
      } else {
          //Log.v("***PIC_PATH***", f.getPath());
      }

      path = f.getAbsolutePath();
      if(f.isDirectory() == true) {
        ListFiles(path, lstPaths);     //当f为文件夹的时候，进入文件夹中     
      }
    }
    }
    
    /** 
     * 判断文件大小有效性
     * 如果文件内存在5K-300K之间则有效，否则返回FALSE
     * @param path
     * @return
     */
    private boolean fileSizeValidity(String path) {
      File f = new File(path);
      if(f.exists()) {
        int cur = 0;
        FileInputStream fis = null;
      try {
        fis = new FileInputStream(f);
        
        cur = fis.available()/1000;
        
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          fis.close();            
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      
      //图片内存在5K-1000K之间，表示有效
      if(cur>=PIC_FILE_SIZE_MIN && cur<=PIC_FILE_SIZE_MAX)
        return true;
      
      }
      
      return false;
    }
    
    
    /**
     * 将数据文件保存到本机目录
     * @param path 为 0 写入help.html
     *         为 1 写入vmc
     *         为 2 写入vmr
     */
    public void saveWMC(Context ctx, String path, int vmr) {
      InputStream in = null;
      BufferedInputStream bis = null;
      FileOutputStream fos = null;
      
      try {
        switch(vmr) {
        case 0: //help
//          in = ctx.getResources().openRawResource(R.raw.help);
          break;
        case 1: //vmc
          in = ctx.getResources().openRawResource(R.raw.newding);
          break;
        case 2: //vmr
          in = ctx.getResources().openRawResource(R.raw.newdingvmr);
          break;
        }
      byte[] buffer = new byte[10*1024];
      
      bis = new BufferedInputStream(in);
      fos = new FileOutputStream(path);
      
      int a = bis.read(buffer, 0, buffer.length);
      while (a != -1) {       
        fos.write(buffer, 0, a);
        fos.flush();
        a = bis.read(buffer, 0, buffer.length);
      }
    } catch (Exception e) {
//      new IOTool().write("FileTool:" + e.getMessage());
      System.out.println(e.getMessage());
    } finally {
      try {
        fos.close();
        bis.close();
        in.close();
      } catch (Exception e2) {
      }
    }
    }
}