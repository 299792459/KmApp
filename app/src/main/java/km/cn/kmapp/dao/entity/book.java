package km.cn.kmapp.dao.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈预约表〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/3 0003
 * @since 1.0.0
 */
public class book implements Serializable {

    //网络传输数据持久化
    private static final long serialVersionUID = 202004031707L;


    int bookid;

    int userid;
    int booksceneryid;
    String booktime;
    String bookstate;
    String generattime;

    public book() {
    }

    public book(int bookid, int userid, int booksceneryid, String booktime, String bookstate, String generattime) {
        this.bookid = bookid;

        this.userid = userid;
        this.booksceneryid = booksceneryid;
        this.booktime = booktime;
        this.bookstate = bookstate;
        this.generattime = generattime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof book)) return false;
        book book = (book) o;
        return getBookid() == book.getBookid() &&
                getUserid() == book.getUserid() &&
                getBooksceneryid() == book.getBooksceneryid() &&
                getBooktime().equals(book.getBooktime()) &&
                getBookstate().equals(book.getBookstate()) &&
                getGenerattime().equals(book.getGenerattime());
    }



    @Override
    public String toString() {
        return "book{" +
                "bookid=" + bookid +
                ", userid=" + userid +
                ", booksceneryid=" + booksceneryid +
                ", booktime='" + booktime + '\'' +
                ", bookstate='" + bookstate + '\'' +
                ", generattime='" + generattime + '\'' +
                '}';
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBooksceneryid() {
        return booksceneryid;
    }

    public void setBooksceneryid(int booksceneryid) {
        this.booksceneryid = booksceneryid;
    }

    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }

    public String getBookstate() {
        return bookstate;
    }

    public void setBookstate(String bookstate) {
        this.bookstate = bookstate;
    }

    public String getGenerattime() {
        return generattime;
    }

    public void setGenerattime(String generattime) {
        this.generattime = generattime;
    }
}
