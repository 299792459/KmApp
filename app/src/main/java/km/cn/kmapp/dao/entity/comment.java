package km.cn.kmapp.dao.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈评论表〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/3/16 0016
 * @since 1.0.0
 */
public class comment implements Serializable {


    //网络传输数据持久化
    private static final long serialVersionUID = 202003171008L;

    int commentid;
    String commentcontent;
    String commenttime;
    int userid;
    String commentstate;
    int sceneryid;
    String sceneryname;
    String userannoyname;

    public comment() {
    }

    public comment(int commentid, String commentcontent, String commenttime, int userid, String commentstate, int sceneryid, String sceneryname, String userannoyname) {
        this.commentid = commentid;
        this.commentcontent = commentcontent;
        this.commenttime = commenttime;
        this.userid = userid;
        this.commentstate = commentstate;
        this.sceneryid = sceneryid;
        this.sceneryname = sceneryname;
        this.userannoyname = userannoyname;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getCommentstate() {
        return commentstate;
    }

    public void setCommentstate(String commentstate) {
        this.commentstate = commentstate;
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

    public String getUserannoyname() {
        return userannoyname;
    }

    public void setUserannoyname(String userannoyname) {
        this.userannoyname = userannoyname;
    }



    @Override
    public String toString() {
        return "comment{" +
                "commentid=" + commentid +
                ", commentcontent='" + commentcontent + '\'' +
                ", commenttime='" + commenttime + '\'' +
                ", userid=" + userid +
                ", commentstate='" + commentstate + '\'' +
                ", sceneryid=" + sceneryid +
                ", sceneryname='" + sceneryname + '\'' +
                ", userannoyname='" + userannoyname + '\'' +
                '}';
    }
}
