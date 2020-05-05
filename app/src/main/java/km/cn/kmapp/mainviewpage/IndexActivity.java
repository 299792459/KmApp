package km.cn.kmapp.mainviewpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import km.cn.kmapp.R;
import km.cn.kmapp.dao.dto.indexMainInfoDTO;
import km.cn.kmapp.dao.entity.comment;
import km.cn.kmapp.dao.entity.scenery;
import km.cn.kmapp.util.NetConnectUtils;
import km.cn.kmapp.util.getimage;
import km.cn.kmapp.util.myResult;
import km.cn.kmapp.util.tips;


//地区广场页面
public class IndexActivity extends Activity {

    private ScrollView sv;

    //分页控件
    int pagecurrent=1;
    private Button leftbutton;
    private EditText pagecurrentET;
    private Button rightbutton;
    private Button jumpbutton;
    //菜单控件
    private Button indexbutton;
    private Button scenerybutton;
    private Button personalzonebutton;

    //跳转控件
    private Button getmoresceneryinfobutton;
    private Button getmoretalkbutton;

    //景点控件
    private ImageView sceneryimageView1;
    private TextView scenerynameTV1;
    private TextView scenerylocationTV1;
    private TextView scenerydescriptionTV1;

    //用户控件
    private ImageView userimageView1;
    private TextView usernameTV1;
    private TextView usercommentTV1;
    private TextView usercommenttimeTV1;

    //单例设计模式获取
    NetConnectUtils ncuinstance= new NetConnectUtils();
    //返回值
    myResult NMR=new myResult();
    //网络地址
    String apis="";
    //标志位
    int isok=0;
    //地址
    String location="";
    //景点信息
    String sceneryinfojson="";
    //景点id
    int mysceneryid=0;
    //评论id
    int mycommentid=0;
    //userid
    int Ouserid=0;
    //pzuserid
    int pzuserid=0;
    //userinfo
    String userinfojson="";

    //返回信息
    indexMainInfoDTO mainInfoDTO=new indexMainInfoDTO();

    //定长为2的线程池
    //ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        //1.首先接受从登录页传过来的值
        location=getIntent().getStringExtra("location");
        Ouserid=getIntent().getIntExtra("userid",1);
        userinfojson=getIntent().getStringExtra("userinfojson");


/**
        Handler updateBarHandler =new Handler();
        updateBarHandler.post(Runnable Thread);

        //移除handler里的任务线程,调用线程的stop()方法，销毁线程。
        updateBarHandler.removecallbacks(Runnable Thread);
**/
        //创建页面
        //sv = (ScrollView)findViewById(R.id.sv);
        //sv.addView(findViewById(R.id.svc));
        //菜单
        indexbutton=(Button)findViewById(R.id.indexbutton);
        scenerybutton=(Button)findViewById(R.id.scenerybutton);
        personalzonebutton=(Button)findViewById(R.id.personalzonebutton);

        //分页
        leftbutton=(Button)findViewById(R.id.leftbutton);
        rightbutton=(Button)findViewById(R.id.rightbutton);
        pagecurrentET=(EditText)findViewById(R.id.pagecurrent);
        jumpbutton=(Button)findViewById(R.id.jumpbutton);

        //跳转控件
        getmoresceneryinfobutton=(Button)findViewById(R.id.getmoresceneryinfobutton);
        getmoretalkbutton=(Button)findViewById(R.id.getmoretalkbutton);

        //景点控件
        sceneryimageView1=(ImageView)findViewById(R.id.sceneryimageView1);

        scenerynameTV1=(TextView)findViewById(R.id.scenerynameTV1);
        scenerylocationTV1=(TextView)findViewById(R.id.scenerylocationTV1);
        scenerydescriptionTV1=(TextView)findViewById(R.id.scenerydescriptionTV1);


        //用户控件
        userimageView1=(ImageView)findViewById(R.id.userimageView1);

