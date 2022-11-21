package dongyang.spmis.service;

import dongyang.spmis.model.MessageModel.MessageDTO;

import java.util.ArrayList;

public interface MessageService {

    public boolean sendMessage(MessageDTO message);

    public ArrayList<MessageDTO> getMessage(String user_email);

}
