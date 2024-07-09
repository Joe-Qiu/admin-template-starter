package com.treeview.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.treeview.entity.system.MenuConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuConfigMapper extends BaseMapper<MenuConfig> {
    @Select({"SELECT DISTINCT mc.resource FROM user_role er   " +
            "   INNER JOIN role_define rd   " +
            "       ON rd.id = er.role_id   " +
            "   INNER JOIN role_menu rm   " +
            "       ON rm.role_id = er.role_id  " +
            "   INNER JOIN menu_config mc" +
            "       ON mc.id = rm.menu_id " +
            "WHERE er.user_id = #{uid} " +
            "   AND rd.state = 1 AND mc.resource IS NOT NULL "})
    @ResultType(String.class)
    List<String> selectMenuIdsByUserId(@Param("uid") Long var1);

    @Select({"SELECT DISTINCT mc.resource FROM user_role er " +
            "   LEFT JOIN role_menu rm " +
            "       ON er.role_id = rm.role_id " +
            "   LEFT JOIN menu_config mc " +
            "       ON mc.id = rm.menu_id " +
            "   WHERE er.user_id = #{uid} " +
            "       AND mc.resource IS NOT NULL AND mc.resource != '' "})
    @ResultType(String.class)
    List<String> selectResourceByUid(@Param("uid") Long var1);
}