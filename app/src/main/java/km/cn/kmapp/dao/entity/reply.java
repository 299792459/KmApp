package km.cn.kmapp.dao.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈回复表〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/3/28 0028
 * @since 1.0.0
 */
public class reply implements Serializable {

    //网络传输数据持久化
    private static final long serialVersionUID = 20200328L;

    Integer replyid;
    Integer replyuserid;
    Integer userid;
    Integer replycommentid;
    String replycontent;
    String replytime;
    String replystate;
    String replytype;
    



    public reply() {
    }



    public reply(Integer replyid, Integer replycommentid, Integer replyuserid, Integer userid, String replycontent, String replytime, String replytype, String replystate) {
        this.replyid = replyid;
        this.replyuserid = replyuserid;
        this.userid = userid;
        this.replycontent = replycontent;
        this.replytime = replytime;
        this.replystate = replystate;
        this.replytype=replytype;
        this.replycommentid=replycommentid;
    }

    @Override
    public String toString() {
        return "reply{" +
                "replyid=" + replyid +
                ", replyuserid=" + replyuserid +
                ", userid=" + userid +
                ", replycontent='" + replycontent + '\'' +
                ", replytime='" + replytime + '\'' +
                ", replystate='" + replystate + '\'' +
                ", replycommentid='" + replycommentid + '\'' +
                ", replytype='" + replytype + '\'' +
                '}';
    }

    public Integer getReplycommentid() {
        return replycommentid;
    }

    public void setReplycommentid(Integer replycommentid) {
        this.replycommentid = replycommentid;
    }

    public String getReplytype() {
        return replytype;
    }

    public void setReplytype(String replytype) {
        this.replytype = replytype;
    }

    public Integer getReplyid() {
        return replyid;
    }

    public void setReplyid(Integer replyid) {
        this.replyid = replyid;
    }

    public Integer getReplyuserid() {
        return replyuserid;
    }

    public void setReplyuserid(Integer replyuserid) {
        this.replyuserid = replyuserid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public String getReplytime() {
        return replytime;
    }

    public void setReplytime(String replytime) {
        this.replytime = replytime;
    }

    public String getReplystate() {
        return replystate;
    }

    public void setReplystate(String replystate) {
        this.replystate = replystate;
    }
}
