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
     * 通过登录名查询
     *
     * @param loginName    登录名
     * @param disabledFlag 禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    UserEntity getByLoginName(@Param("loginName") String loginName, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 通过姓名查询
     *
     * @param name         姓名
     * @param disabledFlag 禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    UserEntity getByName(@Param("name") String name, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 通过手机号码查询
     *
     * @param phone        手机号码
     * @param disabledFlag 禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    UserEntity getByPhone(@Param("phone") String phone, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取所有用户
     *
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<UserVO> listAll();

    /**
     * 获取部门用户数
     *
     * @param departmentId 部门ID
     * @param deletedFlag  禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 通过IDs获取用户
     *
     * @param userIds 用户IDs
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<UserVO> getUserByIds(@Param("userIds") Collection<Long> userIds);

    /**
     * 通过ID获取用户信息
     *
     * @param userId 用户ID
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    UserVO getUserById(@Param("userId") Long userId);

    /**
     * 通过部门ID获取用户
     *
     * @param departmentId 部门ID
     * @param disabledFlag 禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<UserEntity> selectByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 通过部门IDs和姓名查询指定用户
     *
     * @param departmentIdList 部门IDs
     * @param name             姓名
     * @param disabledFlag     禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<UserEntity> selectByName(@Param("departmentIdList") List<Long> departmentIdList,
                                  @Param("name") String name,
                                  @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 通过部门IDs获取用户
     *
     * @param departmentIdList 部门IDs
     * @param disabledFlag     禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<Long> getUserIdByDepartmentIdList(@Param("departmentIdList") List<Long> departmentIdList, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取全部用户
     *
     * @param leaveFlag    离职标志
     * @param disabledFlag 禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<Long> getUserId(@Param("leaveFlag") Boolean leaveFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 通过部门ID获取部门用户ID
     *
     * @param departmentId 部门ID
     * @param disabledFlag 禁用标志
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    List<Long> getUserIdByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 用户重置密码
     *
     * @param userId   用户ID
     * @param password 新密码
     * @author ppxb
     * @Date 2024/10/17 19:17
     */
    Integer updatePassword(@Param("userId") Long userId, @Param("password") String password);
}
