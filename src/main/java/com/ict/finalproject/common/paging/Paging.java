package com.ict.finalproject.common.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paging {
    private int nowPage;
    private int totalRecord;
    private int numPerPage;
    private int pagePerBlock;
    private int totalPage;
    private int beginBlock;
    private int endBlock;
    private int startIndex;

    public Paging(int nowPage, int totalRecord, int numPerPage, int pagePerBlock) {
        this.nowPage = nowPage;
        this.totalRecord = totalRecord;
        this.numPerPage = numPerPage;
        this.pagePerBlock = pagePerBlock;

        this.totalPage = (int) Math.ceil((double)totalRecord / (double)numPerPage);
        if(this.nowPage > this.totalPage){
            this.nowPage = this.totalPage;
        }

        this.startIndex = (nowPage - 1) * numPerPage;
        this.beginBlock = ((nowPage - 1) / pagePerBlock) * pagePerBlock + 1;
        this.endBlock = beginBlock + pagePerBlock - 1;

        if(endBlock > totalPage){
            endBlock = totalPage;
        }
    }
}
