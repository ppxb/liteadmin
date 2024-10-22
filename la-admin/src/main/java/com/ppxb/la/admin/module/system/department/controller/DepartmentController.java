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

    @Operation(summary = "查询部门树形列表")
    @GetMapping("/department/treeList")
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        return departmentService.departmentTree();
    }

    @Operation(summary = "添加部门")
    @PostMapping("/department/add")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentAddForm addForm) {
        return departmentService.addDepartment(addForm);
    }

    @Operation(summary = "更新部门")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateForm updateForm) {
        return departmentService.updateDepartment(updateForm);
    }

    @Operation(summary = "删除部门")
    @GetMapping("/department/delete/{id}")
    public ResponseDTO<String> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }

    @Operation(summary = "查询部门列表")
    @GetMapping("/department/listAll")
    public ResponseDTO<List<DepartmentVO>> listAll() {
        return ResponseDTO.ok(departmentService.listAll());
    }
}
