package Tasks.AccessMatrix;

import Tasks.Base.IAccessible;
import Tasks.Base.IProtectionTask;
import Tasks.Base.Task;
import Tasks.Shared.Helpers.TaskHelpers;
import Tasks.Shared.Utility.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public final class AccessMatrixTask extends Task implements IProtectionTask {

    // Task related fields
    private IAccessible accessMatrix;
    private Semaphore[][] semaphores;
    private User[] users;

    public AccessMatrixTask() {
        super("Access Matrix (-S 1)");
    }

    @Override
    protected void ConfigureTask() {

        // Generate random values for domains and objects in the range [3,7]
        var rand = new Random();
        var domains = rand.nextInt(3,8);
        var objects = rand.nextInt(3,8);

        // Create and randomize access matrix
        accessMatrix = new AccessMatrix(domains, objects);
        accessMatrix.Randomize();

        // Create threads to operate on access matrix
        var numberOfRequests = rand.nextInt(5, 9);
        users = new User[domains];
        for (var i = 0; i < users.length; i++) {
            users[i] = new User(i, numberOfRequests, this);
        }

        // Create semaphores for access matrix objects
        semaphores = new Semaphore[domains][domains + objects];
        for (var i = 0; i < semaphores.length; i++) {
            for (int j = 0; j < semaphores[0].length; j++) {
                semaphores[i][j] = new Semaphore(1);
            }
        }

        // Display values
        System.out.printf("Domains: %d%n", accessMatrix.GetDomainCount());
        System.out.printf("Objects: %d%n", accessMatrix.GetObjectCount());
        System.out.printf("Number of Requests per Thread: %d%n", numberOfRequests);
    }

    @Override
    protected void Simulate() {
        // Task simulation
        accessMatrix.PrintData();

        // Start threads
        Arrays.stream(users).forEach(User::start);
        // Wait for threads to finish for program end
        Arrays.stream(users).forEach(User::join);
    }

    @Override
    public void AcquireSemaphore(final AccessObject accessObject) {
        semaphores[accessObject.DomainId][accessObject.Index].acquireUninterruptibly();
    }

    @Override
    public void ReleaseSemaphore(final AccessObject accessObject) {
        semaphores[accessObject.DomainId][accessObject.Index].release();
    }

    @Override
    public AccessObject GetRandomObject(int domainId) {
        return accessMatrix.GetRandomObject(domainId);
    }

    @Override
    public OperationResult ProcessOperationRequest(Operation operation, final AccessObject accessObject) {

        var isSuccess = true;
        var newDomainId = accessObject.DomainId;

        if (accessObject.AccessRight == AccessRight.ReadOnly && operation == Operation.Read) {
            // Read message
        }
        else if (accessObject.AccessRight == AccessRight.WriteOnly && operation == Operation.Write) {
            accessObject.Message = TaskHelpers.MESSAGES[accessObject.DomainId];
        }
        else if (accessObject.AccessRight == AccessRight.ReadWrite && operation == Operation.Write) {
            accessObject.Message = TaskHelpers.MESSAGES[accessObject.DomainId];
        }
        else if (accessObject.AccessRight == AccessRight.ReadWrite && operation == Operation.Read) {
            // Read message
        }
        else if (accessObject.AccessRight == AccessRight.AllowSwitch && operation == Operation.DomainSwitch) {
            newDomainId = accessObject.ObjectId;
        }
        else {
            isSuccess = false;
        }

        return new OperationResult(newDomainId, isSuccess);
    }
}
