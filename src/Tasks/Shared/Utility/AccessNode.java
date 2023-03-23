package Tasks.Shared.Utility;

public final class AccessNode {

    public final Tasks.Shared.Utility.AccessObject AccessObject;
    public AccessNode Next;

    public AccessNode(AccessObject accessObject) {
        AccessObject = accessObject;
        Next = null;
    }
}