        usernameTV1=(TextView)findViewById(R.id.usernameTV1);
        usercommentTV1=(TextView)findViewById(R.id.usercommentTV1);
        usercommenttimeTV1=(TextView)findViewById(R.id.usercommenttimeTV1);


        //跳转按钮点击
        getmoresceneryinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_scenery");
                //跳转到景点的时候，附带传递景点id
                addIntent.putExtra("sceneryid",mysceneryid);
                //跳转到景点的时候，附带传递景点id
                addIntent.putExtra("sceneryinfojson",sceneryinfojson);
                //用户信息
                //传递userjson
                addIntent.putExtra("userinfojson",userinfojson);
                //传递userid
                addIntent.putExtra("userid",Ouserid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });
        getmoretalkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_talking");
                //跳转到更多评论回复的时候
                addIntent.putExtra("apiid",mycommentid);
                addIntent.putExtra("talktype","comment");
                //用户信息
                //传递userjson
                addIntent.putExtra("userinfojson",userinfojson);
                //传递userid
                addIntent.putExtra("userid",Ouserid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });

        //点击用户名称跳转到详情页
        usernameTV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置一个Intent
                Intent addIntent=new Intent("action_personalzone");
                //用户信息
                //传递userjson
                addIntent.putExtra("userinfojson",userinfojson);
                //传递userid
                addIntent.putExtra("userid",Ouserid);
                //传递pzuserid
                addIntent.putExtra("pzuserid",pzuserid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }

        });


        //菜单按钮点击
        indexbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                //Intent addIntent=new Intent("action_index");
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                //startActivityForResult(addIntent, 1);
            }
        });

        //景点按钮点击
        scenerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_scenery");
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });
        //个人按钮点击
        personalzonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_personalzone");
                //跳转到个人页的时候，附带传递userinfo
                addIntent.putExtra("userinfojson",userinfojson);
                addIntent.putExtra("userid",Ouserid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });

        //分页按钮点击
        //上一页
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagecurrent--;
                pagecurrentET.setText(pagecurrent+"");
                // 先拼接api
                apis="/index/getMainInfo/page?location="+location+"&pagesize=1&pagecurrent="+pagecurrent;
                //网络连接


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置接受类，打开网络连接调用
                        try {
                            //打印测试
                            Log.v("debug", "正在调用接口");
                            //打印测试
                            Log.v("debug", apis);
                            NMR =  ncuinstance.urlGet(apis);
                            //打印测试
                            Log.v("debug", "接口调用完毕");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isok=1;
                        }
                    }
                }).start();
                //循环等待线程处理完毕
                while (isok==0){}
                isok=0;
                //处理信息
                jsoncasttojavaclass();
                //将获得到的信息写入控件
                handleinfo();
            }
        });
        //下一页
        rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagecurrent++;
                pagecurrentET.setText(pagecurrent+"");
                // 先拼接api
                apis="/index/getMainInfo/page?location="+location+"&pagesize=1&pagecurrent="+pagecurrent;
                //网络连接

                new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        // 设置接受类，打开网络连接调用
                        try {
                            //打印测试
                            Log.v("debug", "正在调用接口");
                            //打印测试
                            Log.v("debug", apis);
                            NMR =  ncuinstance.urlGet(apis);
                            //打印测试
                            Log.v("debug", "接口调用完毕");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isok=1;
                        }
                        return null;
                    }
                }.execute();
                /**
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置接受类，打开网络连接调用
                        try {
                            //打印测试
                            Log.v("debug", "正在调用接口");
                            //打印测试
                            Log.v("debug", apis);
                            NMR =  ncuinstance.urlGet(apis);
                            //打印测试
                            Log.v("debug", "接口调用完毕");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isok=1;
                        }
                    }
                }).start();**/
                //循环等待线程处理完毕
                while (isok==0){}
                isok=0;
                //castjson
                jsoncasttojavaclass();
                //将获得到的信息写入控件
                handleinfo();
            }
        });
        //跳转
        jumpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagecurrent=Integer.parseInt(pagecurrentET.getText().toString());
                // 先拼接api
                apis="/index/getMainInfo/page?location="+location+"&pagesize=1&pagecurrent="+pagecurrent;
                //网络连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置接受类，打开网络连接调用
                        try {
                            //打印测试
                            Log.v("debug", "正在调用接口");
                            //打印测试
                            Log.v("debug", apis);
                            NMR =  ncuinstance.urlGet(apis);
                            //打印测试
                            Log.v("debug", "接口调用完毕");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isok=1;
                        }
                    }
                }).start();
                //循环等待线程处理完毕
                while (isok==0){}
                isok=0;
                //castjson
                jsoncasttojavaclass();
                //将获得到的信息写入控件
                handleinfo();
            }
        });



        // 通过传过来的地点去查询地区主要信息
        // 先拼接api
        apis="/index/getMainInfo/page?location="+location+"&pagesize=1&pagecurrent="+pagecurrent;
        //打印测试
        Log.v("debug", apis);
        //网络连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 设置接受类，打开网络连接调用
                try {
                    //打印测试
                    Log.v("debug", "正在调用接口");
                    //打印测试
                    Log.v("debug", apis);
                    NMR =  ncuinstance.urlGet(apis);
                    //打印测试
                    Log.v("debug", "接口调用完毕");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    isok=1;
                }
            }
        }).start();
        //循环等待线程处理完毕
        while (isok==0){
            //打印测试
            Log.v("debug", "等待线程返回");
        }
        isok=0;
        //castjson
        jsoncasttojavaclass();
        //将获得到的信息写入控件
        handleinfo();
    }

    public void jsoncasttojavaclass(){
        //如果连接失败
        if(NMR==null){
            //弹窗提示
            tips.myalert("对不起，网络连接失败",IndexActivity.this);
        }
        //打印测试
        Log.v("debug", "准备处理数据");
        //如果连接成功，用json对象接受返回的数据
        JSONObject maininfojson=(JSONObject)NMR.getContent();


        //打印测试
        //Log.v("debug", maininfojson.toString());
        //解析json对象，取出数据放入内存
        mainInfoDTO = new indexMainInfoDTO();
        //利用反射，将jsonobject映射成java类
        mainInfoDTO = JSON.toJavaObject(maininfojson,indexMainInfoDTO.class);
        //打印测试
        Log.v("debug", mainInfoDTO.toString());
    }



    public void handleinfo(){
        //将获得到的信息写入控件
        //景点
        scenery s1=mainInfoDTO.getSceneryList().get(0);
        //将信息备份一份json形式用于界面中传递
        sceneryinfojson=JSON.toJSONString(s1);

        mysceneryid=s1.getSceneryid();
        //图片
        sceneryimageView1.setImageDrawable(LoadImageFromWebOperations(s1.getImgurl()));
        //名称
        scenerynameTV1.setText(s1.getSceneryname());
        //地址
        scenerylocationTV1.setText(s1.getLocation());
        //介绍
        scenerydescriptionTV1.setText(s1.getScenerydescription());

        //评论
        List<comment> listc1=mainInfoDTO.getCommentListMap().get(String.valueOf(s1.getSceneryid()));
        mycommentid=listc1.get(0).getCommentid();
        //头像
        //userimageView1.setImageDrawable(LoadImageFromWebOperations());
        //昵称
        usernameTV1.setText(listc1.get(0).getUserannoyname());
        //内容
        usercommentTV1.setText(listc1.get(0).getCommentcontent());
        //时间
        usercommenttimeTV1.setText(listc1.get(0).getCommenttime());
        pzuserid=listc1.get(0).getUserid();

    }


    Drawable LoadImageFromWebOperations(String url) {

        //打印测试
        Log.v("debug","获取图片中");
        final String urlpath=url;
        new AsyncTask(){
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
                isok=1;
                return null;
            }
        }.execute();
        while (isok==0){}
        isok=0;
        //打印测试
        Log.v("debug","以获取图片");
        return new BitmapDrawable(bitmap);

    }

    //从网络获取图片
    Bitmap bitmap;

}
