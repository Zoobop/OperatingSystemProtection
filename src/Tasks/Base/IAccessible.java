package Tasks.Base;

import Tasks.Shared.Utility.AccessObject;

public interface IAccessible {

    AccessObject GetObject(int domainIndex, int objectIndex);
    AccessObject GetRandomObject(int domainIndex);
    int GetDomainCount();
    int GetObjectCount();
    void Randomize();
    void PrintData();
}
