package km.cn.kmapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class getimage extends AsyncTask {

    public String urlpath="";
    Bitmap bitmap;
    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            //把传过来的路径转成URL
            URL url = new URL(urlpath);
            //获取连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //使用GET方法访问网络
            connection.setRequestMethod("GET");
            //超时时间为10秒
            connection.setConnectTimeout(10000);
            //获取返回码
            int code = 0;
            code = connection.getResponseCode();
            if (code == 200) {
                InputStream inputStream = connection.getInputStream();
                //使用工厂把网络的输入流生产Bitmap
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
