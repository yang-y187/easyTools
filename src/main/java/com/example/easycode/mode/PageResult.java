package com.example.easycode.mode;

import java.util.Collections;
import java.util.List;

/**
 * @author wangyangyang
 * @Description: 分页结果
 * @date 2026-03-28 13:44
 */
public class PageResult<T> {

    private boolean success;

    private String code;

    private String message;

    private Integer pageNo;

    private Integer pageSize;

    private Long total;

    private List<T> records;

    public static <T> PageResult<T> success(Integer pageNo, Integer pageSize, Long total, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setSuccess(true);
        result.setCode(DefaultResultCode.SUCCESS.getCode());
        result.setMessage(DefaultResultCode.SUCCESS.getMessage());
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setRecords(records);
        return result;
    }

    public static <T> PageResult<T> fail(String message) {
        PageResult<T> result = new PageResult<>();
        result.setSuccess(false);
        result.setCode(DefaultResultCode.FAIL.getCode());
        result.setMessage(message);
        result.setRecords(Collections.emptyList());
        result.setTotal(0L);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records == null ? Collections.emptyList() : records;
    }
}
