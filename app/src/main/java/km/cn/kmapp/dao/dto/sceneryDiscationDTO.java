package km.cn.kmapp.dao.dto;



import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import km.cn.kmapp.dao.entity.comment;
import km.cn.kmapp.dao.entity.reply;

/**
 * 〈一句话功能简述〉<br>
 * 〈景点讨论拼装类〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/12 0012
 * @since 1.0.0
 */
public class sceneryDiscationDTO implements Serializable {

    //网络传输数据持久化
    private static final long serialVersionUID = 202004122034L;

    //包含的数据
    //这里将各个实体类的属性拆分，避免不必要的网络传输，并且提高了安全性

    //景点也显示的评论列表
    List<comment> commentList;
    //主页显示的景区对应的评论列表
    Map<String,List<reply>> replyListMap;

    @Override
    public String toString() {
        return "sceneryDiscationDTO{" +
                "commentList=" + commentList +
                ", replyListMap=" + replyListMap +
                '}';
    }



    @Override
    public int hashCode() {
        return Objects.hash(getCommentList(), getReplyListMap());
    }

    public sceneryDiscationDTO(List<comment> commentList, Map<String, List<reply>> replyListMap) {
        this.commentList = commentList;
        this.replyListMap = replyListMap;
    }

    public List<comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<comment> commentList) {
        this.commentList = commentList;
    }

    public Map<String, List<reply>> getReplyListMap() {
        return replyListMap;
    }

    public void setReplyListMap(Map<String, List<reply>> replyListMap) {
        this.replyListMap = replyListMap;
    }

    public sceneryDiscationDTO() {
    }
}
