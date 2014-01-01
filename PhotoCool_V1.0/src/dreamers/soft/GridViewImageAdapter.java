package dreamers.soft;

import java.util.List;

import dreamers.soft.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GridViewImageAdapter extends BaseAdapter {

	private Context ctx;
	public List<String> items;
	public GridViewImageAdapter(Context ctx, List<String> items) {
		this.ctx = ctx;
		this.items = items;
	}
	
	public int getCount() {
		return items.size();//得到list里面数据的个数
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		 //针对每一个数据（即每一个图片ID）创建一个ImageView实例
		ImageView iv = new ImageView(ctx);//针对外面传递过来的Context变量，
		
		String path = items.get(position);
		
		Bitmap bm = new BitmapTool().charge(path, 80, 80);
		
		if(bm != null) {					
			iv.setAdjustViewBounds(true);	
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			iv.setAdjustViewBounds(true);
			iv.setLayoutParams(new Gallery.LayoutParams(100, 80));
			iv.setImageBitmap(bm);
		} else {
			//加载需要的图片
			Bitmap bma = BitmapFactory.decodeResource(ctx.getResources(), 
				R.drawable.icon);
			bma = new BitmapTool().zoomImage(bma, 80, 80); 
			iv.setAdjustViewBounds(true);
			iv.setImageBitmap(bma);
		}
		return iv;
	}

}
