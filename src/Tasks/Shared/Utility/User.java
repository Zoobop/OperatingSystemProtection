package Tasks.Shared.Utility;

import Tasks.Base.IProtectionTask;
import Tasks.Shared.Helpers.TaskHelpers;

import java.util.concurrent.ThreadLocalRandom;

public final class User implements Runnable {

    private final IProtectionTask task;
    private final Thread thread;
    private final int threadId;
    private int domainId;
    private final int maxNumberOfRequests;

    public User(int threadId, int numberOfRequests, IProtectionTask task) {
        this.threadId = threadId;
        maxNumberOfRequests = numberOfRequests;
        this.task = task;
        domainId = threadId;
        thread = new Thread(this);
    }

    public void start() { thread.start(); }

    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        var numberOfRequests = 0;
        while (numberOfRequests++ < maxNumberOfRequests) {
            // Request access
            Request();
        }

        final var requestsCompleteText = TaskHelpers.THREAD_REQUESTS_COMPLETE.formatted(threadId);
        System.out.println(requestsCompleteText);
    }

    private void Request() {
        // Get random object within current domain
        final AccessObject accessObject = task.GetRandomObject(domainId);
        final Operation operation = TaskHelpers.GetRandomOperation(accessObject.Type);

        // Get text for operation
        final String attemptText = TaskHelpers.GetAttemptText(threadId, operation, accessObject);
        final String operationText = TaskHelpers.GetOperationText(threadId, operation, accessObject);

        // Attempt request
        System.out.println(attemptText);

        // Acquire semaphore of object
        task.AcquireSemaphore(accessObject);

        // Try operation on object based on permissions (arbitrator function)
        final var operationResult = task.ProcessOperationRequest(operation, accessObject);

        // Reassign domain (in case of domain switch operation)
        domainId = operationResult.DomainId();

        final String operationCompleteText = TaskHelpers.THREAD_OPERATION_COMPLETE.formatted(threadId, domainId);
        final String operationFailedText = TaskHelpers.THREAD_OPERATION_FAILED.formatted(threadId, domainId);

        // Display operation success/failure
        if (operationResult.IsSuccess()) {
            System.out.println(operationText);
            System.out.println(operationCompleteText);
        } else {
            System.out.println(operationFailedText);
        }

        // Yield for some cycles
        Yield();

        // Release semaphore of object
        task.ReleaseSemaphore(accessObject);
    }

    private void Yield() {
        var random = ThreadLocalRandom.current().nextInt(3, 7);
        final String yieldText = TaskHelpers.THREAD_YIELD.formatted(threadId, domainId, random);
        System.out.println(yieldText);
        for (var i = 0; i < random; i++) {
            Thread.yield();
        }
    }
}
