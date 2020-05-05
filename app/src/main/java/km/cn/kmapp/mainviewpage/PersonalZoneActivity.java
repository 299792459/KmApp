package km.cn.kmapp.mainviewpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import km.cn.kmapp.R;
import km.cn.kmapp.dao.dto.letterDiscationDTO;
import km.cn.kmapp.dao.dto.sceneryDiscationDTO;
import km.cn.kmapp.dao.entity.comment;
import km.cn.kmapp.dao.entity.letter;
import km.cn.kmapp.dao.entity.reply;
import km.cn.kmapp.util.NetConnectUtils;
import km.cn.kmapp.util.myResult;
import km.cn.kmapp.util.timeUtil;
import km.cn.kmapp.util.tips;


public class PersonalZoneActivity extends Activity {

    //mystar页面控件
    private Button operationbutton;
    private Button letterbutton;
    private Button getmoreletterinfobutton;

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


    //用户0
    private ImageView userimageView0;
    private TextView usernameTV0;
    //private TextView scenerylocationTV1;

    //用户控件1
    private ImageView userimageView1;
    private TextView usernameTV1;
    private TextView userletterTV1;
    private TextView lettertimeTV1;

    //用户控件2
    private ImageView userimageView2;
    private TextView replyusernameTV;
    private TextView usernameTV2;
    private TextView replycontentTV;
    private TextView replytimeTV2;


    //单例设计模式获取
    NetConnectUtils ncuinstance= new NetConnectUtils();
    //返回值
    myResult NMR=new myResult();
    //DTO
    letterDiscationDTO myletterDiscationDTO = new letterDiscationDTO();

    //网络地址
    String apis="";
    //标志位
    int isok=0;
    //留言id
    int myletterid=0;
    //类标志位
    int actpage=3;
    //页面中传递的值
    //Ouserid 是自己的id
    String userinfojson="";
    int Ouserid=0;
    int pzuserid=0;
    int Oletterid=0;
    int letteruserid=0;
    int talkmainuserid=0;

