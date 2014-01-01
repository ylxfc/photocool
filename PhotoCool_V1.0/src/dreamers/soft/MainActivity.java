package dreamers.soft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener,GridView.OnScrollListener{

	private Context mContext = null;
	
	public static Cursor mCursor = null;
	public static List<String> allList = null;
	public static List<String> peopleList = null;
	public static List<String> otherList = null;
	
	private RelativeLayout mHeadBar = null;
	private TextView mNaviTip = null;
	private ImageButton mRefresh = null;
	private GridView mGridView = null;
	private GridAdapter mAdapter = null;
	
	private boolean mBusy = false;
	
	//private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200; 
	private static final int MAX_FACES = 5;
	
	private String STORE_NAME = "MYDATA";
	private int LOAD_COUNT = -1;
	private Dialog mDialog = null;

	private final int ERROR = 10000;
	private final int FIRST_LOAD = 10001;
	private final int SHOW_DIALOG = 10002;
	private final int DISMISS_DIALOG = 10003;
	private final int DO_SCAN = 10004;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (SHOW_DIALOG == msg.what) {
				// 调用对话框弹出函数
				Log.d("coolPhpto", "mHandler SHOW_DIALOG-----------");
				if( !mDialog.isShowing() ) mDialog.show();
			}
			else if( DO_SCAN == msg.what ){
				Log.d("coolPhpto", "mHandler DO_SCAN-----------");
				//getSDImages();
				mAdapter.notifyDataSetChanged();
			}
			else if (DISMISS_DIALOG == msg.what) {
				// 调用消失函数对话框
				Log.d("coolPhpto", "mHandler DISMISS_DIALOG-----------");
				if( mDialog.isShowing() )mDialog.dismiss();
			}
			else if( FIRST_LOAD == msg.what ){
				//调用识别函数
			}
			else if (ERROR == msg.what) {
				// 调用错误提示函数
			}
			super.handleMessage(msg);
		}

	};
	
	private PopupWindow scanMode = null;
	private void showScanModePopWin(){
		if(null == scanMode){
			ListView listView = (ListView) getLayoutInflater().inflate(R.layout.scan_mode_view, null);
			List<String> data = new ArrayList<String>();
	        data.add("人物模式");
	        data.add("其他模式");
	        data.add("所有图片");
	        listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.scan_mode_view_item,data));
	        listView.setOnItemClickListener(this);
	        scanMode = new PopupWindow(listView,150,LayoutParams.WRAP_CONTENT);
	    	//以下两行设置点击popup空白区点击消失
	        scanMode.setBackgroundDrawable(new BitmapDrawable());
	        scanMode.setOutsideTouchable(true);
	        scanMode.setFocusable(true); 
		}
		
		if(scanMode.isShowing()){
			scanMode.dismiss();
		} else {
			scanMode.update(); 
			scanMode.showAsDropDown(mHeadBar,3,3);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		scanMode.dismiss();
		doSelect(position);
	}
	
	private void doSelect(int i){
		switch(i){
			case 0:getPeopleImgs();break;
			case 1:getOtherImgs();break;
			case 2:getAllImgs();break;
		}
	}
	
	private void getPeopleImgs(){
		mNaviTip.setText("人脸识别");
		mAdapter.setImgPath(peopleList);
	}
	
	private void getOtherImgs(){
		mNaviTip.setText("其他模式");
		mAdapter.setImgPath(otherList);
	}
	
	private void getAllImgs(){
		mNaviTip.setText("所有图片");
		mAdapter.setImgPath(allList);
	}
	
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
    }
    

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
        case OnScrollListener.SCROLL_STATE_IDLE:
        	Log.d("coolPhpto", "SCROLL_STATE_IDLE-----------");
            mBusy = false;
            int first = view.getFirstVisiblePosition();
            int count = view.getChildCount();
            for (int i=0; i<count; i++) {
            	RelativeLayout L = (RelativeLayout)view.getChildAt(i);
                if (L.getTag() != null) {
//                	ImageView img = (ImageView)L.findViewById(R.id.image);
//                	File file = new File( imgPath[i] );
//        			if (file.exists()) {
//        				Bitmap bm = getImageThumbnail( imgPath[i] ,150);
//        				// ��ͼƬ��ʾ��ImageView��
//        				img.setImageBitmap(bm);
//        			}
//                    L.setTag(null);
                }
            }
            break;
        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
        	Log.d("coolPhpto", "SCROLL_STATE_TOUCH_SCROLL-----------");
            mBusy = true;
            break;
        case OnScrollListener.SCROLL_STATE_FLING:
        	Log.d("coolPhpto", "SCROLL_STATE_FLING-----------");
            mBusy = true;
            break;
        }
    }
	
    private int getStoreData(String name,int value){
    	SharedPreferences preferences = getSharedPreferences(STORE_NAME,
				MODE_PRIVATE);
		return preferences.getInt(name, value);
    }
    
    private boolean storeData(String name ,int value){
    	SharedPreferences preferences = getSharedPreferences(STORE_NAME,
				MODE_PRIVATE);
    	Editor editor = preferences.edit();
    	editor.putInt(name, value);
    	return editor.commit();
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;
		
		if (null == mDialog) {
			mDialog = new Dialog(this, R.style.dialog);
			mDialog.setContentView(R.layout.progress_dialog);
		}
		
		LOAD_COUNT = getStoreData("loadcount", -1);
		// ��һ�����룬û�н��й�ͼ�����
		if (-1 == LOAD_COUNT) {
			storeData("loadcount", 0);
			new Thread(new Runnable() {
				@Override
				public void run() {
					//mHandler.sendEmptyMessage(SHOW_DIALOG);
					// �����Ǳ����� ��õ�ʶ���������gridview����ʾ
					//mHandler.sendEmptyMessage(DISMISS_DIALOG);
				}
			}).start();
		}
		
		
		
		mHeadBar = (RelativeLayout)findViewById(R.id.head_bar);
		mHeadBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showScanModePopWin();
			}
		});
		
		mNaviTip = (TextView)findViewById(R.id.navitip);
		
		mRefresh = (ImageButton)findViewById(R.id.refresh);
		mRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(SHOW_DIALOG);
						// �����Ǳ����� ��õ�ʶ���������gridview����ʾ
						mHandler.sendEmptyMessage(DO_SCAN);
						mHandler.sendEmptyMessage(DISMISS_DIALOG);
					}
				}).start();
				//mHandler.sendEmptyMessage(SHOW_DIALOG);
				//getSDImages();
				//mAdapter.notifyDataSetChanged();
				//mHandler.sendEmptyMessage(SHOW_DIALOG);
				//mDialog.show();
				//if( !mDialog.isShowing() ) mDialog.show();
				//mHandler.sendEmptyMessage(DO_SCAN);
				//mHandler.sendEmptyMessage(DISMISS_DIALOG);
			}
		});
		
		mAdapter = new GridAdapter();
		mAdapter.notifyDataSetChanged();
		mGridView = (GridView) findViewById(R.id.img_grid);
		mGridView.setAdapter(mAdapter);
