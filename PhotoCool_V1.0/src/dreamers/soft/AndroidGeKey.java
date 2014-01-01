package dreamers.soft;

import weibo4android.OAuthConstant;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AndroidGeKey extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(dreamers.soft.R.layout.share_main);
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	Button beginOuathBtn=  (Button) findViewById(dreamers.soft.R.id.Button_load);
    	

    	beginOuathBtn.setOnClickListener(new Button.OnClickListener()
        {

            public void onClick( View v )
            {
            	Weibo weibo = OAuthConstant.getInstance().getWeibo();
            	RequestToken requestToken;
				try {
					requestToken =weibo.getOAuthRequestToken("weibo4android://OAuthActivity");
	    			Uri uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
	    			OAuthConstant.getInstance().setRequestToken(requestToken);
	    			startActivity(new Intent(Intent.ACTION_VIEW, uri));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
            }
        } );
	}
}