package com.ppxb.la.base.common.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ppxb.la.base.common.domain.PageParam;
import com.ppxb.la.base.common.domain.PageResult;
import com.ppxb.la.base.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PageUtil {

    public static Page<?> convert2PageQuery(PageParam pageParam) {
        Page<?> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        if (pageParam.getSearchCount() != null) {
            page.setSearchCount(pageParam.getSearchCount());
        }

        List<PageParam.SortItem> sortItemList = pageParam.getSortItemList();
        if (CollectionUtils.isEmpty(sortItemList)) {
            return page;
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        for (PageParam.SortItem sortItem : sortItemList) {
            if (StringUtil.isEmpty(sortItem.getColumn())) {
                continue;
            }

            if (SqlInjectionUtils.check(sortItem.getColumn())) {
                log.error("存在 SQL 注入：{}", sortItem.getColumn());
                throw new BusinessException("存在SQL注入风险，请联系技术工作人员");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(sortItem.getColumn());
            orderItem.setAsc(sortItem.getIsAsc());
            orderItemList.add(orderItem);
        }
        page.setOrders(orderItemList);
        return page;
    }

    public static <E> PageResult<E> convert2PageResult(Page<?> page, List<E> sourceList) {
        PageResult<E> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setList(sourceList);
        pageResult.setEmptyFlag(CollectionUtils.isEmpty(sourceList));
        return pageResult;
    }

    public static <E, T> PageResult<T> convert2PageResult(PageResult<E> pageResult, Class<T> targetClass) {
        PageResult<T> newPageResult = new PageResult<>();
        newPageResult.setPageNum(pageResult.getPageNum());
        newPageResult.setPageSize(pageResult.getPageSize());
        newPageResult.setTotal(pageResult.getTotal());
        newPageResult.setPages(pageResult.getPages());
        newPageResult.setEmptyFlag(pageResult.getEmptyFlag());
        newPageResult.setList(BeanUtil.copyList(pageResult.getList(), targetClass));
        return newPageResult;
    }

    public static <T, E> PageResult<T> convert2PageResult(Page<?> page, List<E> sourceList, Class<T> targetClass) {
        return convert2PageResult(page, BeanUtil.copyList(sourceList, targetClass));
    }


    public static <T> PageResult<T> subListPage(Integer pageNum, Integer pageSize, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        int count = list.size();
        int pages = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(pageNum * pageSize, count);

        if (pageNum > pages) {
            pageResult.setPageNum(pageNum.longValue());
            pageResult.setPages((long) pages);
            pageResult.setTotal((long) count);
            pageResult.setList(Lists.newLinkedList());
            return pageResult;
        }

        List<T> pageList = list.subList(fromIndex, toIndex);
        pageResult.setPageNum(pageNum.longValue());
        pageResult.setPages((long) pages);
        pageResult.setTotal((long) count);
        pageResult.setList(pageList);
        return pageResult;
    }
}
