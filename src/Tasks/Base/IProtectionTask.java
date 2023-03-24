package Tasks.Base;

import Tasks.Shared.Utility.AccessObject;
import Tasks.Shared.Utility.Operation;
import Tasks.Shared.Utility.OperationResult;

public interface IProtectionTask {

    void AcquireSemaphore(final AccessObject accessObject);
    void ReleaseSemaphore(final AccessObject accessObject);
    AccessObject GetRandomObject(int domainId);
    OperationResult ProcessOperationRequest(Operation operation, final AccessObject accessObject);
}
