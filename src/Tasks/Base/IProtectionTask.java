package Tasks.Base;

import Tasks.Shared.Utility.AccessObject;
import Tasks.Shared.Utility.Operation;
import Tasks.Shared.Utility.OperationResult;

public interface IProtectionTask {

    void AcquireSemaphore(int domainId, int objectId);
    void ReleaseSemaphore(int domainId, int objectId);
    AccessObject GetRandomEntry(int domainId);
    OperationResult TryOperateOnEntry(Operation operation, int domainId, final AccessObject accessObject);
}
