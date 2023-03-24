package Tasks.Shared.Utility;

public final class AccessPair {

    public AccessObject AccessObject;
    public AccessNode Node;

    public AccessPair(AccessObject accessObject) {
        AccessObject = accessObject;
        Node = null;
    }
}
