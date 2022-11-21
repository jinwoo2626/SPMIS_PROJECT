package dongyang.spmis.model.MessageModel;

import lombok.Data;

@Data
public class MessageDTO {
    private int message_id;
    private String send_user_email;
    private String recv_user_email;
    private String message;
    private boolean confirm;

}
