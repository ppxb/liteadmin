package com.ppxb.la.admin.module.system.menu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppxb.la.admin.module.system.menu.domain.entity.MenuEntity;
import com.ppxb.la.admin.module.system.menu.domain.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MenuDao extends BaseMapper<MenuEntity> {

    MenuEntity getByMenuName(
            @Param("menuName") String menuName,
            @Param("parentId") Long parentId,
            @Param("deletedFlag") Boolean deletedFlag);

    MenuEntity getByWebPerms(@Param("webPerms") String webPerms, @Param("deletedFlag") Boolean deletedFlag);

    void deleteByMenuIdList(
            @Param("menuIdList") List<Long> menuIdList,
            @Param("updateUserId") Long updateUserId,
            @Param("deletedFlag") Boolean deletedFlag);

    List<MenuVO> queryMenuList(@Param("deletedFlag") Boolean deletedFlag,
                               @Param("disabledFlag") Boolean disabledFlag,
                               @Param("menuTypeList") List<Integer> menuTypeList);

    List<MenuEntity> getPointListByMenuId(
            @Param("menuId") Long menuId,
            @Param("menuType") Integer menuType,
            @Param("deletedFlag") Boolean deletedFlag);

    List<MenuVO> queryMenuByUserId(
            @Param("deletedFlag") Boolean deletedFlag,
            @Param("disabledFlag") Boolean disabledFlag,
            @Param("userId") Long userId);

    List<MenuEntity> queryMenuByType(@Param("menuType") Integer menuType,
                                     @Param("deletedFlag") Boolean deletedFlag,
                                     @Param("disabledFlag") Boolean disabledFlag);

    List<Long> selectMenuIdByParentIdList(@Param("menuIdList") List<Long> menuIdList);
}
