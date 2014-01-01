package dreamers.soft;

import android.os.Bundle;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;

/**
 * 
 */
public class SimpleRenrenAuthListener implements RenrenAuthListener {

    private ActivityRenren example;

    private String title;

    public SimpleRenrenAuthListener(ActivityRenren example, String title) {
        this.example = example;
        this.title = title;
    }

    @Override
    public void onCancelAuth(Bundle values) {
        Util.showAlert(example, title, "CancelAuth");
    }

    @Override
    public void onCancelLogin() {
        Util.showAlert(example, title, "CancelLogin");
    }

    @Override
    public void onComplete(Bundle values) {
        this.example.login.updateButtonImage();
        Util.showAlert(example, title, "Complete");
    }

    @Override
    public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
        String msg = String.format("RenrenAuthError:(%s)\n%s", renrenAuthError.getError(),
                renrenAuthError.getErrorDescription());
        Util.showAlert(example, title, msg);
    }
}
