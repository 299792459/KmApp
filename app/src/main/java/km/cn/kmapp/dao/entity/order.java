package km.cn.kmapp.dao.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈订单表〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/3/16 0016
 * @since 1.0.0
 */
public class order implements Serializable {


    //网络传输数据持久化
    private static final long serialVersionUID = 202003170908L;

    Integer orderid;
    Integer orderuserid1;
    Integer orderuserid2;
    String orderstate;
    String ordertime;
    Integer ordersceneryid;
    String other;

    @Override
    public String toString() {
        return "order{" +
                "orderid=" + orderid +
                ", orderuserid1='" + orderuserid1 + '\'' +
                ", orderuserid2='" + orderuserid2 + '\'' +
                ", orderstate='" + orderstate + '\'' +
                ", ordertime='" + ordertime + '\'' +
                ", ordersceneryid='" + ordersceneryid + '\'' +
                ", other='" + other + '\'' +
                '}';
    }



    public order(int orderid, Integer orderuserid1, Integer orderuserid2, String orderstate, String ordertime, Integer ordersceneryid, String other) {
        this.orderid = orderid;
        this.orderuserid1 = orderuserid1;
        this.orderuserid2 = orderuserid2;
        this.orderstate = orderstate;
        this.ordertime = ordertime;
        this.ordersceneryid = ordersceneryid;
        this.other = other;
    }

    public order() {
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public Integer getOrderuserid1() {
        return orderuserid1;
    }

    public void setOrderuserid1(Integer orderuserid1) {
        this.orderuserid1 = orderuserid1;
    }

    public Integer getOrderuserid2() {
        return orderuserid2;
    }

    public void setOrderuserid2(Integer orderuserid2) {
        this.orderuserid2 = orderuserid2;
    }

    public String getOrderstate() {
        return orderstate;
    }

    public void setOrderstate(String orderstate) {
        this.orderstate = orderstate;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public Integer getOrdersceneryid() {
        return ordersceneryid;
    }

    public void setOrdersceneryid(Integer ordersceneryid) {
        this.ordersceneryid = ordersceneryid;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
