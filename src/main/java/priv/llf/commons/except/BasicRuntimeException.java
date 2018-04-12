package priv.llf.commons.except;

import lombok.Data;

/**
 * @Author: eleven
 * @Date: 2018/4/7 17:38
 * @Since: 1.0.0
 * @Description:
 */
@Data
public class BasicRuntimeException extends RuntimeException{

    protected static final String NEWLINE = "\r\n";
    /**
     * 发生异常时自定义的code
     */
    private ErrCodeConstant errCode;

    public BasicRuntimeException(){}

    public BasicRuntimeException(ErrCodeConstant errorCode)
    {
        this.errCode = errorCode;
    }

    public BasicRuntimeException(String message)
    {
        super(message);
    }

    public BasicRuntimeException(String message,ErrCodeConstant errorCode){
        super(message);
        this.errCode = errorCode;
    }

    @Override
    public String getMessage() {

        if (this.errCode != null){
            return this.errCode.toString()+NEWLINE+super.getMessage();
        }

        return super.getMessage();
    }
}
