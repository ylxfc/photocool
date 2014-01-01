
package dreamers.soft;

import dreamers.soft.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class AutoPlayActivity extends Activity implements OnLongClickListener {

    private MyViewFlipper mFlipper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        for(int i = 0 ; i< 2 ;i++)
        { 
        	Toast.makeText(this, "长按图片选择切换特效", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.auto_prebiew);

        mFlipper = (MyViewFlipper)findViewById(R.id.flipper);
        
        Intent intent = getIntent();
        mFlipper._init(this, intent.getIntExtra("picIndex", 0),
                intent.getStringArrayListExtra("picList"));
        
       // mFlipper.startFlipping();
        
        setupView();
    }

    private void setupView() {
        mFlipper.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                // TODO Auto-generated method stub
                menu.setHeaderTitle("选择切换特效");
                // 多维空间
                menu.add(0, 0, 0, "多维空间").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        mFlipper.setInAnimation(AutoPlayActivity.this, R.anim.hyperspace_in);
                        mFlipper.setOutAnimation(AutoPlayActivity.this, R.anim.hyperspace_out);
                        mFlipper.startFlipping();
                        
                        return false;
                        
                    }
                });
                //向上
                menu.add(0, 1, 1, "从下到上").setOnMenuItemClickListener(
                        new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                mFlipper.setInAnimation(AutoPlayActivity.this, R.anim.push_up_in);
                                mFlipper.setOutAnimation(AutoPlayActivity.this, R.anim.push_up_out);
                            	
                                mFlipper.startFlipping();
                                return false;
                            }
                        });
                //向下
                menu.add(0, 2, 2, "从上到下").setOnMenuItemClickListener(
                        new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                mFlipper.setInAnimation(AutoPlayActivity.this, R.anim.push_down_in);
                                mFlipper.setOutAnimation(AutoPlayActivity.this, R.anim.push_down_out);
                                mFlipper.startFlipping();
                                return false;
                            }
                        });
                //向左
                menu.add(0, 3, 3, "从右到左").setOnMenuItemClickListener(
                        new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                mFlipper.setInAnimation(AutoPlayActivity.this, R.anim.push_left_in);
                                mFlipper.setOutAnimation(AutoPlayActivity.this, R.anim.push_left_out);
                                mFlipper.startFlipping();
                                return false;
                            }
                        });
                //向右
                menu.add(0, 4, 4, "从左到右").setOnMenuItemClickListener(
                        new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                mFlipper.setInAnimation(AutoPlayActivity.this, R.anim.push_right_in);
                                mFlipper.setOutAnimation(AutoPlayActivity.this, R.anim.push_right_out);
                                mFlipper.startFlipping();
                                return false;
                            }
                        });
              //  淡入淡出
                menu.add(0, 5, 5, "淡入淡出").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        mFlipper.setInAnimation(AutoPlayActivity.this, android.R.anim.fade_in);
                        mFlipper.setOutAnimation(AutoPlayActivity.this, android.R.anim.fade_out);
                        mFlipper.startFlipping();
                        return false;
                        
                    }
                });
                //放缩
                menu.add(0, 6, 6, "放缩变换").setOnMenuItemClickListener(
                        new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                mFlipper.setInAnimation(AutoPlayActivity.this, R.anim.zoom_enter);
                                mFlipper.setOutAnimation(AutoPlayActivity.this, R.anim.zoom_exit);
                                mFlipper.startFlipping();
                                return false;
                            }
                        });
             
                menu.add(0, 7, 7, "取消特效").setOnMenuItemClickListener(
                        new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                mFlipper.setInAnimation(null);
                                mFlipper.setOutAnimation(null);
                                mFlipper.startFlipping();
                                return false;
                            }
                        });
            }
        });

    }

    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        mFlipper.stopFlipping();
        return false;
    }

}
