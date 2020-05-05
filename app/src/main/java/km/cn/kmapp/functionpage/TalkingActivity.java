package km.cn.kmapp.functionpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import km.cn.kmapp.R;
import km.cn.kmapp.dao.dto.indexMainInfoDTO;
import km.cn.kmapp.dao.entity.letter;
import km.cn.kmapp.dao.entity.reply;
import km.cn.kmapp.dao.entity.user;
import km.cn.kmapp.mainviewpage.IndexActivity;
import km.cn.kmapp.mainviewpage.PersonalZoneActivity;
import km.cn.kmapp.util.NetConnectUtils;
import km.cn.kmapp.util.myResult;
import km.cn.kmapp.util.timeUtil;
import km.cn.kmapp.util.tips;
/**
 * 当选中某个item的时候，跳出回复弹窗，进行评论
 * 当点击用户名的时候，跳转到用户个人空间
 * **/
public class TalkingActivity extends ListActivity {

    //定义控件
    private ImageView imageViewtalk;
    private TextView usernameTV;
    private TextView replyusernameTV;
    private TextView replytimeTV;
    private TextView replycontentTV;
    private TextView otherTV;

    private Button replybutton;
    private Button refleshbutton;
    //定义全局变量
    String apis="";
    myResult NMR=new myResult();
    int isok=0;
    NetConnectUtils ncuinstance = new NetConnectUtils();
    reply replyinfo=new reply();
    String talktip="发表成功";
    //定义界面传输值
    int Ouserid;
    String userinfojson;
    int focususerid;
    int apiid;
    String talktype;

    int replyuserid=0;
    int userid=0;
    int talkmainuserid;
    /*定义一个动态数组*/
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //打印测试
        Log.v("debug", "dakaitaolunjiemian");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talking);

        //获取页面传输值
        userinfojson = this.getIntent().getStringExtra("userinfojson");
        Ouserid=this.getIntent().getIntExtra("userid",0);
        apiid=this.getIntent().getIntExtra("apiid",0);
        talktype = this.getIntent().getStringExtra("talktype");
        talkmainuserid = this.getIntent().getIntExtra("talkmainuserid",0);
        //功能按钮
        replybutton=(Button)findViewById(R.id.replybutton);
        refleshbutton=(Button)findViewById(R.id.refleshbutton);

        imageViewtalk=(ImageView) findViewById(R.id.imageViewtalk);;
        usernameTV=(TextView) findViewById(R.id.usernameTV);;
        replyusernameTV=(TextView) findViewById(R.id.replyusernameTV);;
        replytimeTV=(TextView) findViewById(R.id.replytimeTV);;
        replycontentTV=(TextView) findViewById(R.id.replycontentTV);;
        otherTV=(TextView) findViewById(R.id.otherTV);;

        refleshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }});
        //打印测试
        Log.v("debug", "shezhiwanbi ");
