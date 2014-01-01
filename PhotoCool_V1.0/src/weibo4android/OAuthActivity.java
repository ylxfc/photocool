package weibo4android;

import java.util.List;

import dreamers.soft.ActivityMain;
import dreamers.soft.ActivityTakePic;
//import dsp.zte.UpdateStatus;

import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OAuthActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(dreamers.soft.R.layout.timeline);
		Uri uri=this.getIntent().getData();
		try {
			RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
			AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
			TextView textView = (TextView) findViewById(dreamers.soft.R.id.TextView_time);
			textView.setText("授权成功");
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		Button button=  (Button) findViewById(dreamers.soft.R.id.Button_time);
		button.setText("分享照片");
		button.setOnClickListener(new Button.OnClickListener()
        {

            public void onClick( View v )
            {
    				Weibo weibo=OAuthConstant.getInstance().getWeibo();
    				weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
    			
					///////////////////////////////////////////////////////////////////
    			 /* List<Status> friendsTimeline;
    					try {
    							friendsTimeline = weibo.getTrendStatus("seaeast", new Paging(1,20));
							StringBuilder stringBuilder = new StringBuilder("1");
	    					for (Status status : friendsTimeline) {
	    						stringBuilder.append(status.getUser().getScreenName() + "说:"
	    								+ status.getText() + "-------------------------\n");
	    					}
	    					TextView textView = (TextView) findViewById(dsp.zte.R.id.TextView_time);
	    					textView.setText(stringBuilder.toString());
						} catch (WeiboException e) {
							e.printStackTrace();
						}
						
						*/
    					
            }
        } );		
	}
}
