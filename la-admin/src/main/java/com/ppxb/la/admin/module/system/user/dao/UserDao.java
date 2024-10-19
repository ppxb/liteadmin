package com.ppxb.la.admin.module.system.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ppxb.la.admin.module.system.user.domain.entity.UserEntity;
import com.ppxb.la.admin.module.system.user.domain.form.UserQueryForm;
import com.ppxb.la.admin.module.system.user.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 查询用户列表
     *
     * @param page             分页器
     * @param queryForm        查询表单
     * @param departmentIdList 部门ID集合
     * @return {@link UserVO} 集合
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<UserVO> queryUser(Page<?> page, @Param("queryForm") UserQueryForm queryForm, @Param("departmentIdList") List<Long> departmentIdList);

    List<UserVO> selectUserByDisabledAndDeleted(@Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    void updateDisableFlag(@Param("userId") Long userId, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * [通过登录名查询]
     *
     * @param loginName    登录名
     * @param disabledFlag 禁用标志
     * @return {@link UserEntity}
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    UserEntity getByLoginName(@Param("loginName") String loginName, @Param("disabledFlag") Boolean disabledFlag);

    UserEntity getByName(@Param("name") String name, @Param("disabledFlag") Boolean disabledFlag);

    UserEntity getByPhone(@Param("phone") String phone, @Param("disabledFlag") Boolean disabledFlag);

    List<UserVO> listAll();

    Integer countByDepartmentId(@Param("departmentId") Long departmentId, @Param("deletedFlag") Boolean deletedFlag);

    List<UserVO> getUserByIds(@Param("userIds") Collection<Long> userIds);

    UserVO getUserById(@Param("userId") Long userId);

    List<UserEntity> selectByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);

    List<UserEntity> selectByName(@Param("departmentIdList") List<Long> departmentIdList, @Param("name") String name, @Param("disabledFlag") Boolean disabledFlag);

    List<Long> getUserIdByDepartmentIdList(@Param("departmentIdList") List<Long> departmentIdList, @Param("disabledFlag") Boolean disabledFlag);

    List<Long> getUserId(@Param("leaveFlag") Boolean leaveFlag, @Param("disabledFlag") Boolean disabledFlag);

    List<Long> getUserIdByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password);
}
