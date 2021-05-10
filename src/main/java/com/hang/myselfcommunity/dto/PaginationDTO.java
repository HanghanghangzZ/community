package com.hang.myselfcommunity.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> DTOList;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new LinkedList<>();
    private Integer totalPage;

    public void setPaginationDTO(Integer totalPage, Integer page) {
        this.page = page;
        this.totalPage = totalPage;

        this.pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                this.pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                this.pages.add(page + i);
            }
        }

        /* 是否展示上一页 */
        this.showPrevious = page == 1 ? false : true;

        /* 是否展示下一页 */
        this.showNext = page == totalPage ? false : true;

        /* 是否展示第一页 */
        this.showFirstPage = this.pages.contains(1) ? false : true;

        /* 是否展示最后一页 */
        this.showEndPage = this.pages.contains(totalPage) ? false : true;
    }
}
