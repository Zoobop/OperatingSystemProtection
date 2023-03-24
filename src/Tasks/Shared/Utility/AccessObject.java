package Tasks.Shared.Utility;

public final class AccessObject {

    public final AccessRight AccessRight;
    public final EntryType Type;
    public final int DomainId;
    public final int ObjectId;
    public final int Index;
    public String Message;

    public AccessObject(int domainId, int objectId, AccessRight accessRight, EntryType type) {
        this(domainId, objectId, -1, accessRight, type, "<EMPTY>");
    }

    public AccessObject(int domainId, int objectId, int index, AccessRight accessRight, EntryType type) {
        this(domainId, objectId, index, accessRight, type, "<EMPTY>");
    }

    public AccessObject(int domainId, int objectId, int index, AccessRight accessRight, EntryType type, String message) {
        DomainId = domainId;
        ObjectId = objectId;
        Index = index;
        AccessRight = accessRight;
        Type = type;
        Message = message;
    }

    @Override
    public String toString() {
        return "%s [%d:%d:%d]".formatted(Type, DomainId, ObjectId, Index);
    }
}
