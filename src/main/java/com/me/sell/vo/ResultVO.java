package com.me.sell.vo;

import java.io.Serializable;

/**
 * http请求返回的最外层对象(Json格式)
 * VO, view object
 */
//@Data 但是ide会报错啊
public class ResultVO<T> implements Serializable{

    private Integer code;
    private String msg;
    private T data;

    public static ResultVO success(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success!!!");
        resultVO.setData(null);
        return  resultVO;
    }

    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success!!!");
        resultVO.setData(data);
        return  resultVO;
    }

    public static ResultVO error(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(505);
        resultVO.setMsg(msg);
        resultVO.setData(null);
        return  resultVO;
    }

    public static ResultVO error(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setData(null);
        return  resultVO;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
