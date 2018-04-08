package priv.llf.commons.intercept;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import priv.llf.commons.except.BasicRuntimeException;
import priv.llf.commons.except.ErrCodeConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Eleven
 * @Since: 2018/4/7 17:12
 * @Description:
 */
@Slf4j
public class WrappedHandlerExceptionResolver extends DefaultHandlerExceptionResolver {


    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler,
                                              Exception ex) {
        log.error("详细异常栈：",ex);
        if (handler instanceof HandlerMethod)
        {
            response.setStatus(500);
            JSONObject jo = new JSONObject();
            jo.put("status",false);
            jo.put("code","500");

            if (ex instanceof BasicRuntimeException){
                ErrCodeConstant code = ((BasicRuntimeException) ex).getErrCode();
                log.error(ex.getMessage());
                if (code != null) {
                    jo.put("code", code.getCode());
                    jo.put("info", code.getMessage());
                } else {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(ex.getMessage());
                        if (jsonObject != null&&jsonObject.get("message")!=null) {
                            jo.put("info",jsonObject.get("message"));
                        }
                    } catch (Exception e) {
                        jo.put("info", ex.getMessage());
                    }
                }
            }else {
                jo.put("info",ex);
            }

            try {
                response.getWriter().write(jo.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ModelAndView();
        }


        return super.doResolveException(request, response, handler, ex);
    }
}
