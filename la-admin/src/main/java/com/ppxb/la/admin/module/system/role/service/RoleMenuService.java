package com.ppxb.la.admin.module.system.role.service;

import com.ppxb.la.admin.module.system.menu.dao.MenuDao;
import com.ppxb.la.admin.module.system.menu.domain.entity.MenuEntity;
import com.ppxb.la.admin.module.system.menu.domain.vo.MenuSimpleTreeVO;
import com.ppxb.la.admin.module.system.menu.domain.vo.MenuVO;
import com.ppxb.la.admin.module.system.role.dao.RoleDao;
import com.ppxb.la.admin.module.system.role.dao.RoleMenuDao;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleEntity;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleMenuEntity;
import com.ppxb.la.admin.module.system.role.domain.form.RoleMenuUpdateForm;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleMenuTreeVO;
import com.ppxb.la.admin.module.system.role.manager.RoleMenuManager;
import com.ppxb.la.base.common.code.UserErrorCode;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleMenuService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private RoleMenuDao roleMenuDao;

    @Resource
    private RoleMenuManager roleMenuManager;

    @Resource
    private MenuDao menuDao;

    /**
     * 更新角色权限
     *
     * @param updateForm 更新DTO
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRoleMenu(RoleMenuUpdateForm updateForm) {
        Long roleId = updateForm.getRoleId();
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (roleEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        List<RoleMenuEntity> roleMenuEntityList = updateForm.getMenuIdList()
                .stream()
                .map(menuId -> {
                    RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
                    roleMenuEntity.setRoleId(roleId);
                    roleMenuEntity.setMenuId(menuId);
                    return roleMenuEntity;
                })
                .toList();

        roleMenuManager.updateRoleMenu(roleId, roleMenuEntityList);
        return ResponseDTO.ok();
    }


    /**
     * 根据角色id集合，查询其所有的菜单权限
     *
     * @param roleIdList        角色ID列表
     * @param administratorFlag 是否为管理员
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    public List<MenuVO> getMenuList(List<Long> roleIdList, Boolean administratorFlag) {
        if (administratorFlag) {
            List<MenuEntity> menuEntityList = roleMenuDao.selectMenuListByRoleIdList(Lists.newArrayList(), false);
            return BeanUtil.copyList(menuEntityList, MenuVO.class);
        }

        if (CollectionUtils.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        List<MenuEntity> menuEntityList = roleMenuDao.selectMenuListByRoleIdList(roleIdList, false);
        return BeanUtil.copyList(menuEntityList, MenuVO.class);
    }

    /**
     * 获取角色关联菜单权限
     *
     * @param roleId 角色ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    public ResponseDTO<RoleMenuTreeVO> getRoleSelectedMenu(Long roleId) {
        RoleMenuTreeVO result = new RoleMenuTreeVO();
        List<Long> selectedMenuId = roleMenuDao.queryMenuIdByRoleId(roleId);
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, Boolean.FALSE, null);
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream()
                .collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuSimpleTreeVO> menuTreeList = buildMenuTree(parentMap, NumberUtils.LONG_ZERO);
        result.setRoleId(roleId);
        result.setSelectedMenuId(selectedMenuId);
        result.setMenuTreeList(menuTreeList);
        return ResponseDTO.ok(result);
    }

    /**
     * 构建菜单树
     *
     * @param parentMap 菜单Map
     * @param parentId  父级菜单ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    private List<MenuSimpleTreeVO> buildMenuTree(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        return parentMap.getOrDefault(parentId, Lists.newArrayList())
                .stream()
                .map(e -> {
                    MenuSimpleTreeVO treeVO = BeanUtil.copy(e, MenuSimpleTreeVO.class);
                    treeVO.setChildren(buildMenuTree(parentMap, e.getMenuId()));
                    return treeVO;
                })
                .toList();
    }
}
