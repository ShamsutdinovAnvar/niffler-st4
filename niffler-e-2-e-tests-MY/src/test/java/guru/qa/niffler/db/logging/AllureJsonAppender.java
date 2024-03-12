package guru.qa.niffler.db.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import lombok.SneakyThrows;

import java.util.Objects;

public class AllureJsonAppender extends StdoutLogger {

    private final String templateName = "json.ftl";
    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

    @SneakyThrows
    public void logJson(String name, Object json) {
        if (Objects.nonNull(json)) {
            JsonAttachment jsonAttachment = new JsonAttachment(name, new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(json));
            attachmentProcessor.addAttachment(jsonAttachment, new FreemarkerAttachmentRenderer(templateName));
        }
    }
}