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
import km.cn.kmapp.dao.dto.indexMainInfoDTO;
import km.cn.kmapp.dao.dto.sceneryDiscationDTO;
import km.cn.kmapp.dao.entity.comment;
import km.cn.kmapp.dao.entity.letter;
import km.cn.kmapp.dao.entity.reply;
import km.cn.kmapp.dao.entity.scenery;
import km.cn.kmapp.util.NetConnectUtils;
import km.cn.kmapp.util.myResult;
import km.cn.kmapp.util.timeUtil;
import km.cn.kmapp.util.tips;


public class SceneryActivity extends Activity {

    //mystar页面控件
    private Button bookbutton;
    private Button commentbutton;
    private Button getmorereplybutton;

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


    //景点控件
    private ImageView sceneryimageView1;
    private TextView scenerynameTV1;
    //private TextView scenerylocationTV1;

    //用户控件
    private ImageView userimageView1;
    private TextView usernameTV1;
    private TextView usercommentTV1;
    private TextView usercommenttimeTV1;

    //用户控件
    private ImageView userimageView2;
    private TextView usernameTV2;
    private TextView replycontentTV;
    private TextView replytimeTV2;
    private TextView replyusernameTV;

    //单例设计模式获取
    NetConnectUtils ncuinstance= new NetConnectUtils();
    //返回值
    myResult NMR=new myResult();
    //DTO
    sceneryDiscationDTO sceneryDiscationDTO = new sceneryDiscationDTO();

    //网络地址
    String apis="";
    //标志位
    int isok=0;
    //地址
    String location="";
    //景点id
    int mysceneryid=0;
    //评论id
    int mycommentid=0;
    //类标志位
    int actpage=2;
    //页面中传递的值
    String userinfojson="";
    String sceneryinfojson="";
    //int Osceneryid=0;
    int Ouserid=0;
    int Ocommenid=0;


