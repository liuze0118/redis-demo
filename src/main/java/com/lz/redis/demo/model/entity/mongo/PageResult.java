package com.lz.redis.demo.model.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果
 *
 * @author yangsongbo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 分页数据
     */
    private List<T> rowData;


    /**
     * 基础分页信息
     *
     * @author yangsongbo
     */
    @NoArgsConstructor
    @Data
    public static class PageInfo {


        /**
         * 每页条数
         */
        private Integer pageSize;
        /**
         * 当前页码
         */
        private Integer pageNum;

        /**
         * 总条数
         */
        private Long totalCount;

        /**
         * 总页数
         */
        private Integer pageCount;

        /**
         * 是否有下一页
         */
        private Boolean hasNexPage;

        /**
         * 是否有上一页
         */
        private Boolean hasPrePage;

        /**
         * 是否为第一页
         */
        private Boolean isFirstPage;

        /**
         * 是否为最后一页
         */
        private Boolean isLastPage;

        /**
         * 当前结果数据条数
         */
        private Integer dataSize;


        public PageInfo(Integer pageNum, Integer pageSize, Long totalCount, Integer dataSize) {

            if (pageNum == null || pageNum <= 0) {
                throw new IllegalArgumentException("pageNum[" + pageSize + "]必须大于0");
            }

            if (pageSize == null || pageSize <= 0) {
                throw new IllegalArgumentException("pageSize[" + pageSize + "]必须大于0");
            }

            if (totalCount == null || totalCount < 0) {
                throw new IllegalArgumentException("totalCount[" + totalCount + "]必须大于等于0");
            }

            if (dataSize == null || dataSize < 0) {
                throw new IllegalArgumentException("dataSize[" + dataSize + "]必须大于等于0");
            }

            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.dataSize = dataSize;
            this.pageNum = pageNum;

            pageCount = totalCount == 0 ? 0 :
                    (totalCount.intValue() % pageSize > 0 ? (totalCount.intValue() / pageSize) + 1
                            : totalCount.intValue() / pageSize);

            //如果pageNum大于总页数，那么pageNum保持在最后一页
//            this.pageNum = pageNum > pageCount ? pageCount : pageNum;

            hasNexPage = (pageCount > pageNum);
            hasPrePage = (pageNum > 1);
            isFirstPage = (pageNum == 1);
            isLastPage = !hasNexPage;
        }

    }
}
