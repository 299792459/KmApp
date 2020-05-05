package km.cn.kmapp.dao.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈景点表〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/3/17 0017
 * @since 1.0.0
 */
public class scenery implements Serializable {


    //网络传输数据持久化
    private static final long serialVersionUID = 202003170840L;


    int sceneryid;
    String sceneryname;
    String scenerydescription;
    String scenerystate;
    String location;
    String imgurl;
    String tel;


    public scenery(int sceneryid, String sceneryname, String scenerydescription, String scenerystate, String location, String imgurl, String tel) {
        this.sceneryid = sceneryid;
        this.sceneryname = sceneryname;
        this.scenerydescription = scenerydescription;
        this.scenerystate = scenerystate;
        this.location = location;
        this.imgurl = imgurl;
        this.tel = tel;
    }





    @Override
    public String toString() {
        return "scenery{" +
                "sceneryid=" + sceneryid +
                ", sceneryname='" + sceneryname + '\'' +
                ", scenerydescription='" + scenerydescription + '\'' +
                ", scenerystate='" + scenerystate + '\'' +
                ", location='" + location + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public scenery() {
    }

    public int getSceneryid() {
        return sceneryid;
    }

    public void setSceneryid(int sceneryid) {
        this.sceneryid = sceneryid;
    }

    public String getSceneryname() {
        return sceneryname;
    }

    public void setSceneryname(String sceneryname) {
        this.sceneryname = sceneryname;
    }

    public String getScenerydescription() {
        return scenerydescription;
    }

    public void setScenerydescription(String scenerydescription) {
        this.scenerydescription = scenerydescription;
    }

    public String getScenerystate() {
        return scenerystate;
    }

    public void setScenerystate(String scenerystate) {
        this.scenerystate = scenerystate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
