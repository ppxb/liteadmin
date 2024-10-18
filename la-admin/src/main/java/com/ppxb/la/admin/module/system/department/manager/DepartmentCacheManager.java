package com.ppxb.la.admin.module.system.department.manager;

import com.google.common.collect.Lists;
import com.ppxb.la.admin.constant.AdminCacheConst;
import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentTreeVO;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentVO;
import com.ppxb.la.base.common.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentCacheManager {

    private final DepartmentDao departmentDao;

    @CacheEvict(value = {
            AdminCacheConst.Department.DEPARTMENT_LIST_CACHE,
            AdminCacheConst.Department.DEPARTMENT_MAP_CACHE,
            AdminCacheConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE,
            AdminCacheConst.Department.DEPARTMENT_TREE_CACHE,
            AdminCacheConst.Department.DEPARTMENT_PATH_CACHE}, allEntries = true)
    public void clearCache() {
        log.info("Cleared all department caches");
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
        return getDepartmentList()
                .stream()
                .collect(Collectors.toMap(DepartmentVO::getDepartmentId, Function.identity()));
    }

    /**
     * 缓存部门树结构
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_TREE_CACHE)
    public List<DepartmentTreeVO> getDepartmentTree() {
        return buildTree(getDepartmentList());
    }

    /**
     * 缓存某个部门的下级id列表
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE)
    public List<Long> getDepartmentSelfAndChildren(Long departmentId) {
        return selfAndChildrenIdList(departmentId, getDepartmentList());
    }

    /**
     * 部门的路径名称
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_PATH_CACHE)
    public Map<Long, String> getDepartmentPathMap() {
        var departmentMap = getDepartmentMap();
        return departmentMap
                .values()
                .stream()
                .collect(Collectors.toMap(DepartmentVO::getDepartmentId, dept -> buildDepartmentPath(dept, departmentMap)));
    }

    /**
     * 构建父级考点路径
     */
    private String buildDepartmentPath(DepartmentVO departmentVO, Map<Long, DepartmentVO> departmentMap) {
        var pathParts = new ArrayList<String>();
        var current = departmentVO;
        while (current != null && !Objects.equals(current.getParentId(), NumberUtils.LONG_ZERO)) {
            pathParts.addFirst(current.getName());
            current = departmentMap.get(current.getParentId());
        }
        pathParts.addFirst(departmentVO.getName());
        return String.join("/", pathParts);
    }

    /**
     * 构建部门树结构
     */
    public List<DepartmentTreeVO> buildTree(List<DepartmentVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Lists.newArrayList();
        }

        var nodeMap = voList
                .stream()
                .map(vo -> {
                    var treeVO = new DepartmentTreeVO();
                    BeanUtil.copyProperties(vo, treeVO);
                    return treeVO;
                })
                .collect(Collectors.toMap(DepartmentTreeVO::getDepartmentId, Function.identity()));

        var rootNodes = new ArrayList<DepartmentTreeVO>();
        nodeMap.values().forEach(node -> {
            if (node.getParentId() == null || Objects.equals(node.getParentId(), NumberUtils.LONG_ZERO)) {
                rootNodes.add(node);
            } else {
                var parentNode = nodeMap.get(node.getParentId());
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(Lists.newArrayList());
                    }
                    parentNode.getChildren().add(node);
                }
            }
        });

        rootNodes.sort(Comparator.comparing(DepartmentTreeVO::getSort).reversed());
        setTreeNodeRelations(rootNodes);
        return rootNodes;
    }

    private void setTreeNodeRelations(List<DepartmentTreeVO> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            var node = nodes.get(i);
            if (i > 0) {
                node.setPreId(nodes.get(i - 1).getDepartmentId());
            }
            if (i < nodes.size() - 1) {
                node.setNextId(nodes.get(i + 1).getDepartmentId());
            }
            if (node.getChildren() != null) {
                setTreeNodeRelations(node.getChildren());
            }
            node.setSelfAndAllChildrenIdList(getAllChildrenIds(node));
        }
    }

    private List<Long> getAllChildrenIds(DepartmentTreeVO node) {
        var ids = new ArrayList<Long>();
        ids.add(node.getDepartmentId());
        if (node.getChildren() != null) {
            for (DepartmentTreeVO child : node.getChildren()) {
                ids.addAll(getAllChildrenIds(child));
            }
        }
        return ids;
    }

    /**
     * 通过部门id,获取当前以及下属部门
     */
    public List<Long> selfAndChildrenIdList(Long departmentId, List<DepartmentVO> voList) {
        var result = new HashSet<Long>();
        result.add(departmentId);
        collectChildrenIds(departmentId, voList, result);
        return result.stream().toList();
    }

    private void collectChildrenIds(Long departmentId, List<DepartmentVO> voList, Set<Long> result) {
        voList
                .stream()
                .filter(vo -> Objects.equals(vo.getParentId(), departmentId))
                .forEach(vo -> {
                    result.add(vo.getDepartmentId());
                    collectChildrenIds(vo.getDepartmentId(), voList, result);
                });
    }
}
