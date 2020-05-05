package km.cn.kmapp.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈时间工具类〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/11 0011
 * @since 1.0.0
 */
public class timeUtil {

    //时间函数：只获取当前date的年月日


    // 时间函数：获取当前时间
    public String getNowTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        return dateFormat.format(date).toString();
    }

    // 时间函数：当前时间加减N天
    public myResult timeAddNDay(int Nday,String Odate){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date sDate = null;
            sDate = sdf.parse(Odate);
            //要实现日期+1 需要String转成Date类型
            Format f = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sDate);
            //利用Calendar 实现 Date日期+Nday天
            c.add(Calendar.DAY_OF_MONTH, Nday);

            sDate = c.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Odate = sdf1.format(sDate);
            //返回修改过的时间
            String Ndate=Odate;
            return new myResult(1,"转换成功",Ndate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new myResult(0,"对不起，转换失败","");
        }



    }
}
