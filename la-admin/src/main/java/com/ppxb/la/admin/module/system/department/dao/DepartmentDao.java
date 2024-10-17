package com.ppxb.la.admin.module.system.department.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppxb.la.admin.module.system.department.domain.entity.DepartmentEntity;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DepartmentDao extends BaseMapper<DepartmentEntity> {

    Integer countSubDepartment(@Param("departmentId") Long departmentId);

    List<DepartmentVO> listAll();
}
