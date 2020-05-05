package km.cn.kmapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import km.cn.kmapp.R;

public class tips {

    //弹窗函数
    public static void myalert(String Omessage, final Activity mya){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mya);
        //    设置Title的图标
        builder.setIcon(R.mipmap.ic_launcher);
        //    设置Title的内容
        builder.setTitle("Tips");
        //    设置Content来显示一个信息
        builder.setMessage(Omessage);
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(mya, "positive: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //  显示出该对话框
        builder.show();
    }
}
