package priv.llf.commons.except;

import lombok.Data;

/**
 * @Author: eleven
 * @Date: 2018/4/7 17:38
 * @Since: 1.0.0
 * @Description:
 */
@Data
public class ErrCodeConstant {
    /**
     * 异常code
     */
    private String code;
    /**
     * 异常消息
     */
    private String message;


    ErrCodeConstant(String code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString()
    {
        StringBuffer sbf = new StringBuffer("{Code:");
        sbf.append(this.code);
        sbf.append(",");
        sbf.append("message:");
        sbf.append(this.message);
        sbf.append("}\r\n");
        return sbf.toString();
    }
}
