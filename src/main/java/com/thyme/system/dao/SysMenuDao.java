package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.system.entity.SysMenu;
import com.thyme.system.vo.MenuNameVO;
import com.thyme.system.vo.SysMenuVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thyme
 * @ClassName SysMenuDao
 * @Description TODO
 * @Date 2019/12/19 15:44
 */
@Repository
public interface SysMenuDao extends BaseMapper<SysMenu> {

    @Select("SELECT *  FROM sys_menu as m LEFT JOIN sys_menu_role as r ON m.id = r.menu_id WHERE m.is_show = 'true' and r.role_id = #{roleId} ORDER BY m.menu_weight")
    List<SysMenu> findByRoleId(@Param("roleId") String roleId);


    @Select("SELECT * FROM sys_menu WHERE menu_level = 1 ORDER BY menu_weight")
    IPage<SysMenu> findFirstMenu(Page page);

    @Select("SELECT * FROM sys_menu WHERE parent_id = #{parentId} ORDER BY menu_weight")
    List<SysMenu> findByParentId(@Param("parentId") String parentId);

    /**
     * 修改菜单
     * @param sysMenu 菜单
     * @return 返回值
     */
    @Update("update sys_menu set parent_id = #{parentId}, menu_name = #{menuName}, menu_code = #{menuCode}, menu_href = #{menuHref}, " +
            "menu_level = #{menuLevel}, menu_weight = #{menuWeight}, menu_icon = #{menuIcon}, is_show = #{isShow} where id = #{id}")
    int updateMenu(SysMenuVO sysMenu);

    /**
     * 添加菜单
     * @param sysMenu 菜单
     * @return 返回值
     */
    @Insert("insert into sys_menu (id,parent_id,menu_name,menu_code,menu_href,menu_level,menu_weight,menu_icon,is_show,create_date,create_by) values" +
            "(#{id}, #{parentId}, #{menuName}, #{menuCode}, #{menuHref}, #{menuLevel}, #{menuWeight}, #{menuIcon}, #{isShow}, #{createDate}, #{createBy})")
    int addMenu(SysMenuVO sysMenu);

    /**
     * 查询菜单
     * @param menuName 菜单名称
     * @param menuCode 菜单别名
     * @param menuHref 菜单链接
     * @return 返回值
     */
    @Select({"<script>" +
            "select * from sys_menu where menu_name = #{menuName} or menu_code = #{menuCode} " +
                "<when test=\"menuHref != null and menuHref != ''\"> or menu_href = #{menuHref}</when>" +
            "</script>"})
    SysMenu getByName(@Param("menuName")String menuName, @Param("menuCode")String menuCode, @Param("menuHref")String menuHref);


    /**
     * 根据id查询菜单
     * @param id id
     * @return 菜单
     */
    @Select("select * from sys_menu where id = #{id}")
    SysMenu getById(@Param("id")String id);

    /**
     * 获取一级菜单
     * @return 一级菜单
     */
    @Select("SELECT * FROM sys_menu WHERE menu_level = 1 ORDER BY menu_weight")
    List<SysMenu> getFirstMenu();

    /**
     * 获取二级菜单
     */
    @Select("SELECT * FROM sys_menu WHERE menu_level = 2")
    List<SysMenu> getSecondMenu();

    /**
     * 根据角色id查询所有父级菜单id
     * @param roleId 角色id
     * @return 父级菜单id
     */
    @Select("SELECT DISTINCT menu.id FROM sys_menu AS menu " +
            "LEFT JOIN sys_menu_role AS role ON menu.id = role.menu_id " +
            "WHERE menu_level = 1 AND role.role_id = #{roleId} " +
            " ORDER BY menu_weight")
    List<String> getRoleMenu(@Param("roleId")String roleId);

    /**
     * 获取菜单层级
     * @return 菜单登记
     */
    @Select("select distinct menu_level from sys_menu order by menu_level asc")
    List<String> getMenuLevel();

    /**
     * 查询当前菜单的上级菜单
     * @param menuLevel 上级菜单层级
     * @return 上级菜单名称
     */
    @Select("select id, menu_name from sys_menu where menu_level = #{menuLevel} order by create_date")
    List<MenuNameVO> getPreviousMenu(@Param("menuLevel")String menuLevel);

    /**
     * 根据菜单名称查询菜单id
     * @param menuNames 菜单名称
     * @return 菜单id
     */
    @Select("select id from sys_menu where menu_name = #{menuNames}")
    String getByMenuName(@Param("menuNames")String menuNames);

    /**
     * 根据id删除菜单(若为一级菜单id删除其子菜单)
     * @param id id
     * @return 返回值
     */
    @Delete("delete from sys_menu where id = #{id} or parent_id = #{id}")
    int deleteMenuById(String id);

    /*
     * 通过自己的id查询父级菜单
     * @Date 2020/6/1 1:31
     * @Param 自己的id
     * @return SysMenu
     **/
    @Select("select * from sys_menu where parent_id = #{id}")
    List<SysMenu> getPreviousMenuById(String id);

    /*
     * 根据连接获取菜单名称（用于显示导航）
     * @Date 2020/5/30 12:28
     * @Param [menuHref]
     * @return SysMenu
     **/
    @Select("select * from sys_menu where menu_href = #{menuHref}")
    SysMenu getByHref(@Param("menuHref") String menuHref);

    /*
     * 根据搜索名称模糊查询菜单
     * @Date 2020/5/30 19:44
     * @Param [menuName]
     * @return SysMenu>
     **/
    @Select("SELECT * FROM sys_menu WHERE 1=1 AND menu_name LIKE CONCAT('%',#{menuName},'%')")
    IPage<SysMenu> searchMenu(Page page ,@Param("menuName") String menuName);

    /*
     * 只通过开关控制显示
     * @Date 2020/5/31 12:15
     * @Param  对应菜单id
     * @return
     **/
    @Update("UPDATE sys_menu SET is_show = #{stat} WHERE id = #{id}")
    int updateShow(@Param("id") String id , @Param("stat") String stat);

    @Select("<script>" +
            "SELECT m.* FROM sys_menu AS m " +
            "<when test=\"authority != 'ROLE_DEVELOPER' \">" +
            "LEFT JOIN sys_menu_role AS mr ON m.id = mr.menu_id LEFT JOIN sys_role AS r ON r.id = mr.role_id WHERE r.authority = #{authority} " +
            "</when>" +
            "</script>")
    List<SysMenu> getAllMenus(@Param("authority") String authority);
}
