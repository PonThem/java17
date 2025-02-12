package com.valuelab.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.valuelab.entity.User;

@Mapper
public interface UserRepository {

    // 全件取得
    @Select("SELECT * FROM public.user")
    public List<User> selectAll();

    // 1件取得
    @Select("SELECT * FROM public.user WHERE user_id = #{id}")
    public User selectByUserId(Long id);

    // 1件取得
    @Select("SELECT * FROM public.user WHERE custom_uuid = #{customUuid}")
    public User selectByCustomUuid(String customUuid);

    // 1件更新
    @Update("UPDATE public.user SET cognito_sub_id = #{cognitoSubId}, role = #{role}, email = #{email}, username = #{username}, custom_uuid = #{customUuid} WHERE user_id = #{userId}")
    public int updateUserByUser(User user);

    // 1件追加
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    @Insert("INSERT INTO public.user(cognito_sub_id,role,email,username,custom_uuid) VALUES(#{cognitoSubId},#{role},#{email},#{username},#{customUuid})")
    public int insertUserByUser(User user);

    // 1件削除
    @Delete("DELETE FROM public.user WHERE user_id = #{id}")
    public int deleteUserByUserId(Long id);

    /**
     * Authenticates a user with the given customUuid.
     * 
     * @param customUuid The customUuid of the user.
     * @return A boolean value indicating whether the user is authenticated.
     */
    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM public.user WHERE custom_uuid = #{customUuid} AND role = 'admin' ")
    public boolean isAdminRoleByCustomUuid(String customUuid) throws Exception;

}
