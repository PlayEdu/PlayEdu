package xyz.playedu.api.request.backend;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serial;
import java.io.Serializable;

public class PaginationRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "page参数值不能少于1")
    public Integer page = 1;

    @Min(value = 1, message = "size参数值不能少于1")
    @Max(value = 1000, message = "size参数值不能超过1000")
    public Integer size = 10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
