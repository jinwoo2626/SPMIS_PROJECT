package dongyang.spmis.service;

import dongyang.spmis.mapper.MessageMapper;
import dongyang.spmis.model.MessageModel.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MessageServiceImpl implements  MessageService{

    @Autowired
    MessageMapper messageMapper;

    @Override
    public boolean sendMessage(MessageDTO message) {
        return messageMapper.saveMessage(message);
    }

    @Override
    public ArrayList<MessageDTO> getMessage(String user_email) {
        return messageMapper.findMessageByUserEmail(user_email);
    }
}
