package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.system.entity.SysUser;
import com.thyme.system.vo.UserVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thyme
 * @ClassName SysUserDao
 * @Description TODO
 * @Date 2019/12/12 21:50
 */
@Repository
public interface SysUserDao extends BaseMapper<SysUser> {

    /**
     * 根据姓名查询
     * @param name 姓名
     * @return 用户单个实例
     */
    @Select("SELECT * FROM sys_user WHERE name= #{name}")
    SysUser findByName(@Param("name") String name);

    /*
     * @Result指定返回结果集
     * @Date 2020/5/22 19:54
     * @Param [name, id]
     * @return int
     **/
    @Select("SELECT count(*) AS count FROM sys_user WHERE name= #{name} AND id= #{id}")
    @ResultType(Integer.class)
    int countByName(@Param("name") String name,
                    @Param("id")String id);

    /**
     * 查询所有用户
     * @param page 分页数据
     * @return 所有用户集合
     */
    @Select("<script>" +
            "SELECT u.* , r.name AS userRole " +
            "FROM sys_user AS u  " +
            "LEFT JOIN sys_user_role AS ur ON u.id = ur.user_id " +
            "LEFT JOIN sys_role AS r ON r.id  = ur.role_id " +
            "WHERE 1=1 "+
            "<when test=\" username !='' and username!=null\">" +
            "AND u.name LIKE CONCAT('%',#{username},'%') " +
            "</when>" +
            "ORDER BY u.create_time"+
            "</script>")
    IPage<UserVO> getAll(Page page,@Param("username")String username);

    /**
     * 根据id查用户
     * @param id id
     * @return 用户集合
     */
    @Select("select * from sys_user where id = #{id}")
    SysUser getById(@Param("id")String id);

    /**
     * 更新用户密码
     * @param password 密码
     * @param id id
     * @return 返回值
     */
    @Update("update sys_user set password = #{password} where id = #{id}")
    int updatePasswordById(@Param("password") String password,
                           @Param("id") String id);


    @Update("UPDATE sys_user set avatar=#{path} WHERE name = #{username}")
    int updateAvatar(@Param("username") String username,@Param("path") String path);
}