    //
    String pztip="发表成功";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalzone);

        //接受传递过来的数据
        userinfojson = this.getIntent().getStringExtra("userinfojson");
        Ouserid=this.getIntent().getIntExtra("userid",0);
        pzuserid = this.getIntent().getIntExtra("pzuserid",1);

        // 在这里写页面创建时后做的事
        operationbutton=(Button)findViewById(R.id.operationbutton);
        letterbutton=(Button)findViewById(R.id.letterbutton);
        getmoreletterinfobutton=(Button)findViewById(R.id.getmoreletterinfobutton);

        //创建页面
        //菜单
        indexbutton=(Button)findViewById(R.id.indexbutton);
        scenerybutton=(Button)findViewById(R.id.scenerybutton);
        personalzonebutton=(Button)findViewById(R.id.personalzonebutton);

        //分页
        leftbutton=(Button)findViewById(R.id.leftbutton);
        rightbutton=(Button)findViewById(R.id.rightbutton);
        pagecurrentET=(EditText)findViewById(R.id.pagecurrent);
        jumpbutton=(Button)findViewById(R.id.jumpbutton);

        //用户控件0
        usernameTV0=(TextView)findViewById(R.id.usernameTV0);

        //用户控件1
        userimageView1=(ImageView)findViewById(R.id.userimageView1);
        usernameTV1=(TextView)findViewById(R.id.usernameTV1);
        userletterTV1=(TextView)findViewById(R.id.userletterTV1);
        lettertimeTV1=(TextView)findViewById(R.id.lettertimeTV1);

        //用户控件2
        userimageView2=(ImageView)findViewById(R.id.userimageView2);
        usernameTV2=(TextView)findViewById(R.id.usernameTV2);
        replyusernameTV=(TextView)findViewById(R.id.replyusernameTV);
        replycontentTV=(TextView)findViewById(R.id.replycontentTV);
        replytimeTV2=(TextView)findViewById(R.id.replytimeTV2);

        //处理信息
        //留言
        try{
            JSONObject SJ=JSONObject.parseObject(userinfojson);
            usernameTV0.setText(SJ.getString("userannoyname"));
        }catch (Exception e){
            e.printStackTrace();
        }

        // 先拼接api
        apis="/pz/getRevicedLetters/page?letteruserid="+pzuserid+"&pagesize=1&pagecurrent=1";
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                // 设置接受类，打开网络连接调用
                try {
                    //打印测试
                    Log.v("debug", "正在调用接口");
                    Log.v("debug", pzuserid+"####");
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
        while (isok==0){}
        isok=0;
        jsoncasttojavaclass();
        handleinfo();


        getmoreletterinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_talking");
                //跳转到回复详情的时候，传递评论id，用户信息
                addIntent.putExtra("apiid",myletterid);
                addIntent.putExtra("talkmainuserid",talkmainuserid);
                addIntent.putExtra("userinfojson",userinfojson);
                addIntent.putExtra("userid",Ouserid);
                addIntent.putExtra("talktype","letter");

                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });

        //点击名称跳转到详情页
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
                addIntent.putExtra("pzuserid",letteruserid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });


        //菜单按钮点击
        indexbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_index");
                addIntent.putExtra("userinfojson",userinfojson);
                addIntent.putExtra("userid",Ouserid);

                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });

        //景点按钮点击
        scenerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_scenery");
                addIntent.putExtra("userinfojson",userinfojson);
                addIntent.putExtra("userid",Ouserid);
                 //3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });
        //个人按钮点击
        personalzonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Ouserid!=pzuserid){
                    return;
                }
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
                apis="/pz/getRevicedLetters/page?letteruserid="+pzuserid+"&pagesize=1&pagecurrent="+pagecurrent;
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
                apis="/pz/getRevicedLetters/page?letteruserid="+pzuserid+"&pagesize=1&pagecurrent="+pagecurrent;
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
                //循环等待线程处理完毕
                while (isok==0){}
                isok=0;
                //处理信息
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
                apis="/pz/getRevicedLetters/page?letteruserid="+pzuserid+"&pagesize=1&pagecurrent="+pagecurrent;
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
                //循环等待线程处理完毕
                while (isok==0){}
                isok=0;
                //处理信息
                jsoncasttojavaclass();
                //将获得到的信息写入控件
                handleinfo();
            }
        });

        //caozuo按钮
        operationbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳转到评论界面

                if(pzuserid!=Ouserid){
                    //如果此界面不是个人主页
                    //则显示

                }

                //评论完以后跳转回来
            }
        });

        //发表评论按钮点击绑定监听器
        letterbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳转到评论界面
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalZoneActivity.this);
                builder.setTitle("请输入您想说的话");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(PersonalZoneActivity.this).inflate(R.layout.mydialog, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);

                final EditText words = (EditText)view.findViewById(R.id.mywords);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String lettercontent = words.getText().toString().trim();
                        // 组装letter值
                        final letter Nletter=new letter();
                        Nletter.setLettercontent(lettercontent);
                        Nletter.setLetteruserid(pzuserid);
                        Nletter.setUserid(Ouserid);
                        Nletter.setTime(new timeUtil().getNowTime());
                        apis="/pz/letteruser";

                        //新开线程调用网络服务
                        new AsyncTask(){
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                // 设置接受类，打开网络连接调用
                                try {
                                    if(ncuinstance.urlPost(apis,JSONObject.toJSONString(Nletter))==null){
                                        pztip="发表失败";
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }finally {
                                    isok=1;
                                }
                                return null;
                            }
                        }.execute();
                        //更新主线程ui
                        //循环等待线程处理完毕
                        while (isok==0){}
                        isok=0;
                        // 用户反馈
                        Toast.makeText(PersonalZoneActivity.this, pztip , Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                builder.show();
            }
        });
    }
    public void jsoncasttojavaclass(){
        //如果连接失败
        if(NMR==null){
            //弹窗提示
            tips.myalert("对不起，网络连接失败",PersonalZoneActivity.this);
        }
        //打印测试
        Log.v("debug", "准备处理数据");
        //如果连接成功，用json对象接受返回的数据
        JSONObject letterDiscationDTOjson=(JSONObject)NMR.getContent();
        //打印测试
        //Log.v("debug", maininfojson.toString());
        //解析json对象，取出数据放入内存
        //利用反射，将jsonobject映射成java类
        myletterDiscationDTO = JSON.toJavaObject(letterDiscationDTOjson,letterDiscationDTO.class);
        //打印测试
        Log.v("debug", myletterDiscationDTO.toString());
    }



    public void handleinfo(){
        if(myletterDiscationDTO==null){
            return;
        }
        //将获得到的信息写入控件
        //评论
        if(myletterDiscationDTO.getLetterList()==null||myletterDiscationDTO.getReplyListMap()==null){
            return;
        }
        try{
            letter l1=myletterDiscationDTO.getLetterList().get(0);
            if(l1==null){
                return;
            }
            myletterid=l1.getLetterid();
            //图片
            //sceneryimageView1.setImageDrawable(LoadImageFromWebOperations(s1.getImgurl()));
            //名称
            usernameTV1.setText(getusernamebyid(l1.getUserid()));
            talkmainuserid=l1.getUserid();
            //地址
            userletterTV1.setText(l1.getLettercontent());
            //介绍
            lettertimeTV1.setText(l1.getTime());

            //回复l
            List<reply> listr1=myletterDiscationDTO.getReplyListMap().get(String.valueOf(l1.getLetterid()));
            if(listr1==null){
                return;
            }
            //myreplyid=listr1.get(0).getReplyid();
            //头像
            //userimageView1.setImageDrawable(LoadImageFromWebOperations());
            //昵称

            usernameTV2.setText(getusernamebyid(listr1.get(0).getUserid()));
            replyusernameTV.setText(getusernamebyid(listr1.get(0).getReplyuserid()));
            //内容
            replycontentTV.setText(listr1.get(0).getReplycontent());
            //时间
            replytimeTV2.setText(listr1.get(0).getReplytime());
            letteruserid=l1.getUserid();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    String getusernamebyid(int userid){
        // 先拼接api
        apis="/pz/getUserNameById"+userid;
        //打开网络连接
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
        //循环等待线程处理完毕
        while (isok==0){}
        isok=0;
        //处理返回数据
        JSONObject usernamejson=(JSONObject)NMR.getContent();
        if(usernamejson==null){
            return "";
        }
        //打印测试
        //Log.v("debug", maininfojson.toString());
        //解析json对象，取出数据放入内存
        //利用反射，将jsonobject映射成java类
        String username = JSON.toJavaObject(usernamejson,String.class);
        return username;
    }



    //从网络获取图片
    Bitmap bitmap;
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
}
