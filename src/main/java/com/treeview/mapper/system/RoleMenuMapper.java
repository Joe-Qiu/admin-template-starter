package com.treeview.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.treeview.entity.system.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Select({"SELECT DISTINCT rm.menu_id FROM user_role er " +
            "  INNER JOIN role_define rd " +
            "      ON rd.id = er.role_id " +
            "  INNER JOIN role_menu rm " +
            "       ON rm.role_id = er.role_id WHERE er.user_id = #{uid} AND rd.state = 1 ORDER BY rm.menu_id ASC; "})
    @ResultType(Long.class)
    List<Long> selectRoleMenuIdsByUserId(@Param("uid") Long uid);
}
