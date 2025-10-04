package az.qala.permissionbased.controller.chat;

import az.qala.permissionbased.model.request.chat.ChatMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/chat")
public class ChatController {
    // emitters are essentially active listeners (subscribers)
    private final List<SseEmitter> emitters = new ArrayList<>();

    // client subscribes and starts stream text/
    @GetMapping("/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(0L); // never timeout
        emitters.add(emitter);

        // removing emitters when  they close tab or connection somewhow timesout
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // no need to set text/event-stream manually since spring boot automatically sets it
        // when SseEmitter returned
        return emitter;
    }

    // client will send messages here
    @PostMapping("/send")
    public void send(@RequestBody ChatMessage message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        // sending message to all active subscribers
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("Chat") // event name
                        .data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);  // removing dead connections from alll subscribers list
    }
}
