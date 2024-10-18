package com.ppxb.la.admin.module.system.department.manager;

import com.google.common.collect.Lists;
import com.ppxb.la.admin.constant.AdminCacheConst;
import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentTreeVO;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentVO;
import com.ppxb.la.base.common.util.BeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentCacheManager {

    @Resource
    private DepartmentDao departmentDao;

    private void logClearInfo(String cache) {
        log.info("clear {}", cache);
    }

    @CacheEvict(value = {
            AdminCacheConst.Department.DEPARTMENT_LIST_CACHE,
            AdminCacheConst.Department.DEPARTMENT_MAP_CACHE,
            AdminCacheConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE,
            AdminCacheConst.Department.DEPARTMENT_TREE_CACHE,
            AdminCacheConst.Department.DEPARTMENT_PATH_CACHE}, allEntries = true)
    public void clearCache() {
        logClearInfo(AdminCacheConst.Department.DEPARTMENT_LIST_CACHE);
    }

    /**
     * 部门列表
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_LIST_CACHE)
    public List<DepartmentVO> getDepartmentList() {
        return departmentDao.listAll();
    }

    /**
     * 部门map
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_MAP_CACHE)
    public Map<Long, DepartmentVO> getDepartmentMap() {
        return this.getDepartmentList()
                .stream()
                .collect(Collectors.toMap(DepartmentVO::getDepartmentId, Function.identity()));
    }

    /**
     * 缓存部门树结构
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_TREE_CACHE)
    public List<DepartmentTreeVO> getDepartmentTree() {
        return this.buildTree(this.getDepartmentList());
    }

    /**
     * 缓存某个部门的下级id列表
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE)
    public List<Long> getDepartmentSelfAndChildren(Long departmentId) {
        return this.selfAndChildrenIdList(departmentId, this.getDepartmentList());
    }


    /**
     * 部门的路径名称
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_PATH_CACHE)
    public Map<Long, String> getDepartmentPathMap() {
        var departmentMap = this.getDepartmentMap();
        return departmentMap
                .values()
                .stream()
                .collect(Collectors.toMap(DepartmentVO::getDepartmentId, dept -> buildDepartmentPath(dept, departmentMap)));
    }

    /**
     * 构建父级考点路径
     */
    private String buildDepartmentPath(DepartmentVO departmentVO, Map<Long, DepartmentVO> departmentMap) {
        if (Objects.equals(departmentVO.getParentId(), NumberUtils.LONG_ZERO)) {
            return departmentVO.getName();
        }

        var parentDepartment = departmentMap.get(departmentVO.getParentId());
        if (parentDepartment == null) {
            return departmentVO.getName();
        }

        var pathName = buildDepartmentPath(parentDepartment, departmentMap);
        return pathName + "/" + departmentVO.getName();
    }

    /**
     * 构建部门树结构
     */
    public List<DepartmentTreeVO> buildTree(List<DepartmentVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Lists.newArrayList();
        }

        var rootList = voList
                .stream()
                .filter(e -> e.getParentId() == null || Objects.equals(e.getParentId(), NumberUtils.LONG_ZERO))
                .toList();
        if (CollectionUtils.isEmpty(rootList)) {
            return Lists.newArrayList();
        }

        var treeVOList = BeanUtil.copyList(rootList, DepartmentTreeVO.class);
        this.recursiveBuildTree(treeVOList, voList);
        return treeVOList;
    }

    /**
     * 构建所有根节点的下级树形结构
     * 返回值为层序遍历结果
     * [由于departmentDao中listAll给出数据根据Sort降序 所以同一层中Sort值较大的优先遍历]
     */
    private List<Long> recursiveBuildTree(List<DepartmentTreeVO> nodeList, List<DepartmentVO> allDepartmentList) {
        var nodeSize = nodeList.size();
        var childIdList = new ArrayList<Long>();

        for (int i = 0; i < nodeSize; i++) {
            var preIndex = i - 1;
            var nextIndex = i + 1;
            var node = nodeList.get(i);
            if (preIndex > -1) {
                node.setPreId(nodeList.get(preIndex).getDepartmentId());
            }
            if (nextIndex < nodeSize) {
                node.setNextId(nodeList.get(nextIndex).getDepartmentId());
            }

            var children = getChildren(node.getDepartmentId(), allDepartmentList);
            List<Long> tmpChildIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(children)) {
                node.setChildren(children);
                tmpChildIdList = this.recursiveBuildTree(children, allDepartmentList);
            }

            if (CollectionUtils.isEmpty(node.getSelfAndAllChildrenIdList())) {
                node.setSelfAndAllChildrenIdList(new ArrayList<>());
            }

            node.getSelfAndAllChildrenIdList().add(node.getDepartmentId());

            if (CollectionUtils.isNotEmpty(tmpChildIdList)) {
                node.getSelfAndAllChildrenIdList().addAll(tmpChildIdList);
                childIdList.addAll(tmpChildIdList);
            }
        }

        for (int i = nodeSize - 1; i >= 0; i--) {
            childIdList.add(0, nodeList.get(i).getDepartmentId());
        }

        return childIdList;
    }

    /**
     * 获取子元素
     */
    private List<DepartmentTreeVO> getChildren(Long departmentId, List<DepartmentVO> voList) {
        var childrenEntityList = voList.stream().filter(e -> e.getParentId().equals(departmentId)).toList();
        if (CollectionUtils.isEmpty(childrenEntityList)) {
            return Lists.newArrayList();
        }
        return BeanUtil.copyList(childrenEntityList, DepartmentTreeVO.class);
    }

    /**
     * 通过部门id,获取当前以及下属部门
     */
    public List<Long> selfAndChildrenIdList(Long departmentId, List<DepartmentVO> voList) {
        List<Long> selfAndChildrenIdList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(voList)) {
            return selfAndChildrenIdList;
        }
        selfAndChildrenIdList.add(departmentId);

        var children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return selfAndChildrenIdList;
        }

        var childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).toList();
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
        return selfAndChildrenIdList;
    }

    /**
     * 递归查询
     */
    public void selfAndChildrenRecursion(List<Long> selfAndChildrenIdList, Long departmentId, List<DepartmentVO> voList) {
        var children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        var childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).toList();
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
    }
}
