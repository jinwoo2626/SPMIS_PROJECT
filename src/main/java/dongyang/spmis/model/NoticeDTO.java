package dongyang.spmis.model;

import dongyang.spmis.model.MessageModel.MessageDTO;
import dongyang.spmis.model.Project.NoticeCheck;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
public class NoticeDTO {
    private ArrayList<NoticeCheck> noticeChecks;
    private ArrayList<MessageDTO> messages;

    @Builder
    public NoticeDTO(ArrayList<NoticeCheck> noticeChecks, ArrayList<MessageDTO> messages) {
        this.noticeChecks = noticeChecks;
        this.messages = messages;
    }
}
