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


public class RegistActivity extends Activity {
    //mystar页面控件
    private Button registbutton;
    private EditText tel;
    private EditText qq;
    private EditText email;
    private EditText accountname;
    private EditText password;
    private EditText annoyname;

    //全局变量
    int isok=0;
    user Nuser=new user();
    myResult NMR=new myResult();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registorupdate);

        // 在这里写页面创建时后做的事

        registbutton=(Button)findViewById(R.id.registokbutton);
        tel=(EditText)findViewById(R.id.tel);
        qq=(EditText)findViewById(R.id.qq);
        email=(EditText)findViewById(R.id.email);
        accountname=(EditText)findViewById(R.id.accountnameET);
        password=(EditText)findViewById(R.id.passwordET);
        annoyname=(EditText)findViewById(R.id.annoynameET);

        //用户点击注册完毕按钮以后，后台接收到传过来的用户信息
        //注册按钮点击绑定监听器
        registbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //组装成user

                try{
                    Nuser.setUseraccountname(accountname.getText().toString());
                    Nuser.setUserannoyname(annoyname.getText().toString());
                    Nuser.setUserpassword(password.getText().toString());
                    Nuser.setUserqq(Integer.parseInt(qq.getText().toString()));
                    Nuser.setUsertel(Integer.parseInt(tel.getText().toString()));
                    Nuser.setEmail(email.getText().toString());
                }catch (Exception e){
                    tel.setError("请输入电话");
                    qq.setError("请输入qq");
                    email.setError("请输入邮箱");
                    e.printStackTrace();
                }

                //新开线程处理
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //打开网络连接，调用post注册服务
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        NMR = new NetConnectUtils().urlPost("/regist/userInfo",JSON.toJSONString(Nuser));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    isok=1;
                                }}).start();
                    }
                }).start();
                while (isok==0){}
                isok=0;
                //如果失败弹窗提示
                if(NMR.getStatecode()==0){
                    //弹窗提示
                    tips.myalert(NMR.getMessage(),RegistActivity.this);
                }
                else {
                    //成功则返回登录界面
                    Intent starIntent=new Intent("android.intent.action.MAIN");
                    // 3-6-2:因为添加完之后不返回列表界面，所以使用不带返回的跳转方式
                    startActivityForResult(starIntent,1);
                }

            }
        });


    }

}
