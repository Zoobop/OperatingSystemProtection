package Tasks.Shared.Utility;

public enum AccessRight {

    NoObjectAccess(" "),
    ReadOnly("R"),
    WriteOnly("W"),
    ReadWrite("RW"),
    NoDomainAccess("    "),
    AllowSwitch("allow"),
    SameDomain(" -- ");

    public final String Label;

    AccessRight(String label) { Label = label; }

    @Override
    public String toString() {
        return Label;
    }
}
