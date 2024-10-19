package com.ppxb.la.admin.module.system.department.controller;

import com.ppxb.la.admin.constant.AdminSwaggerTagConst;
import com.ppxb.la.admin.module.system.department.domain.form.DepartmentAddForm;
import com.ppxb.la.admin.module.system.department.domain.form.DepartmentUpdateForm;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentTreeVO;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentVO;
import com.ppxb.la.admin.module.system.department.service.DepartmentService;
import com.ppxb.la.base.common.domain.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_DEPARTMENT)
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/department/treeList")
    @Operation(summary = "查询部门树形列表")
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        return departmentService.departmentTree();
    }

    @PostMapping("/department/add")
    @Operation(summary = "添加部门")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentAddForm addForm) {
        return departmentService.addDepartment(addForm);
    }

    @PostMapping("/department/update")
    @Operation(summary = "更新部门")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateForm updateForm) {
        return departmentService.updateDepartment(updateForm);
    }

    @GetMapping("/department/delete/{id}")
    @Operation(summary = "删除部门")
    public ResponseDTO<String> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }

    @GetMapping("/department/listAll")
    @Operation(summary = "查询部门列表")
    public ResponseDTO<List<DepartmentVO>> listAll() {
        return ResponseDTO.ok(departmentService.listAll());
    }
}
