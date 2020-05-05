package km.cn.kmapp.functionpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import km.cn.kmapp.R;
import km.cn.kmapp.dao.entity.user;
import km.cn.kmapp.util.NetConnectUtils;
import km.cn.kmapp.util.myResult;
import km.cn.kmapp.util.tips;


public class VerifyActivity extends Activity {

    //页面控件
    private Button getverifycodebutton;
    private Button askverifybutton;
    private EditText email;
    private EditText verifycode;

    //全局变量
    user Nuser=new user();
    myResult NMR=new myResult();
    String emails;
    String verifycodes;
    int ischecked=0;
    int isok=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        // 在这里写页面创建时后做的事

        getverifycodebutton=(Button)findViewById(R.id.getverifycodebutton);
        askverifybutton=(Button)findViewById(R.id.askverifybutton);
        email=(EditText)findViewById(R.id.email);
        verifycode=(EditText)findViewById(R.id.verifycode);

        //获取验证码按钮点击绑定监听器
        getverifycodebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try{
                    emails=email.getText().toString();
                }catch (Exception e){
                    email.setError("请输入邮箱");
                    e.printStackTrace();
                }
                //新开线程处理
                        //打开网络连接，调用get获取验证码服务
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    NMR = new NetConnectUtils().urlGet("/regist/email?Email="+emails);
                                    isok=1;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }}).start();
                //线程处理完毕以后
                //弹窗提示后台消息
                while(isok==0){}
                isok=0;
                tips.myalert(NMR.getMessage(),VerifyActivity.this);
            }
        });

        //请求验证按钮点击绑定监听器
        askverifybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try{
                    verifycodes = (verifycode.getText().toString());
                }catch (Exception e){
                    email.setError("请输入验证码");
                    e.printStackTrace();
                }
                        //打开网络连接，调用post注册服务
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    NMR = new NetConnectUtils().urlGet("/regist/verifycode?Email="+emails+"&verifycode="+verifycodes);
                                    //如果验证成功，ischecked变为1
                                    if(NMR.getStatecode()==1){
                                        ischecked=1;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                isok=1;
                            }}).start();

                //成功则返回登录界面
                while(isok==0){}
                isok=0;
                //弹窗提示
                tips.myalert(NMR.getMessage(),VerifyActivity.this);
                if(ischecked==1)
                {
                    ischecked=0;
                    //跳转到注册界面
                    Intent starIntent=new Intent("action_regist");
                    // 3-6-2:因为添加完之后不返回列表界面，所以使用不带返回的跳转方式
                    startActivityForResult(starIntent, 0);
                }

            }
        });


    }



}
