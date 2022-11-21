package dongyang.spmis.mapper;

import dongyang.spmis.model.Project.DiscordLinkDTO;
import dongyang.spmis.model.Project.ProjectJoinDiscordDTO;
import dongyang.spmis.model.Project.ProjectNoticeDTO;
import dongyang.spmis.model.Project.WebhookLinkDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

public interface DiscordMapper extends DefaultDBInfo {

    @Select("Select * FROM " + PROJECT_NOTICE + " WHERE project_id=#{project_id}")
    ProjectNoticeDTO discordNoticeFindByProjectID(String project_id);

    @Insert("Insert into " + PROJECT_NOTICE + " VALUES(null,  #{project_id}, #{notice}, now() )")
    boolean addProjectNotice(@Param("project_id") int project_id,@Param("notice") String notice);

    @Select("Select * FROM " + DISCORD_LINK + " WHERE project_id=#{project_id}")
    DiscordLinkDTO discordLinkFindByProjectID(int project_id);

    @Select("Select dl.project_id, p.project_name, link FROM " + DISCORD_LINK + " as dl, "+ PROJECTJOIN +" as pj, " + PROJECT + " as p" +
            " WHERE dl.project_id = pj.project_id AND dl.project_id = p.project_id " +
            " AND pj.user_email=#{user_email}")
    ArrayList<ProjectJoinDiscordDTO> discordLinkFindByUserEmail(String user_email);

    @Insert("INSERT INTO " + DISCORD_LINK + " VALUES (#{project_id}, #{link} )")
    boolean saveDiscordLink(DiscordLinkDTO discordLinkDTO);

    @Delete("DELETE FROM " + DISCORD_LINK + " WHERE project_id=#{project_id} AND link=#{link}")
    boolean deleteDiscordLink(DiscordLinkDTO discordLinkDTO);

    @Insert("INSERT INTO " + WEBHOOK_LINK + " VALUES (#{project_id}, #{webhook_link})")
    boolean addWebhookLink(@Param("project_id") int project_id,@Param("webhook_link") String webhook_link);

    @Select("Select * from " + WEBHOOK_LINK + " WHERE project_id=#{project_id} ")
    ArrayList<WebhookLinkDTO> findWebhookByProjectID(int project_id);

    @Select("Select * from " + WEBHOOK_LINK + " WHERE project_id=#{project_id} AND webhook_link=#{webhook_link}")
    WebhookLinkDTO findOneWebhookByProjectID(@Param("project_id") int project_id,@Param("webhook_link") String webhook_link);

    @Select("Select  from " + WEBHOOK_LINK + " WHERE (#{project_id})")
    ArrayList<WebhookLinkDTO> findWebhookByProjectIdAndUser(int project_id, String user_email);

    @Update("UPDATE" + DISCORD_LINK + " SET webhook_link=#{webhook_link}  WHERE project_id=#{project_id}")
    boolean updateWebhook(int project_id, String webhook_link);

    @Delete("DELETE FROM " + WEBHOOK_LINK + " WHERE project_id=#{project_id} AND webhook_link=#{webhook_link}")
    boolean deleteWebhook(int project_id, String webhook_link);

    @Select("SELECT * FROM " + PROJECT_NOTICE + " WHERE project_id=#{project_id}")
    ArrayList<ProjectNoticeDTO> findProjectNoticeByProject_id(int Project_id);

    @Select ("SELECT project_id FROM " + PROJECT_NOTICE + " ORDER BY project_id desc limit 1 ")
    int findLastSaveProjectNoticeId();

    @Insert("INSERT INTO "+ NOTICE_CHECK + " VALUES(null, #{user_email}, #{notice_id}, false )")
    boolean saveNoticeCheck(@Param("user_email")String user_email, @Param("notice_id")int notice_id);

}
