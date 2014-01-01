package dreamers.soft;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dreamers.soft.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
//定义一个类，使其实现拍照并保存功能
public class ActivityTakePic extends Activity {
	//定义类的成员变量
	private SurfaceView surfaceView;
	private ImageButton takePic;
	private ImageButton back;
	private Camera camera;
	private boolean isPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.camera);

		surfaceView = (SurfaceView) findViewById(R.id.camera_surface);

		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().addCallback(new MyHolder());

		takePic = (ImageButton) findViewById(R.id.take_picture);//拍照按钮
		back = (ImageButton) findViewById(R.id.back);//返回按钮

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTakePic.this.finish();//定义返回按钮的监听函数
				//关闭当前拍照页面，返回主界面
			}
		});

		takePic.setOnClickListener(new OnClickListener() {
			//定义拍照函数的监听函数
			@Override
			public void onClick(View v) {
				if (isPreview) {
					camera.takePicture(null, null, new PictureCallback() {

						@Override
						public void onPictureTaken(byte[] data, Camera camera) {
							try {
								takePic.setClickable(false);
								back.setClickable(false);
								Bitmap bitMap_ro = BitmapFactory
										.decodeByteArray(data, 0, data.length);

								
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

								String mstrTitle = " ";//设置水印
								Bitmap bitMap = Bitmap.createBitmap(bitMap_ro.getWidth(), bitMap_ro.getHeight(), Bitmap.Config.RGB_565);
								Canvas canvasTemp = new Canvas(bitMap);
								canvasTemp.drawColor(Color.WHITE);
								Paint p = new Paint();
								String familyName = "微软雅黑";//设置水印字体
								Typeface font = Typeface.create(familyName,
										Typeface.BOLD);
								p.setColor(Color.BLUE);//设置水印字体颜色
								p.setTypeface(font);
								p.setTextSize(30);
								canvasTemp.drawBitmap(bitMap_ro, 0, 0, p);				
								canvasTemp.drawText(mstrTitle,bitMap_ro.getWidth()-110,bitMap_ro.getHeight()-40,p);
							//添加水印文字
								bitMap.compress(CompressFormat.JPEG, 75,
										outStream);

								outStream.close();
								camera.stopPreview();
								Toast.makeText(ActivityTakePic.this,
										"图片已保存至" + folder.getAbsolutePath(),
										Toast.LENGTH_SHORT).show();//打印保存信息
								camera.startPreview();

							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								takePic.setClickable(true);
								back.setClickable(true);
							}

						}
					});
				}

			}
		});

	}

	class MyHolder implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (!isPreview) {
				camera = Camera.open();//�����
				Camera.Parameters parameter = camera.getParameters();
				camera.setParameters(parameter);
				try {
					camera.setPreviewDisplay(holder);
					camera.startPreview();
					isPreview = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (isPreview) {
				isPreview = false;
				camera.stopPreview();
				camera.release();
			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
