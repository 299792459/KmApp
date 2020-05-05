package km.cn.kmapp.dao.dto;



import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import km.cn.kmapp.dao.entity.comment;
import km.cn.kmapp.dao.entity.scenery;

/**
 * 〈一句话功能简述〉<br>
 * 〈主页关键展示信息DTO〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/5 0005
 * @since 1.0.0
 */

public class indexMainInfoDTO implements Serializable {

    //网络传输数据持久化
    private static final long serialVersionUID = 202004061602L;

    //包含的数据
    //这里将各个实体类的属性拆分，避免不必要的网络传输，并且提高了安全性

    //主页显示的景区列表
    List<scenery> sceneryList;
    //主页显示的景区对应的评论列表
    Map<String,List<comment>> commentListMap;

    public indexMainInfoDTO() {
    }

    public indexMainInfoDTO(List<scenery> sceneryList, Map<String, List<comment>> commentListMap) {
        this.sceneryList = sceneryList;
        this.commentListMap = commentListMap;
    }

    @Override
    public String toString() {
        return "indexMainInfoDTO{" +
                "sceneryList=" + sceneryList +
                ", commentListMap=" + commentListMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof indexMainInfoDTO)) return false;
        indexMainInfoDTO that = (indexMainInfoDTO) o;
        return getSceneryList().equals(that.getSceneryList()) &&
                getCommentListMap().equals(that.getCommentListMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSceneryList(), getCommentListMap());
    }

    public List<scenery> getSceneryList() {
        return sceneryList;
    }

    public void setSceneryList(List<scenery> sceneryList) {
        this.sceneryList = sceneryList;
    }

    public Map<String, List<comment>> getCommentListMap() {
        return commentListMap;
    }

    public void setCommentListMap(Map<String, List<comment>> commentListMap) {
        this.commentListMap = commentListMap;
    }
}