/**
        //点击名称跳转到详情页
        usernameTV.setOnClickListener(new View.OnClickListener() {
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
                addIntent.putExtra("pzuserid",userid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });
        //点击名称跳转到详情页
        replyusernameTV.setOnClickListener(new View.OnClickListener() {
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
                addIntent.putExtra("pzuserid",replyuserid);
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(addIntent, 1);
            }
        });
**/
        //设置控件数组
        String[] namearr = new String[] {"userimage","username","replyusername", "replytime","other","replycontent"};
        int[] idarr = new int[] {R.id.imageViewtalk,R.id.usernameTV,R.id.replyusernameTV,R.id.replytimeTV,R.id.otherTV,R.id.replycontentTV};



        //按钮点击事件
        replybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到评论界面
                AlertDialog.Builder builder = new AlertDialog.Builder(TalkingActivity.this);
                builder.setTitle("请输入您想说的话");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(TalkingActivity.this).inflate(R.layout.mydialog, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);

                final EditText words = (EditText)view.findViewById(R.id.mywords);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String replycontent = words.getText().toString().trim();
                        // 组装letter值
                        final reply Nreply=new reply();
                        Nreply.setReplycontent(replycontent);
                        Nreply.setReplytype(talktype);
                        Nreply.setUserid(Ouserid);
                        Nreply.setReplyuserid(talkmainuserid);
                        Nreply.setReplycommentid(apiid);
                        Nreply.setReplytime(new timeUtil().getNowTime());
                        apis="/talk/reply";

                        //新开线程调用网络服务
                        new AsyncTask(){
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                // 设置接受类，打开网络连接调用
                                try {
                                    if(ncuinstance.urlPost(apis,JSONObject.toJSONString(Nreply))==null){
                                        talktip="发表失败";
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
                        Toast.makeText(TalkingActivity.this, talktip , Toast.LENGTH_SHORT).show();
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
        //打印测试
        Log.v("debug", "panduan leixing");
        if(talktype.equals("comment"))
        {
            //打开网络连接
            apis="/talk/comment?commentid="+apiid;
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
        }else if(talktype.equals("letter")){
            //打开网络连接
            apis="/talk/letter?letterid="+apiid;
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
        }
        //处理返回值
        while (isok==0){}
        isok=0;
        jsoncasttojavaclass();

        //空处理
        if(replyinfo==null||(replyinfo.getReplyid()<1)){
            return;
        }
        userid = replyinfo.getUserid();
        replyuserid = replyinfo.getReplyuserid();
        //填充数据

        /*在数组中存放数据*/
        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("userimage", R.mipmap.ic_launcher);//加入图片
            map.put("username", getusernamebyid(replyinfo.getUserid()));
            map.put("replyusername", getusernamebyid(replyinfo.getReplyuserid()));
            map.put("replytime", replyinfo.getReplytime());
            map.put("other", replyinfo.getUserid());
            map.put("replycontent", replyinfo.getReplycontent());
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.talking_row,//每一行的布局
                //动态数组中的数据源的键对应到定义布局的View中
                namearr,idarr);
        //为ListView绑定适配器
        setListAdapter(mSimpleAdapter);
}
    //3-8:点击列表的某一项，启动发表回复
    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        //3-8-1:获取当前选择的联系人ID
        long uid = l.getItemIdAtPosition(position);
        //跳转到评论界面
        AlertDialog.Builder builder = new AlertDialog.Builder(TalkingActivity.this);
        builder.setTitle("请输入您想说的话");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(TalkingActivity.this).inflate(R.layout.mydialog, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);

        final EditText words = (EditText)view.findViewById(R.id.mywords);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String replycontent = words.getText().toString().trim();
                // 组装letter值
                final reply Nreply=new reply();
                Nreply.setReplycontent(replycontent);
                Nreply.setReplystate(talktype);
                Nreply.setUserid(Ouserid);
                Nreply.setReplyuserid(getidbyposion(position));
                Nreply.setReplycommentid(apiid);
                Nreply.setReplytime(new timeUtil().getNowTime());
                apis="/talk/reply";

                //新开线程调用网络服务
                new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        // 设置接受类，打开网络连接调用
                        try {
                            if(ncuinstance.urlPost(apis,JSONObject.toJSONString(Nreply))==null){
                                talktip="发表失败";
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
                Toast.makeText(TalkingActivity.this, talktip , Toast.LENGTH_SHORT).show();
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


    public void jsoncasttojavaclass(){
        //如果连接失败
        if(NMR==null){
            //弹窗提示
            tips.myalert("对不起，网络连接失败",TalkingActivity.this);
        }
        try {
            //打印测试
            Log.v("debug", "准备处理数据");
            //如果连接成功，用json对象接受返回的数据
            JSONObject replyinfojson=(JSONObject)NMR.getContent();
            //利用反射，将jsonobject映射成java类
            replyinfo = JSON.toJavaObject(replyinfojson,reply.class);

            //打印测试
            Log.v("debug", replyinfojson.toString());
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

    public int getidbyposion(int poi){
        return Integer.parseInt(listItem.get(poi).get("other").toString());
    }
}