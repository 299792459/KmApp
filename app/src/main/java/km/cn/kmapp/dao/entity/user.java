package km.cn.kmapp.dao.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈映射数据库实体类〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/3/13 0013
 * @since 1.0.0
 */



public class user implements Serializable {


    //网络传输数据持久化
    private static final long serialVersionUID = 20200314L;

    int userid;
    String useraccountname;
    String userannoyname;
    String userpassword;
    String userlocation;
    int usertel;
    int userqq;
    String email;
    String userstate;

    @Override
    public String toString() {
        return "user{" +
                "userid=" + userid +
                ", useraccountname='" + useraccountname + '\'' +
                ", userannoyname='" + userannoyname + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", userlocation='" + userlocation + '\'' +
                ", usertel=" + usertel +
                ", userqq=" + userqq +
                ", email='" + email + '\'' +
                ", userstate='" + userstate + '\'' +
                '}';
    }





    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public user(int userid, String useraccountname, String userannoyname, String userpassword, String userlocation, int usertel, int userqq, String email, String userstate) {
        this.userid = userid;
        this.useraccountname = useraccountname;
        this.userannoyname = userannoyname;
        this.userpassword = userpassword;
        this.userlocation = userlocation;
        this.usertel = usertel;
        this.userqq = userqq;
        this.email = email;
        this.userstate = userstate;
    }

    public user() {
    }

    public user(int userid, String useraccountname, String userannoyname, String userpassword, String userlocation, int usertel, int userqq, String userstate) {
        this.userid = userid;
        this.useraccountname = useraccountname;
        this.userannoyname = userannoyname;
        this.userpassword = userpassword;
        this.userlocation = userlocation;
        this.usertel = usertel;
        this.userqq = userqq;
        this.userstate = userstate;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUseraccountname() {
        return useraccountname;
    }

    public void setUseraccountname(String useraccountname) {
        this.useraccountname = useraccountname;
    }

    public String getUserannoyname() {
        return userannoyname;
    }

    public void setUserannoyname(String userannoyname) {
        this.userannoyname = userannoyname;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }

    public int getUsertel() {
        return usertel;
    }

    public void setUsertel(int usertel) {
        this.usertel = usertel;
    }

    public int getUserqq() {
        return userqq;
    }

    public void setUserqq(int userqq) {
        this.userqq = userqq;
    }

    public String getUserstate() {
        return userstate;
    }

    public void setUserstate(String userstate) {
        this.userstate = userstate;
    }
}