//		mGridView.setOnScrollListener(this);
		
		int counts = getStoreData("mode", 0);
		doSelect(counts);

	}

//	private String[] imgPath ;
//	private int imgCount = 0;
//	private void getSDImages() {
//		Log.d("coolPhpto", "getSDImages---start--");
//		Log.d("coolPhpto", "getSDImages---SHOW_DIALOG--");
//		if( !mDialog.isShowing() ) mDialog.show();
////		mHandler.sendEmptyMessage(SHOW_DIALOG);
////		mCursor = query(mContext,
////				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
////				MediaStore.Images.Media.DEFAULT_SORT_ORDER);
//		if (mCursor.moveToFirst()) {
//			String[] imgPaths = new String[ mCursor.getCount() ];
//			Log.d("coolPhpto", "getSDImages---getCount--" + mCursor.getCount());
//			FaceDetector fd;
//			FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
//			do {
//				Log.d("coolPhpto", "getSDImages----getString--" + mCursor.getString(1));
//				File file = new File(mCursor.getString(1));
//				if( file.exists() ){
////					Bitmap b = getImageThumbnail( cursor.getString(1) ,200); 
////					Bitmap mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true); 
//					Bitmap mFaceBitmap = getImageThumbnail( mCursor.getString(1) ,200); 
////					b.recycle();
//					mFaceWidth = mFaceBitmap.getWidth();
//					mFaceHeight = mFaceBitmap.getHeight();
//					
//			    	fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);        
//		    		int c = fd.findFaces(mFaceBitmap, faces);
//		    		Log.d("coolPhpto", "getSDImages----findFaces--" + mCursor.getString(1));
//		    		if( c > 0 ){
//		    			imgPaths[imgCount++] = mCursor.getString(1);
//		    		}
//					
//				}
//			} while (mCursor.moveToNext());
//			imgPath = imgPaths;
//		}
//		mHandler.sendEmptyMessage(DISMISS_DIALOG);
//		Log.d("coolPhpto", "getSDImages---end--");
//	}
	
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
    private Bitmap getImageThumbnail(String imagePath, int width,int height) {  
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
        int bew = w / width;  
        int beh = h / height;
        int be = bew > beh ? bew : beh;
        if (be <= 0) {  
        	be = 1;  
        }  
        options.inJustDecodeBounds = false; // ��Ϊ false  
        options.inSampleSize = be;  
        
        // ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  
	
	
	
	class GridAdapter extends BaseAdapter {
		private List<String> imgPath = new ArrayList<String>();
		
		public void setImgPath(List<String> img){
			imgPath = img;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
//			return imgCount;
			return imgPath.size();
		}

		@Override
		public Object getItem(int position) {
			return imgPath.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		ImageView mImages;
		@Override
		public View getView(int position, View contenView, ViewGroup viewGroup) {
			if (null == contenView) {
				contenView = (RelativeLayout) LayoutInflater.from(mContext)
						.inflate(R.layout.grid_item2, null);
				mImages = (ImageView) contenView
						.findViewById(R.id.image);
				contenView.setTag(mImages);
			}
//			if(!mBusy){
//				File file = new File( imgPath.get(position) );
//				if (file.exists()) {
//					Bitmap bm = getImageThumbnail( imgPath.get(position) ,150);
////					 ��ͼƬ��ʾ��ImageView��
//					mImages.setImageBitmap(bm);
//				}
//				contenView.setTag(null);
//			}
//			else{
//				contenView.setTag(mImages);
//			}
			
			ImageView mImages = (ImageView) contenView.getTag();
			
			File file = new File( imgPath.get(position) );
			if (file.exists()) {
				Bitmap bm = getImageThumbnail( imgPath.get(position) ,150,150);
				// ��ͼƬ��ʾ��ImageView��
				mImages.setImageBitmap(bm);
			}
			
			return contenView;
		}

	}

}
