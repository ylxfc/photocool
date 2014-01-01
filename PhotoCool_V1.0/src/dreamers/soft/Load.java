package dreamers.soft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.media.FaceDetector;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Load extends Activity {
	
	private static final int MAX_FACES = 5;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	
	private Cursor mCursor = null;
	private List<String> allList = null;
	private List<String> peopleList = null;
	private List<String> otherList = null;
	
	private final int ERROE = 10000;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(ERROE == msg.what){
				Toast.makeText(getApplicationContext(), "发现未知错误！", Toast.LENGTH_SHORT);
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_view);
		
		ImageView loadimg = (ImageView)findViewById(R.id.load_image);
//		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//		loadimg.setAnimation( anim );
		
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ContentResolver resolver = getContentResolver();
				if (null == resolver) {
					mHandler.sendEmptyMessage(ERROE);
					finish();
				}
				if(null == MainActivity.allList){
					MainActivity.allList = new ArrayList<String>();
					allList = MainActivity.allList;
				}
				if(null == MainActivity.peopleList){
					MainActivity.peopleList = new ArrayList<String>();
					peopleList = MainActivity.peopleList;
				}
				if(null == MainActivity.otherList){
					MainActivity.otherList = new ArrayList<String>();
					otherList = MainActivity.otherList;
				}
				MainActivity.mCursor = resolver.query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
						null, null, null,
						MediaStore.Images.Media.DEFAULT_SORT_ORDER);
				if(null == MainActivity.mCursor){
					mHandler.sendEmptyMessage(ERROE);
					finish();
				}
				
				Cursor mCursor = MainActivity.mCursor;
				
				if (mCursor.moveToFirst()) {
					Log.d("coolPhpto", "getSDImages---getCount--" + mCursor.getCount());
					FaceDetector fd;
					FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
					String path = null;
					do {
						path = mCursor.getString(1);
						
						File file = new File(path);
						if( file.exists() ){
							Log.d("coolPhpto", "Load----allList--" + path);
							allList.add(path);
							Bitmap mFaceBitmap = getImageThumbnail( path ,200); 
							mFaceWidth = mFaceBitmap.getWidth();
							mFaceHeight = mFaceBitmap.getHeight();
							
					    	fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);        
				    		int c = fd.findFaces(mFaceBitmap, faces);
				    		if( c > 0 ){
				    			Log.d("coolPhpto", "Load----peopleList--" + path);
				    			peopleList.add(path);
				    		}
				    		else {
				    			Log.d("coolPhpto", "Load----otherList--" + path);
				    			otherList.add(path);
				    		}
						}
					} while (mCursor.moveToNext());
				}
				goToMain();
			}
		}, 3000);
		
        
		
	}
	
	/**
     * ���ָ����ͼ��·���ʹ�С����ȡ����ͼ 
     * �˷���������ô��� 
     *     1. ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ� 
     *        �ڶ��ζ�ȡ��bitmap�Ǹ�ݱ���ѹ�����ͼ�񣬵���ζ�ȡ��bitmap����Ҫ������ͼ�� 
     *     2. ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ 
     *        �����������ɵ�ͼ�񲻻ᱻ���졣 
     * @param imagePath ͼ���·�� 
     * @param width ָ�����ͼ��Ŀ�� 
     * @return ��ɵ�����ͼ 
     */  
	BitmapFactory.Options options = null;  
    private Bitmap getImageThumbnail(String imagePath, int width) {  
        Bitmap bitmap = null;  
        if(null == options){
        	options = new BitmapFactory.Options();
        	options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        options.inJustDecodeBounds = true;  
        // ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // �������ű�  
        int h = options.outHeight;  
        int w = options.outWidth;  
        int be = w / width;  
        if (be <= 0) {  
        	be = 1;  
        } 
        h = h/be;
        w = width; 
        options.inJustDecodeBounds = false; // ��Ϊ false  
        options.inSampleSize = be;  
        
        // ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  
	
	private void goToMain(){
		Intent main = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(main);
	}
}
