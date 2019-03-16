package com.github.jankwq.yalta.bean.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.jankwq.yalta.bean.response.jsonview.BaseView;


/**
 * @author yinjianfeng
 * @date 2019/2/22
 */
public class StatusResponse {

    @JsonView(BaseView.class)
    protected Boolean success = true;

    @JsonView(BaseView.class)
    protected int status;

    @JsonView(BaseView.class)
    protected String message;

    public StatusResponse() {
        this.status = 0;
        this.message = "请求成功";
        this.success = true;
    }

    public StatusResponse setError() {
        return setError("未定义异常");
    }

    public StatusResponse setError(String message){
        this.status = -1;
        this.message = message;
        this.success = false;
        return this;
    }

    public StatusResponse(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public StatusResponse(final int status, final String message, final Boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }

    public static StatusResponse ok() {
        return new StatusResponse();
    }

    public static StatusResponse fail() {
        return new StatusResponse().setError();
    }

    public static StatusResponse fail(String message) {
        return new StatusResponse().setError(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
