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

    /**
     * 添加菜单
     *
     * @param menuAddForm 添加菜单DTO
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
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

    /**
     * 更新菜单
     *
     * @param menuUpdateForm 更新菜单DTO
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
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

    /**
     * 批量删除菜单
     *
     * @param menuIdList 目录ID列表
     * @param userId     用户ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    public synchronized ResponseDTO<String> batchDeleteMenu(List<Long> menuIdList, Long userId) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return ResponseDTO.userErrorParam("选择菜单不能为空");
        }
        menuDao.deleteByMenuIdList(menuIdList, userId, Boolean.TRUE);
        this.recursiveDeleteChildren(menuIdList, userId);
        return ResponseDTO.ok();
    }

    /**
     * 递归删除菜单
     *
     * @param menuIdList 目录ID列表
     * @param userId     用户ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    private void recursiveDeleteChildren(List<Long> menuIdList, Long userId) {
        List<Long> childrenMenuIdList = menuDao.selectMenuIdByParentIdList(menuIdList);
        if (CollectionUtils.isEmpty(childrenMenuIdList)) {
            return;
        }
        menuDao.deleteByMenuIdList(childrenMenuIdList, userId, Boolean.TRUE);
        recursiveDeleteChildren(childrenMenuIdList, userId);
    }

    /**
     * 校验菜单名称
     *
     * @param menuDTO 菜单DTO
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
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

    /**
     * 校验前端权限字符串
     *
     * @param menuDTO 菜单DTO
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
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

    /**
     * 查询菜单列表
     *
     * @param disabledFlag 是否禁用
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    public List<MenuVO> queryMenuList(Boolean disabledFlag) {
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, disabledFlag, null);
        Map<Long, List<MenuVO>> parentMap = menuVOList
                .stream()
                .collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        return this.filterNoParentMenu(parentMap, NumberUtils.LONG_ZERO);
    }


    /**
     * 过滤没有父级菜单的菜单列表
     *
     * @param parentMap 父级菜单Map
     * @param parentId  父级菜单ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    private List<MenuVO> filterNoParentMenu(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        List<MenuVO> result = parentMap.getOrDefault(parentId, Lists.newArrayList());
        List<MenuVO> childMenu = Lists.newArrayList();
        result.forEach(menu -> {
            List<MenuVO> menuLis = this.filterNoParentMenu(parentMap, menu.getMenuId());
            childMenu.addAll(menuLis);
        });
        result.addAll(childMenu);
        return result;
    }

    /**
     * 查询菜单树
     *
     * @param onlyMenu 是否查询功能点
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(Boolean onlyMenu) {
        List<Integer> menuTypeList = Lists.newArrayList();
        if (onlyMenu) {
            menuTypeList = Lists.newArrayList(MenuTypeEnum.CATALOG.getValue(), MenuTypeEnum.MENU.getValue());
        }
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, null, menuTypeList);
        Map<Long, List<MenuVO>> parentMap = menuVOList
                .stream()
                .collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuTreeVO> menuTreeVOList = this.buildMenuTree(parentMap, NumberUtils.LONG_ZERO);
        return ResponseDTO.ok(menuTreeVOList);
    }

    /**
     * 构建菜单树
     *
     * @param parentMap 父级菜单Map
     * @param parentId  父级菜单ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    private List<MenuTreeVO> buildMenuTree(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        List<MenuTreeVO> result = parentMap
                .getOrDefault(parentId, Lists.newArrayList())
                .stream()
                .map(menu -> BeanUtil.copy(menu, MenuTreeVO.class)).toList();
        result.forEach(menu -> {
            menu.setChildren(this.buildMenuTree(parentMap, menu.getParentId()));
        });
        return result;
    }

    /**
     * 查询菜单详情
     *
     * @param menuId 菜单ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
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
