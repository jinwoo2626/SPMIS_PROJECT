package dongyang.spmis.model.User;

import lombok.Data;

@Data
public class ModifyPasswordDTO {
    private String newPass;
    private String newPassCheck;
    private String user_email;
}
