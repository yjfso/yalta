package com.github.jankwq.yalta.bean.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.jankwq.yalta.bean.response.jsonview.BaseView;

/**
 * @author yinjianfeng
 * @date 2019/2/22
 */
public class DataResponse<T> extends StatusResponse {

    @JsonView(BaseView.class)
    private T data;

    public DataResponse(T data) {
        super();
        this.data = data;
    }

    public DataResponse() {
        super();
    }

    public DataResponse(int status, String message) {
        super(status, message);
    }

    public DataResponse(final int status, final String message, T data ,final Boolean success) {
        super(status, message, success);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public DataResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.data == null;
    }

    @JsonIgnore
    public boolean isNotEmpty() {
        return this.data != null;
    }

    public static <T> DataResponse<T> success(T data) {
        return new DataResponse<>(data);
    }

}
