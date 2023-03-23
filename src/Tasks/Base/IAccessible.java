package Tasks.Base;

import Tasks.Shared.Utility.AccessObject;

public interface IAccessible {

    AccessObject GetEntry(int domainIndex, int objectIndex);
    AccessObject GetRandomEntry(int domainIndex);
    int GetDomainCount();
    int GetObjectCount();
    void Randomize();
    void PrintData();
}
