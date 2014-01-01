package dreamers.soft;

import android.app.ProgressDialog;

import com.renren.api.connect.android.RequestListener;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;

/**
 * 
 */
public class SimpleRequestListener implements RequestListener {
	private ActivityRenren example;
	private ProgressDialog progress;
	private Thread uiThread;

	public String curText = null;
	
	SimpleRequestListener(ActivityRenren example) {
		this.example = example;
		uiThread = Thread.currentThread();
	}

	void showProgress(String title) {
		progress = ProgressDialog.show(example, title, "Loading...");
		progress.show();
	}

	@Override
	public void onComplete(final String response) {
		this.updateDisplay("Response Complete", response);
	}

	@Override
	public void onFault(final Throwable fault) {
		fault.printStackTrace();
		this.updateDisplay("Fault", fault.toString());
	}

	@Override
	public void onRenrenError(final RenrenError e) {
		e.printStackTrace();
		this.updateDisplay("RenrenError", e.toString());
	}

	void updateDisplay(final String title, final String text) {
		if (Thread.currentThread() == this.uiThread) {
			this.showResult(title, text);
		} else {
			example.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showResult(title, text);
				}
			});
		}
		
		if(text!=null)
		{
			curText = text;
		}
	}

	private void showResult(final String title, final String text) {
		if (progress != null)
			progress.dismiss();
		Util.showAlert(example, title, text);
	}
}