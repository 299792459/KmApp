package km.cn.kmapp.mainviewpage;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import java.io.IOException;

import km.cn.kmapp.LocationApplication;
import km.cn.kmapp.R;
import km.cn.kmapp.dao.entity.user;
import km.cn.kmapp.service.LocationService;
import km.cn.kmapp.service.Utils;
import km.cn.kmapp.util.NetConnectUtils;
import km.cn.kmapp.util.myResult;
import km.cn.kmapp.util.tips;

public class MainActivity extends Activity {



    //首页的控件
    private Button loginbutton;
    private Button registbutton;
    private EditText location;
    private EditText accountname;
    private EditText password;
    private TextView tipt;
    private TextView tipsmain;
    private Button startLocation;
    //private TextView testtextview;

    //全局变量
    StringBuffer sb = new StringBuffer(256);
    StringBuffer locations=new StringBuffer("苏州市");
    myResult MR=new myResult();
    public LocationService locationService;
    public Vibrator mVibrator;


    int isok=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 在这里写页面创建时后做的事
        startLocation=(Button)findViewById(R.id.startLocation);
        loginbutton=(Button)findViewById(R.id.loginbutton);
        registbutton=(Button)findViewById(R.id.registbutton);;
        location=(EditText)findViewById(R.id.location);
        accountname=(EditText)findViewById(R.id.accountnameET);
        password=(EditText)findViewById(R.id.passwordET);
        tipt=(TextView) findViewById(R.id.tips1);
        tipsmain=(TextView) findViewById(R.id.tipsmain);

        //注册按钮点击绑定监听器
        registbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳转到注册界面
                Intent  starIntent=new Intent("action_verify");
                // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                startActivityForResult(starIntent, 1);

            }
        });
        //登录按钮点击绑定监听器
        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // 当点击登录时，首先获取传过来的地址，用户名，密码
                String locations=location.getText().toString();
                String accountnames=accountname.getText().toString();
                String passwords=password.getText().toString();
                // 然后将三者拼接成api
                final String apis="/login/user?username="+accountnames+"&password="+passwords;

                //测试用，正式发布的时候删掉
                //打印测试
                Log.v("debug", apis);


                // 通过网络连接类调用后端接口
                new Thread(new Runnable() {
                    //通过线程执行连接操作
                    //降低系统负荷
                    @Override
                    public void run() {
                        //打开连接
                        try {
                            MR = new NetConnectUtils().urlGet(apis);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isok=1;
                        }
                    }}).start();

                while (isok==0){}
                isok=0;
                //Log.v("debug", MR.toString());
                //如果登录失败
                if(MR.getStatecode()==0){
                    //跳出提示弹窗
                    tips.myalert(MR.getMessage(),MainActivity.this);
                }else{
                    //如果登陆成功，则跳转到地区广场界面
                    Intent  starIntent=new Intent("action_index");
                    //传递所需要的值
                    starIntent.putExtra("location",locations);
                    //取出用户信息
                    JSONObject userinfojson = (JSONObject)MR.getContent();
                    //处理用户信息
                    //打印测试
                    //Log.v("debug", maininfojson.toString());
                    //利用反射，将jsonobject映射成java类
                    user Ouser = JSON.toJavaObject(userinfojson,user.class);
                    //传递userjson
                    starIntent.putExtra("userinfojson",userinfojson.toJSONString());
                    //传递userid
                    Log.v("debug", "main中userid"+Ouser.getUserid());
                    starIntent.putExtra("userid",Ouser.getUserid());
                    // 3-6-2:因为添加完之后要返回列表界面的，所以使用带返回的跳转方式
                    startActivityForResult(starIntent, 1);
                }

            }
        });


        startLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startLocation.getText().toString().equals(getString(R.string.startlocation))) {
                    locationService.start();// 定位SDK
                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText(getString(R.string.stoplocation));

                    location.setText(locations.toString());
                    //打印测试
                    Log.v("debug", locations.toString());
                } else {
                    locationService.stop();
                    startLocation.setText(getString(R.string.startlocation));
                    //打印测试
                    Log.v("debug", locations.toString());
                    location.setText(locations.toString());
                }
            }
        });

    }



    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
// -----------location config ------------
        // -----------location config ------------
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.start();
        }
    }
    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(final String str, final int tag) {

        try {
            if (tipsmain != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tipsmain.post(new Runnable() {
                            @Override
                            public void run() {
                                if (tag == Utils.RECEIVE_TAG) {
                                    tipsmain.setText(str);
                                } else if (tag == Utils.DIAGNOSTIC_TAG) {
                                    tipsmain.setText(str);
                                }
                            }
                        });
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                int tag = 1;

                locations=new StringBuffer(location.getProvince());
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlongtitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nProvince : ");// 获取省份
                sb.append(location.getProvince());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nTown : ");// 获取镇信息
                sb.append(location.getTown());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nStreetNumber : ");// 获取街道号码
                sb.append(location.getStreetNumber());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append("poiName:");
                        sb.append(poi.getName() + ", ");
                        sb.append("poiTag:");
                        sb.append(poi.getTags() + "\n");
                    }
                }
                if (location.getPoiRegion() != null) {
                    sb.append("PoiRegion: ");// 返回定位位置相对poi的位置关系，仅在开发者设置需要POI信息时才会返回，在网络不通或无法获取时有可能返回null
                    PoiRegion poiRegion = location.getPoiRegion();
                    sb.append("DerectionDesc:"); // 获取POIREGION的位置关系，ex:"内"
                    sb.append(poiRegion.getDerectionDesc() + "; ");
                    sb.append("Name:"); // 获取POIREGION的名字字符串
                    sb.append(poiRegion.getName() + "; ");
                    sb.append("Tags:"); // 获取POIREGION的类型
                    sb.append(poiRegion.getTags() + "; ");
                    sb.append("\nSDK版本: ");
                }
                sb.append(locationService.getSDKVersion()); // 获取SDK版本
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //logMsg(sb.toString(), tag);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * @param locType 当前定位类型
         * @param diagnosticType 诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
            int tag = 2;
            StringBuffer sb = new StringBuffer(256);
            sb.append("诊断结果: ");
            if (locType == BDLocation.TypeNetWorkLocation) {
                if (diagnosticType == 1) {
                    sb.append("网络定位成功，没有开启GPS，建议打开GPS会更好");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 2) {
                    sb.append("网络定位成功，没有开启Wi-Fi，建议打开Wi-Fi会更好");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeOffLineLocationFail) {
                if (diagnosticType == 3) {
                    sb.append("定位失败，请您检查您的网络状态");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeCriteriaException) {
                if (diagnosticType == 4) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 5) {
                    sb.append("定位失败，无法获取有效定位依据，请检查运营商网络或者Wi-Fi网络是否正常开启，尝试重新请求定位");
                    sb.append(diagnosticMessage);
                } else if (diagnosticType == 6) {
                    sb.append("定位失败，无法获取有效定位依据，请尝试插入一张sim卡或打开Wi-Fi重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 7) {
                    sb.append("定位失败，飞行模式下无法获取有效定位依据，请关闭飞行模式重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 9) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeServerError) {
                if (diagnosticType == 8) {
                    sb.append("定位失败，请确认您定位的开关打开状态，是否赋予APP定位权限");
                    sb.append("\n" + diagnosticMessage);
                }
            }
            //logMsg(sb.toString(), tag);
        }
    };
}
