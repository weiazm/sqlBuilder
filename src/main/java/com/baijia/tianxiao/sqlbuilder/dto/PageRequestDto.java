package com.baijia.tianxiao.sqlbuilder.dto;

import lombok.Data;

/**
 * @author cxm
 * @version 1.0
 * @title PageRequestDto
 * @desc TODO
 * @date 2015年12月26日
 */
@Data
public class PageRequestDto {

    /**
     * 分页信息
     */
    private Integer pageNum;
    /**
     * 每页显示的大小
     */
    private Integer pageSize;

    private transient PageDto pageDto;

    /**
     *
     */
    public PageRequestDto() {
        super();
    }

    public PageDto getPageDto() {
        if (this.pageDto != null) {
            return this.pageDto;
        }
        if (this.pageNum != null && this.pageNum > 0) {
            if (this.pageDto == null) {
                this.pageDto = new PageDto();
            }
            pageDto.setPageNum(pageNum);
        }
        if (this.pageSize != null && this.pageSize > 0) {
            if (this.pageDto == null) {
                this.pageDto = new PageDto();
            }
            this.pageDto.setPageSize(pageSize);
        }
        return this.pageDto;
    }

}
