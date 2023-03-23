package Tasks.Shared.Utility;

public final class AccessObject {

    public final AccessRight AccessRight;
    public final EntryType Type;
    public final int Id;
    public final int Index;
    public String Message;

    public AccessObject(int id, int index, AccessRight accessRight, EntryType type) {
        this(id, index, accessRight, type, "<EMPTY>");
    }

    public AccessObject(int id, int index, AccessRight accessRight, EntryType type, String message) {
        Id = id;
        Index = index;
        AccessRight = accessRight;
        Type = type;
        Message = message;
    }

    @Override
    public String toString() {
        return "%s [%d:%d]".formatted(Type, Id, Index);
    }
}
