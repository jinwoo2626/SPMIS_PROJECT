package dongyang.spmis.service;

import dongyang.spmis.model.Project.ProjectNoticeDTO;
import dongyang.spmis.model.Project.WebhookLinkDTO;
import dongyang.spmis.properties.WHProperties;

import java.util.ArrayList;

public interface WebHookService {

    public WHProperties callEvent(int project_id, String Message);

    // webhook 링크 추가 (webhook link, project_id)
    public WHProperties addWebhookLink(int project_id, String webhook_link);

    // webhook 링크 삭제 (webhook )
    public boolean deleteWebhookLink(int project_id, String link);

    // webhook 링크 조회 (project_id)
    ArrayList<WebhookLinkDTO> FindWebhookLinkByProjectID(int project_id);

    // 알림 기록 가져오기(project_id)
    ArrayList<ProjectNoticeDTO> FindNoticeByProjectID(int project_id);

    // webhook 링크 수정
    boolean editWebhook(int project_id, String link);

}
