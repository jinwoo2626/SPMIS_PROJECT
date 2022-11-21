package dongyang.spmis.service;

import dongyang.spmis.mapper.DiscordMapper;
import dongyang.spmis.mapper.ProjectMapper;
import dongyang.spmis.model.Project.ProjectJoinDTO;
import dongyang.spmis.model.Project.ProjectNoticeDTO;
import dongyang.spmis.model.Project.WebhookLinkDTO;
import dongyang.spmis.properties.WHProperties;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Service
public class WebHookServiceImpl implements WebHookService{

    @Autowired
    DiscordMapper discordMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public WHProperties callEvent(int project_id, String Message) {
        System.out.println("project_id = " + project_id);
        ArrayList<WebhookLinkDTO> linkSet = discordMapper.findWebhookByProjectID(project_id);
        // 공지 목록 추가
        discordMapper.addProjectNotice(project_id, Message);



        // 공지 id 가져오기
        int notice_id = discordMapper.findLastSaveProjectNoticeId();

        ArrayList<ProjectJoinDTO> joins = projectMapper.group(project_id);
        // 프로젝트 공지 정적 알림 추가
        for (ProjectJoinDTO join : joins) {
            if(join.getJoin_status().equals("admin") || join.getJoin_status().equals("member")){
                discordMapper.saveNoticeCheck(join.getUser_email(), notice_id);
            }
        }
        if(linkSet.isEmpty()){
            return WHProperties.FIND_NOT_LINK;
        }
        // webhook 알림 보내기
        for (WebhookLinkDTO webhookLinkDTO : linkSet) {

            JSONObject data = new JSONObject();

            data.put("content", Message);

            send(data, webhookLinkDTO.getWebhook_link());
        }
        return WHProperties.SUCCESS;

    }

    private void send(JSONObject object, String url){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);
        restTemplate.postForObject(url, entity, String.class);
    }
    @Override
    public WHProperties addWebhookLink(int project_id, String link) {
        if(discordMapper.findOneWebhookByProjectID(project_id, link) != null){
            return WHProperties.DUPLICATE_WEBHOOK_LINK;
        }
        discordMapper.addWebhookLink(project_id, link );


        return WHProperties.SUCCESS;
    }

    @Override
    public boolean deleteWebhookLink(int project_id, String link) {

        return discordMapper.deleteWebhook(project_id, link );
    }

    @Override
    public ArrayList<WebhookLinkDTO> FindWebhookLinkByProjectID(int project_id) {

        return discordMapper.findWebhookByProjectID(project_id);
    }

    @Override
    public ArrayList<ProjectNoticeDTO> FindNoticeByProjectID(int project_id) {
        return discordMapper.findProjectNoticeByProject_id(project_id);
    }

    @Override
    public boolean editWebhook(int project_id, String link) {
        return discordMapper.updateWebhook(project_id, link);
    }


}
