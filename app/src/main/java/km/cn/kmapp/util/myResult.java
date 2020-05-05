package km.cn.kmapp.util;

import java.io.Serializable;
import java.util.Objects;

public class myResult implements Serializable {

    //网络传输序列化
    private static final long serialVersionUID = 202004151706L;

    int statecode;
    String message;
    Object content;

    public myResult() {
    }

    public myResult(int statecode, String message, Object content) {
        this.statecode = statecode;
        this.message = message;
        this.content = content;
    }

    @Override
    public String toString() {
        return "myResult{" +
                "statecode=" + statecode +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof myResult)) return false;
        myResult myResult = (myResult) o;
        return getStatecode() == myResult.getStatecode() &&
                getMessage().equals(myResult.getMessage()) &&
                getContent().equals(myResult.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatecode(), getMessage(), getContent());
    }

    public int getStatecode() {
        return statecode;
    }

    public void setStatecode(int statecode) {
        this.statecode = statecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

