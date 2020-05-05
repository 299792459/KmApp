package km.cn.kmapp.util;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetConnectUtils {

    /**
    //单例模式
    //创建 SingleObject 的一个对象
    private static NetConnectUtils instance = new NetConnectUtils();

    //让构造函数为 private，这样该类就不会被实例化
    private NetConnectUtils(){}

    //获取唯一可用的对象
    public static NetConnectUtils getInstance(){
        return instance;
    }**/

    String baseURL="http://49.233.214.213:8090";
    BufferedReader reader = null;
    String result = null;
    HttpURLConnection conn;
    OutputStream dataout;

    //返回值
    myResult mr=new myResult();
    //标志位
    int isok=0;

    //定长为2的线程池
    //ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);


    //使用handle通信，等待网络相关线程执行完毕，再把得到的数据返回
    //这个方案暂时被更低级但是有效的循环等待方法替代
/**
    public myResult urlGet(final String api) throws IOException {

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String val = data.getString("value");
                if(val.equals("ok")){

                }
            }
        };
        return mr;
    }
 **/

    public myResult urlGet(final String api) throws IOException {

        /**
        Thread myThread = new Thread(new Runnable() {
                    //通过线程执行连接操作
                    //降低系统负荷
                    @Override
                    public void run() {
                    **/
                        //打开连接
                        try {
                            Log.v("debug", api);
                            conn = (HttpURLConnection) new URL(baseURL + api).openConnection();
                            conn.setReadTimeout(5000);
                            conn.setConnectTimeout(10000);
                            conn.setRequestMethod("GET");// 默认GET请求
                            conn.connect();// 建立TCP连接
                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                result = TextConvertUtils.inputstreamToString(conn.getInputStream());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        } finally {
                            //关闭网络连接
                            conn.disconnect();
                        }
                        Log.v("debug", "网络连接关闭");
                        //将结果转成json格式
        try {
            JSONObject jsonresult = JSONObject.parseObject(result);
            Log.v("debug", "jieguo"+jsonresult.toString());

            mr.setStatecode(jsonresult.getInteger("statecode"));
            mr.setMessage(jsonresult.getString("message"));
            mr.setContent(jsonresult.get("content"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //完成以后将标志位置1
            isok=1;
        }


       // myThread.start();
        //循环等待线程结束
        while (isok==0){
            //打印测试
            Log.v("debug", "xxxxxxxxx");
        }
        //myThread.interrupt();
       // myThread.stop();
        //返回信息
        isok=0;
        //测试用，正式发布的时候删掉
        //打印测试
        Log.v("debug", "线程结束");
        return mr;
    }



    public myResult urlPost(final String api, final String param) throws IOException {
        new Thread(new Runnable() {
                    //通过线程执行连接操作
                    //降低系统负荷
                    @Override
                    public void run() {
                        //打开连接
                        try {
                            conn = (HttpURLConnection) new URL(baseURL + api).openConnection();
                            conn.setReadTimeout(5000);
                            conn.setConnectTimeout(10000);

                            conn.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
                            conn.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
                            conn.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
                            conn.setUseCaches(false);// post请求不能使用缓存设为false
                            conn.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
                            conn.setRequestProperty("connection", "Keep-Alive");// 连接复用
                            conn.setRequestProperty("charset", "utf-8");
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要

                            dataout = new DataOutputStream(conn.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数

                            String Nparam=param;
                            dataout.write(Nparam.getBytes());
                            dataout.flush();
                            dataout.close();

                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                //reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));// 发送http请求
                                // 读取流
                                result = TextConvertUtils.inputstreamToString(conn.getInputStream());
                                System.out.println(result);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        } finally {
                            //关闭网络连接
                            conn.disconnect();
                        }
                        //将结果转成json格式
                        JSONObject jsonresult = JSONObject.parseObject(result);
                        mr = JSON.toJavaObject(jsonresult,myResult.class);
                        //完成以后将标志位置1
                        isok=1;
                    }}).start();
        //循环等待线程结束
        while (isok==0){
            //测试用，正式发布的时候删掉
            //打印测试
            //Log.v("debug", "等待中");
        }
        //返回信息
        isok=0;
        return mr;

    }



}

