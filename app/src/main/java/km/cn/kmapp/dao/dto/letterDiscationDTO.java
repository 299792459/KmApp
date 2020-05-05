package km.cn.kmapp.dao.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import km.cn.kmapp.dao.entity.letter;
import km.cn.kmapp.dao.entity.reply;

/**
 * 〈一句话功能简述〉<br>
 * 〈个人空间〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/21 0021
 * @since 1.0.0
 */
public class letterDiscationDTO implements Serializable {

    //网络传输数据持久化
    private static final long serialVersionUID = 202004212330L;

    List<letter> letterList = new LinkedList<letter>();
    Map<String,List<reply>> replyListMap=new HashMap<String,List<reply>>();



    @Override
    public String toString() {
        return "letterDiscationDTO{" +
                "letterList=" + letterList +
                ", replyListMap=" + replyListMap +
                '}';
    }

    public List<letter> getLetterList() {
        return letterList;
    }

    public void setLetterList(List<letter> letterList) {
        this.letterList = letterList;
    }

    public Map<String, List<reply>> getReplyListMap() {
        return replyListMap;
    }

    public void setReplyListMap(Map<String, List<reply>> replyListMap) {
        this.replyListMap = replyListMap;
    }

    public letterDiscationDTO(List<letter> letterList, Map<String, List<reply>> replyListMap) {
        this.letterList = letterList;
        this.replyListMap = replyListMap;
    }

    public letterDiscationDTO() {
    }
}
