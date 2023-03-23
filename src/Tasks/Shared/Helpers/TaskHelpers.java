package Tasks.Shared.Helpers;

import Tasks.Shared.Utility.AccessObject;
import Tasks.Shared.Utility.EntryType;
import Tasks.Shared.Utility.Operation;

import java.util.Random;

public final class TaskHelpers {

    public static final String[] MESSAGES = {
            "English: This is a phrase",
            "Spanish: Esta es una frase",
            "French: C'est une locution",
            "Italian: Questa è una frase",
            "Danish: Dette er en sætning",
            "Portuguese: Esta é uma frase",
            "Polish: To jest fraza",
            "Russian: это фраза",
            "Japanese: これはフレーズです",
            "Korean: 이것은 문구입니다",
            "Chinese: 这是一个短语",
    };

    public static final String THREAD_READ_MESSAGE = "[Thread %d (D%d)] Reading message '%s' from 'F%d'.";
    public static final String THREAD_WRITE_MESSAGE = "[Thread %d (D%d)] Writing message '%s' to 'F%d'.";
    public static final String THREAD_SWITCH_DOMAIN = "[Thread %d (D%d)] Switching to domain 'D%d'.";
    public static final String THREAD_ATTEMPT_READ_MESSAGE = "[Thread %d (D%d)] Attempting to read message from 'F%d'.";
    public static final String THREAD_ATTEMPT_WRITE_MESSAGE = "[Thread %d (D%d)] Attempting to write message to 'F%d'.";
    public static final String THREAD_ATTEMPT_SWITCH_DOMAIN = "[Thread %d (D%d)] Attempting to switch to domain 'D%d'.";
    public static final String THREAD_OPERATION_COMPLETE = "[Thread %d (D%d)] Operation complete.";
    public static final String THREAD_OPERATION_FAILED = "[Thread %d (D%d)] Operation failure: Permission Denied.";
    public static final String THREAD_YIELD = "[Thread %d (D%d)] Yielding for %d cycles.";
    public static final String THREAD_REQUESTS_COMPLETE = "[Thread %d] All requests have been tried.";

    public static Operation GetRandomOperation(EntryType type) {
        switch (type) {
            case Object -> {
                var random = new Random();
                return random.nextBoolean() ? Operation.Read : Operation.Write;
            }
            case Domain -> {
                return Operation.DomainSwitch;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static String GetAttemptText(Operation operation, int threadId, int domainId, final AccessObject accessObject) {
        return switch (operation) {
            case Read -> TaskHelpers.THREAD_ATTEMPT_READ_MESSAGE.formatted(threadId, domainId, accessObject.Id);
            case Write -> TaskHelpers.THREAD_ATTEMPT_WRITE_MESSAGE.formatted(threadId, domainId, accessObject.Id);
            case DomainSwitch -> TaskHelpers.THREAD_ATTEMPT_SWITCH_DOMAIN.formatted(threadId, domainId, accessObject.Id);
        };
    }

    public static String GetOperationText(Operation operation, int threadId, int domainId, final AccessObject accessObject) {
        return switch (operation) {
            case Read -> TaskHelpers.THREAD_READ_MESSAGE.formatted(threadId, domainId, accessObject.Message, accessObject.Id);
            case Write -> TaskHelpers.THREAD_WRITE_MESSAGE.formatted(threadId, domainId, TaskHelpers.MESSAGES[domainId], accessObject.Id);
            case DomainSwitch -> TaskHelpers.THREAD_SWITCH_DOMAIN.formatted(threadId, domainId, accessObject.Id);
        };
    }
}
