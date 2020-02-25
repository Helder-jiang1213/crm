package com.shsxt.crm;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.interceptors.NoLoginInterceptor;
import com.shsxt.crm.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 首先判断异常类型
         * 如果异常类型为未登录异常，执行视图转发
         *
         */
        ModelAndView mv = new ModelAndView();
        if(ex instanceof NoLoginException){
            NoLoginException ne = (NoLoginException) ex;
            mv.setViewName("no_login");
            mv.addObject("msg",ne.getMsg());
            mv.addObject("ctx",request.getContextPath());
            return mv;
        }


        /**方法返回值类型判断：
         *      如果方法级别存在@RequestBody，方法响应内容为json否则为视图
         *      handler参数类型为HandlerMethod
         * 返回值
         *      视图：默认错误页面
         *
         *
         *      json：错误的json信息
         */


        mv.setViewName("errors");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常，请稍后再试..");
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            ResponseBody responseBody = hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (responseBody==null){
                /**
                 * 方法返回视图
                 *
                 * 判断异常的类型
                 */

                if(ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    mv.addObject("msg",pe.getMsg());
                    mv.addObject("code",pe.getCode());
                }
                return mv;
            }else{
                /**
                 * 方法返回json
                 */
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误，请稍后再试");
                if (ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    resultInfo.setMsg(pe.getMsg());
                    resultInfo.setCode(pe.getCode());
                }
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter pw = null;

                try {
                    pw = response.getWriter();
                    pw.write(JSON.toJSONString(resultInfo));
                    pw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != pw){
                        pw.close();
                    }
                }
                return null;
            }
        } else {
            return null;
        }

    }
}
