package hitrash.jumismo.android.uoc.edu.hitrash.Utils;

import android.os.Looper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class AsyncHttpUtils {
    //private static final String BASE_URL = "http://192.168.1.141:8080/api/";
    private static final String BASE_URL = "http://sonny.titaniumsystem.es/hitrash/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    // Método para realizar una petición get al servidor
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setMaxRetriesAndTimeout(1, 600);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Método para realizar una petición post al servidor
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setMaxRetriesAndTimeout(1, 600);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    // Método para realizar una petición put al servidor
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setMaxRetriesAndTimeout(1, 600);
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    // Método para obtener la url completa a la API del servidor
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
