package Tasks.Shared.Utility;

import Tasks.Base.IProtectionTask;
import Tasks.Shared.Helpers.TaskHelpers;

import java.util.concurrent.ThreadLocalRandom;

public final class AccessOperator implements Runnable {

    private final IProtectionTask task;
    private final Thread thread;
    private final int id;
    private int domainId;
    private final int maxNumberOfRequests;

    public AccessOperator(int id, int numberOfRequests, IProtectionTask task) {
        this.id = id;
        maxNumberOfRequests = numberOfRequests;
        this.task = task;
        domainId = id;
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

        final var requestsCompleteText = TaskHelpers.THREAD_REQUESTS_COMPLETE.formatted(id);
        System.out.println(requestsCompleteText);
    }

    private void Request() {
        // Get random object within current domain
        final AccessObject accessObject = task.GetRandomEntry(domainId);
        final Operation operation = TaskHelpers.GetRandomOperation(accessObject.Type);

        // Get text for operation
        final String attemptText = TaskHelpers.GetAttemptText(operation, id, domainId, accessObject);
        final String operationText = TaskHelpers.GetOperationText(operation, id, domainId, accessObject);

        // Attempt request
        System.out.println(attemptText);

        // Acquire semaphore of entry
        task.AcquireSemaphore(domainId, accessObject.Index);

        // Try operation on entry
        var operationResult = task.TryOperateOnEntry(operation, domainId, accessObject);
        if (operationResult.IsSuccess()) {
            System.out.println(operationText);
        }

        // Yield for some cycles
        Yield();

        // Release semaphore of entry
        task.ReleaseSemaphore(domainId, accessObject.Index);

        // Reassign domain (in case of domain switch operation)
        domainId = operationResult.DomainId();

        final String operationCompleteText = TaskHelpers.THREAD_OPERATION_COMPLETE.formatted(id, domainId);
        final String operationFailedText = TaskHelpers.THREAD_OPERATION_FAILED.formatted(id, domainId);

        // Display operation success/failure
        if (operationResult.IsSuccess()) {
            System.out.println(operationCompleteText);
        } else {
            System.out.println(operationFailedText);
        }

        // Yield for some cycles
        Yield();
    }

    private void Yield() {
        var random = ThreadLocalRandom.current().nextInt(3, 7);
        final String yieldText = TaskHelpers.THREAD_YIELD.formatted(id, domainId, random);
        System.out.println(yieldText);
        for (var i = 0; i < random; i++) {
            Thread.yield();
        }
    }
}
