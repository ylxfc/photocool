package dreamers.soft;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.util.Log;

/**
 * 
 */
public final class ExampleUtil {

    public static final String LOG_TAG = "Renren-SDK-example";

    public static void logger(String message) {
        Log.i(LOG_TAG, message);
    }

    public static byte[] getBytes(InputStream is) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int i = 0; (i = is.read(buf)) > 0;) {
                os.write(buf, 0, i);
            }
            os.close();
            return os.toByteArray();
        } catch (Exception e) {
            logger(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
