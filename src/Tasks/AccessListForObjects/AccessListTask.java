package Tasks.AccessListForObjects;

import Tasks.Base.IAccessible;
import Tasks.Base.IProtectionTask;
import Tasks.Base.Task;
import Tasks.Shared.Helpers.TaskHelpers;
import Tasks.Shared.Utility.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public final class AccessListTask extends Task implements IProtectionTask {

    // Task related fields
    private IAccessible accessList;
    private Semaphore[][] semaphores;
    private AccessOperator[] accessOperators;

    public AccessListTask() {
        super("Access List For Objects (-S 2)");
    }

    @Override
    protected void ConfigureTask() {

        // Generate random values for domains and objects in the range [3,7]
        var rand = new Random();
        var domains = rand.nextInt(3, 8);
        var objects = rand.nextInt(3, 8);

        // Create and randomize access matrix
        accessList = new AccessList(domains, objects);
        accessList.Randomize();

        // Create threads to operate on access matrix
        var numberOfRequests = rand.nextInt(5, 11);
        accessOperators = new AccessOperator[domains];
        for (var i = 0; i < accessOperators.length; i++) {
            accessOperators[i] = new AccessOperator(i, numberOfRequests, this);
        }

        // Create semaphores for access list entries
        semaphores = new Semaphore[domains][domains + objects];
        for (var i = 0; i < semaphores.length; i++) {
            for (int j = 0; j < semaphores[0].length; j++) {
                semaphores[i][j] = new Semaphore(1);
            }
        }

        // Display values
        System.out.printf("Domains: %d%n", accessList.GetDomainCount());
        System.out.printf("Objects: %d%n", accessList.GetObjectCount());
        System.out.printf("Number of Requests per Thread: %d%n", numberOfRequests);
    }

    @Override
    protected void Simulate() {
        // Task simulation
        accessList.PrintData();

        // Start threads
        Arrays.stream(accessOperators).forEach(AccessOperator::start);
        // Wait for threads to finish for program end
        Arrays.stream(accessOperators).forEach(AccessOperator::join);
    }

    @Override
    public void AcquireSemaphore(int domainId, int objectId) {
        semaphores[domainId][objectId].acquireUninterruptibly();
    }

    @Override
    public void ReleaseSemaphore(int domainId, int objectId) {
        semaphores[domainId][objectId].release();
    }

    @Override
    public AccessObject GetRandomEntry(int domainId) {
        return accessList.GetRandomEntry(domainId);
    }

    @Override
    public OperationResult TryOperateOnEntry(Operation operation, int domainId, final AccessObject accessObject) {
        var isSuccess = true;
        var newDomainId = domainId;

        if (accessObject.AccessRight == AccessRight.ReadOnly && operation == Operation.Read) {
            // Read message
        }
        else if (accessObject.AccessRight == AccessRight.WriteOnly && operation == Operation.Write) {
            accessObject.Message = TaskHelpers.MESSAGES[domainId];
        }
        else if (accessObject.AccessRight == AccessRight.ReadWrite && operation == Operation.Write) {
            accessObject.Message = TaskHelpers.MESSAGES[domainId];
        }
        else if (accessObject.AccessRight == AccessRight.ReadWrite && operation == Operation.Read) {
            // Read message
        }
        else if (accessObject.AccessRight == AccessRight.AllowSwitch && operation == Operation.DomainSwitch) {
            newDomainId = accessObject.Index;
        }
        else {
            isSuccess = false;
        }

        return new OperationResult(newDomainId, isSuccess);
    }
}