    String sctip="发表成功";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenery);

        //接受传递过来的数据
        userinfojson = this.getIntent().getStringExtra("userinfojson");
        mysceneryid = this.getIntent().getIntExtra("sceneryid",1);
        sceneryinfojson = this.getIntent().getStringExtra("sceneryinfojson");
        Ouserid = this.getIntent().getIntExtra("userid",1);

        // 在这里写页面创建时后做的事

        bookbutton=(Button)findViewById(R.id.bookbutton);
        commentbutton=(Button)findViewById(R.id.commentbutton);
        getmorereplybutton=(Button)findViewById(R.id.getmorereplybutton);


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


        //景点控件
        sceneryimageView1=(ImageView)findViewById(R.id.sceneryimageView1);
        scenerynameTV1=(TextView)findViewById(R.id.scenerynameTV1);
        //scenerylocationTV1=(TextView)findViewById(R.id.scenerylocationTV1);


        //用户控件
        userimageView1=(ImageView)findViewById(R.id.userimageView1);
        usernameTV1=(TextView)findViewById(R.id.usernameTV1);
        usercommentTV1=(TextView)findViewById(R.id.usercommentTV1);
        usercommenttimeTV1=(TextView)findViewById(R.id.usercommenttimeTV1);

        //处理信息
        //景点
        try{
            JSONObject SJ=JSONObject.parseObject(sceneryinfojson);
            scenerynameTV1.setText(SJ.getString("sceneryname"));
        }catch (Exception e){
            e.printStackTrace();
        }
        // 先拼接api
        apis="/scenery/getdiscation/page?sceneryid="+mysceneryid+"&pagesize=1&pagecurrent=1";
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                // 设置接受类，打开网络连接调用
                try {
                    //打印测试
                    Log.v("debug", "正在调用接口");
                    Log.v("debug", mysceneryid+"####");
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


        getmorereplybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-6-1:设置一个Intent
                Intent addIntent=new Intent("action_talking");
                //跳转到回复详情的时候，传递评论id，用户信息
                addIntent.putExtra("apiid",mycommentid);
                addIntent.putExtra("talktype","comment");
                addIntent.putExtra("userinfojson",userinfojson);
                addIntent.putExtra("userid",Ouserid);
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
                //Intent addIntent=new Intent("action_scenery");
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                //startActivityForResult(addIntent, 1);
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
                apis="/scenery/getdiscation/page?sceneryid="+mysceneryid+"&pagesize=1&pagecurrent="+pagecurrent;
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
                apis="/scenery/getdiscation/page?sceneryid="+mysceneryid+"&pagesize=1&pagecurrent="+pagecurrent;
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
                apis="/scenery/getdiscation/page?sceneryid="+mysceneryid+"&pagesize=1&pagecurrent="+pagecurrent;
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


        //注册按钮点击绑定监听器
        bookbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // 先拼接api
                apis="/scenery/bookscnenery/?sceneryid="+mysceneryid+"&userid="+Ouserid;
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
                //弹窗提示，预约信息
                tips.myalert(NMR.getMessage(),SceneryActivity.this);
            }
        });
        //发表评论按钮点击绑定监听器
        commentbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳转到评论界面
                AlertDialog.Builder builder = new AlertDialog.Builder(SceneryActivity.this);
                builder.setTitle("请输入您想说的话");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(SceneryActivity.this).inflate(R.layout.mydialog, null);
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
                        final comment Ncomment=new comment();
                        Ncomment.setCommentcontent(lettercontent);
                        Ncomment.setSceneryid(mysceneryid);
                        Ncomment.setUserid(Ouserid);

                        //处理返回数据
                        JSONObject uinfojson=JSONObject.parseObject(userinfojson);
                        JSONObject sinfojson=JSONObject.parseObject(sceneryinfojson);
                        if(uinfojson==null||sinfojson==null){
                            return;
                        }
                        Ncomment.setSceneryname(sinfojson.getString("sceneryname"));
                        Ncomment.setUserannoyname(uinfojson.getString("userannoyname"));
                        Ncomment.setCommenttime(new timeUtil().getNowTime());
                        apis="/scenery/commentscenery";
                        //新开线程调用网络服务
                        new AsyncTask(){
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                // 设置接受类，打开网络连接调用
                                try {
                                    if(ncuinstance.urlPost(apis,JSONObject.toJSONString(Ncomment))==null){
                                        sctip="发表失败";
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
                        Toast.makeText(SceneryActivity.this, sctip , Toast.LENGTH_SHORT).show();
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
            tips.myalert("对不起，网络连接失败",SceneryActivity.this);
        }
        //打印测试
        Log.v("debug", "准备处理数据");
        //如果连接成功，用json对象接受返回的数据
        JSONObject maininfojson=(JSONObject)NMR.getContent();
        //打印测试
        //Log.v("debug", maininfojson.toString());
        //解析json对象，取出数据放入内存
        //利用反射，将jsonobject映射成java类
        sceneryDiscationDTO = JSON.toJavaObject(maininfojson,sceneryDiscationDTO.class);
        //打印测试
        Log.v("debug", sceneryDiscationDTO.toString());
    }



    public void handleinfo(){
        //将获得到的信息写入控件


        //评论
        comment c1=sceneryDiscationDTO.getCommentList().get(0);
        if(c1==null){
            return;
        }
        mycommentid=c1.getSceneryid();
        //图片
        //sceneryimageView1.setImageDrawable(LoadImageFromWebOperations(s1.getImgurl()));
        //名称
        usernameTV1.setText(c1.getUserannoyname());
        //地址
        usercommentTV1.setText(c1.getCommentcontent());
        //介绍
        usercommenttimeTV1.setText(c1.getCommenttime());

        //回复
        List<reply> listr1=sceneryDiscationDTO.getReplyListMap().get(String.valueOf(c1.getCommentid()));
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
