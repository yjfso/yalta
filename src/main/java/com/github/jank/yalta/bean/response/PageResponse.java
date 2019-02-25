package com.github.jank.yalta.bean.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.jank.yalta.bean.response.jsonview.BaseView;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.beans.Transient;
import java.util.List;

/**
 * @author yinjianfeng
 * @date 2019/2/22
 */
@SuppressWarnings("unused")
@NoArgsConstructor
public class PageResponse<T> extends StatusResponse {

    @JsonView(BaseView.class)
    private int currentPage = 0;

    @JsonView(BaseView.class)
    private int pageSize = 15;

    @JsonView(BaseView.class)
    private int totalPage;

    @JsonView(BaseView.class)
    private int totalCount;

    @JsonView(BaseView.class)
    private List<T> data;

    public PageResponse(List<T> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        if (currentPage != null){
            this.currentPage = currentPage;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null){
            this.pageSize = pageSize;
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setPage(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    @Transient
    public boolean isEmpty(){
        return CollectionUtils.isEmpty(this.data);
    }

    @Transient
    public boolean isNotEmpty(){
        return !CollectionUtils.isEmpty(this.data);
    }

    public static <T> PageResponse<T> success(List<T> data) {
        return new PageResponse<T>(data);
    }
}
