package com.valuelab.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.valuelab.entity.MstUser;

@Mapper
public interface MstUsersRepository {

        @Select("SELECT * FROM mst_users LIMIT 1")
        public List<MstUser> selectFirst();

        @Select({
                        "<script>",
                        "SELECT ",
                        "  uuid, ",
                        "  user_id, ",
                        "  user_name, ",
                        "  mail_address, ",
                        "  user_type, ",
                        "  updated_datetime ",
                        "FROM mst_users ",
                        "WHERE is_deleted = false ",
                        "  AND user_type != '9' ",
                        "<if test='userId != null and userId != \"\"'>",
                        " AND user_id LIKE CONCAT('%',#{userId},'%') ",
                        "</if>",
                        "<if test='userName != null and userName != \"\"'>",
                        " AND user_name LIKE CONCAT('%',#{userName},'%') ",
                        "</if>",
                        "<if test='mailAddress != null and mailAddress != \"\"'>",
                        " AND mail_address LIKE CONCAT('%',#{mailAddress},'%') ",
                        "</if>",
                        "<choose>",
                        " <when test='userType == null'>",
                        " AND user_type IN ('0', '1') ",
                        " </when>",
                        " <when test='userType == \"0\"'>",
                        " AND user_type = '0' ",
                        " </when>",
                        " <when test='userType == \"1\"'>",
                        " AND user_type = '1' ",
                        " </when>",
                        "</choose>",
                        "ORDER BY user_id",
                        "</script>"
        })
        public List<MstUser> selectUser(
                        String userId,
                        String userName,
                        String mailAddress,
                        String userType);

        @Select({
                        "<script>",
                        "SELECT ",
                        "  user_pk, ",
                        "  user_id, ",
                        "  user_name, ",
                        "  mail_address, ",
                        "  user_type, ",
                        "  uuid, ",
                        "  updated_datetime ",
                        "FROM mst_users ",
                        "WHERE is_deleted = false ",
                        // " AND user_type != '9' ",
                        "<if test='uuid != null and uuid != \"\"'>",
                        " AND uuid = #{uuid}",
                        "</if>",
                        "ORDER BY user_pk LIMIT 1 ",
                        "</script>"
        })
        public MstUser selectUserDetails(
                        String uuid);

        @Select({
                        "<script>",
                        "SELECT ",
                        "  user_pk, ",
                        "  user_id, ",
                        "  user_name, ",
                        "  mail_address, ",
                        "  user_type, ",
                        "  uuid, ",
                        "  updated_datetime ",
                        "FROM mst_users ",
                        "WHERE is_deleted = true ",
                        "  AND user_type != '9' ",
                        "<if test='uuid != null and uuid != \"\"'>",
                        " AND uuid = #{uuid}",
                        "</if>",
                        "ORDER BY user_pk LIMIT 1 ",
                        "</script>"
        })
        public MstUser selectUserDetailsForDeletedUser(
                        String uuid);

        // 1件追加
        /**
         * @param mstUser MstUser: userId, userName, userType, mailAddress, uuid,
         *                updatedDatetime
         */
        @Options(useGeneratedKeys = true, keyProperty = "userPk")
        @Insert("INSERT INTO public.mst_users(user_id, user_name, user_type, mail_address, uuid, created_datetime,created_user_pk, updated_datetime, updated_user_pk) VALUES(#{userId}, #{userName}, #{userType}, #{mailAddress}, #{uuid}, #{createdDatetime}, #{createdUserPk},#{updatedDatetime},#{updatedUserPk})")
        public int insertMstUser(MstUser mstUser);

        // 1件更新
        @Update("UPDATE public.mst_users SET user_id = #{userId}, user_name = #{userName}, user_type = #{userType}, mail_address = #{mailAddress}, updated_datetime = #{newUpdatedDatetime}, updated_user_pk= #{updatedUserPk} WHERE is_deleted = false AND uuid = #{uuid} AND updated_datetime = #{oldUpdatedDatetime}")
        public int updateMstUser(String userId, String userName, String userType, String mailAddress, String uuid,
                        LocalDateTime oldUpdatedDatetime, int updatedUserPk, LocalDateTime newUpdatedDatetime);

        // 1件更新, is_deleted = true
        @Update("UPDATE public.mst_users SET is_deleted = true WHERE uuid = #{uuid} AND updated_datetime::date = #{updatedDatetime}::date AND is_deleted = false")
        public int deleteMstUser(MstUser mstUser);

        // search user's count
        @Select({
                        "<script>",
                        "SELECT ",
                        "  COUNT(*) ",
                        "FROM mst_users ",
                        "WHERE is_deleted = false ",
                        "  AND user_id = #{userId} ",
                        "</script>"
        })
        public int checkUserCountByUserId(String userId);

        // search user's count
        @Select({
                        "<script>",
                        "SELECT ",
                        "  COUNT(*) ",
                        "FROM mst_users ",
                        "WHERE is_deleted = false ",
                        "  AND uuid = #{uuid} ",
                        "</script>"
        })
        public int checkUserCountByUuid(String uuid);

        // search user's count
        @Select({
                        "<script>",
                        "SELECT ",
                        "  *",
                        "FROM mst_users ",
                        "WHERE is_deleted = false ",
                        "  AND user_id = #{userId} ",
                        "</script>"
        })
        public List<MstUser> getMstUserByUserId(String userId);

        @Select({
                        "<script>",
                        "SELECT ",
                        "  user_name",
                        "FROM mst_users ",
                        "WHERE is_deleted = false ",
                        "  AND user_pk = #{userPk} ",
                        "</script>"
        })
        public String selectUserNameByUserPk(Long userPk);
}
