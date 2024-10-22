package com.ppxb.la.admin.module.system.menu.service;

import com.ppxb.la.admin.module.system.menu.constant.MenuTypeEnum;
import com.ppxb.la.admin.module.system.menu.dao.MenuDao;
import com.ppxb.la.admin.module.system.menu.domain.entity.MenuEntity;
import com.ppxb.la.admin.module.system.menu.domain.form.MenuAddForm;
import com.ppxb.la.admin.module.system.menu.domain.form.MenuBaseForm;
import com.ppxb.la.admin.module.system.menu.domain.form.MenuUpdateForm;
import com.ppxb.la.admin.module.system.menu.domain.vo.MenuTreeVO;
import com.ppxb.la.admin.module.system.menu.domain.vo.MenuVO;
import com.ppxb.la.base.common.code.SystemErrorCode;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Resource
    private MenuDao menuDao;
//
//    @Resource
//    private List<RequestUrlVO> authUrl;

    public synchronized ResponseDTO<String> addMenu(MenuAddForm menuAddForm) {
        if (this.validateMenuName(menuAddForm)) {
            return ResponseDTO.userErrorParam("菜单名称已存在");
        }
        if (this.validateWebPerms(menuAddForm)) {
            return ResponseDTO.userErrorParam("前端权限字符串已存在");
        }
        MenuEntity menuEntity = BeanUtil.copy(menuAddForm, MenuEntity.class);
        menuDao.insert(menuEntity);
        return ResponseDTO.ok();
    }

    public synchronized ResponseDTO<String> updateMenu(MenuUpdateForm menuUpdateForm) {
        MenuEntity selectMenu = menuDao.selectById(menuUpdateForm.getMenuId());
        if (selectMenu == null) {
            return ResponseDTO.userErrorParam("菜单不存在");
        }
        if (selectMenu.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("菜单已被删除");
        }
        if (this.validateMenuName(menuUpdateForm)) {
            return ResponseDTO.userErrorParam("菜单名称已存在");
        }
        if (this.validateWebPerms(menuUpdateForm)) {
            return ResponseDTO.userErrorParam("前端权限字符串已存在");
        }
        if (menuUpdateForm.getMenuId().equals(menuUpdateForm.getParentId())) {
            return ResponseDTO.userErrorParam("父级菜单不能为自己");
        }
        MenuEntity menuEntity = BeanUtil.copy(menuUpdateForm, MenuEntity.class);
        menuDao.updateById(menuEntity);
        return ResponseDTO.ok();
    }

    public synchronized ResponseDTO<String> batchDeleteMenu(List<Long> menuIdList, Long userId) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return ResponseDTO.userErrorParam("选择菜单不能为空");
        }
        menuDao.deleteByMenuIdList(menuIdList, userId, Boolean.TRUE);
        this.recursiveDeleteChildren(menuIdList, userId);
        return ResponseDTO.ok();
    }

    private void recursiveDeleteChildren(List<Long> menuIdList, Long userId) {
        List<Long> childrenMenuIdList = menuDao.selectMenuIdByParentIdList(menuIdList);
        if (CollectionUtils.isEmpty(childrenMenuIdList)) {
            return;
        }
        menuDao.deleteByMenuIdList(childrenMenuIdList, userId, Boolean.TRUE);
        recursiveDeleteChildren(childrenMenuIdList, userId);
    }

    public <T extends MenuBaseForm> Boolean validateMenuName(T menuDTO) {
        MenuEntity menu = menuDao.getByMenuName(menuDTO.getMenuName(), menuDTO.getParentId(), Boolean.FALSE);
        if (menuDTO instanceof MenuAddForm) {
            return menu != null;
        }
        if (menuDTO instanceof MenuUpdateForm) {
            Long menuId = ((MenuUpdateForm) menuDTO).getMenuId();
            return menu != null && menu.getMenuId().longValue() != menuId.longValue();
        }
        return true;
    }

    public <T extends MenuBaseForm> Boolean validateWebPerms(T menuDTO) {
        MenuEntity menu = menuDao.getByWebPerms(menuDTO.getWebPerms(), Boolean.FALSE);
        if (menuDTO instanceof MenuAddForm) {
            return menu != null;
        }
        if (menuDTO instanceof MenuUpdateForm) {
            Long menuId = ((MenuUpdateForm) menuDTO).getMenuId();
            return menu != null && menu.getMenuId().longValue() != menuId.longValue();
        }
        return true;
    }

    public List<MenuVO> queryMenuList(Boolean disabledFlag) {
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, disabledFlag, null);
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        return this.filterNoParentMenu(parentMap, NumberUtils.LONG_ZERO);
    }

    private List<MenuVO> filterNoParentMenu(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        List<MenuVO> result = parentMap.getOrDefault(parentId, Lists.newArrayList());
        List<MenuVO> childMenu = Lists.newArrayList();
        result.forEach(e -> {
            List<MenuVO> menuLis = this.filterNoParentMenu(parentMap, e.getMenuId());
            childMenu.addAll(menuLis);
        });
        result.addAll(childMenu);
        return result;
    }

    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(Boolean onlyMenu) {
        List<Integer> menuTypeList = Lists.newArrayList();
        if (onlyMenu) {
            menuTypeList = Lists.newArrayList(MenuTypeEnum.CATALOG.getValue(), MenuTypeEnum.MENU.getValue());
        }
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, null, menuTypeList);
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuTreeVO> menuTreeVOList = this.buildMenuTree(parentMap, NumberUtils.LONG_ZERO);
        return ResponseDTO.ok(menuTreeVOList);
    }

    private List<MenuTreeVO> buildMenuTree(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        List<MenuTreeVO> result = parentMap.getOrDefault(parentId, Lists.newArrayList()).stream()
                .map(e -> BeanUtil.copy(e, MenuTreeVO.class)).toList();
        result.forEach(e -> {
            e.setChildren(this.buildMenuTree(parentMap, e.getParentId()));
        });
        return result;
    }

    public ResponseDTO<MenuVO> getMenuDetail(Long menuId) {
        MenuEntity selectMenu = menuDao.selectById(menuId);
        if (selectMenu == null) {
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "菜单不存在");
        }
        if (selectMenu.getDeletedFlag()) {
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "菜单已被删除");
        }
        MenuVO menuVO = BeanUtil.copy(selectMenu, MenuVO.class);
        return ResponseDTO.ok(menuVO);
    }

//    public ResponseDTO<List<RequestUrlVO>> getAuthUrl() {
//        return ResponseDTO.ok(authUrl);
//    }
}
