package az.qala.permissionbased.model.request.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMessage implements Serializable {
    private String sender;
    private String content;
}
