package dreamers.soft;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import dreamers.soft.R;

public class MyViewFlipper extends ViewFlipper{

    private static final String TAG = "MyViewFlipper";
    private int mCurPicIndex = 0;
    private ArrayList<String> mPicList ;
    private int mPicCount ;
    
    public MyViewFlipper(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
        
    }

    public MyViewFlipper(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setAnimation(Context context){
        setInAnimation(context, R.anim.push_up_in);
        setOutAnimation(context, R.anim.push_up_out);
    }
    
    public void _init(Context context, int index,ArrayList<String> list ){
        mCurPicIndex = index ;
        mPicList = list ;
        mPicCount = list.size();
        
        Log.i(TAG,"________mCurPicIndex :"+index +", mPicCount : "+mPicCount);
        
        setAnimation(context);
        
        firstFillChild();
    }
    
    private void firstFillChild(){
        Log.i(TAG,"_______enter fillChild()____");
        ImageView child = (ImageView)getChildAt(getDisplayedChild());
        child.setImageBitmap(BitmapFactory.decodeFile(mPicList.get(mCurPicIndex++)));
        updateChild();
    }
    
    /* (non-Javadoc)
     * @see android.widget.ViewAnimator#showNext()
     */
    @Override
    public void showNext() {
        // TODO Auto-generated method stub
        super.showNext();
        
        updateChild();
    }

    private  void updateChild(){
        Log.i(TAG,"_______enter updateChild()____");
        postDelayed(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(mCurPicIndex >= mPicCount){
                    //stopFlipping();
                    return;
                }
                ImageView child = (ImageView)getChildAt(1 - getDisplayedChild());
                child.setImageBitmap(BitmapFactory.decodeFile(mPicList.get(mCurPicIndex++)));
               
            }
        }, 1000);
    }

}
