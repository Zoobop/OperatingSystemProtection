package Tasks.AccessMatrix;

import Tasks.Base.IAccessible;
import Tasks.Shared.Utility.AccessRight;
import Tasks.Shared.Utility.AccessObject;
import Tasks.Shared.Utility.EntryType;
import java.util.concurrent.ThreadLocalRandom;

public final class AccessMatrix implements IAccessible {

    private final int _domains;
    private final int _objects;
    private final AccessObject[][] _data;

    public AccessMatrix(int domains, int objects) {
        _domains = domains;
        _objects = objects;
        _data = new AccessObject[_domains][_domains + _objects];
    }

    @Override
    public AccessObject GetEntry(int domainIndex, int objectIndex) {
        return _data[domainIndex][objectIndex];
    }

    @Override
    public AccessObject GetRandomEntry(int domainIndex) {
        final int randomIndex = ThreadLocalRandom.current().nextInt(_data.length);
        return _data[domainIndex][randomIndex];
    }

    @Override
    public int GetDomainCount() { return _domains; }

    @Override
    public int GetObjectCount() { return _objects; }

    @Override
    public void Randomize() {
        final AccessRight[] rights = AccessRight.values();

        var random = ThreadLocalRandom.current();
        for (int i = 0; i < _domains; i++) {
            // Create objects
            for (int j = 0; j < _objects; j++) {
                var right = rights[random.nextInt(4)];
                _data[i][j] = new AccessObject(j, j, right, EntryType.Object); //rights index
            }

            // Create domains
            for (var j = _objects; j < _domains + _objects; j++) {
                // Check for switching to same domain
                if (i + _objects == j) {
                    _data[i][j] = new AccessObject(j - _objects, j, AccessRight.SameDomain, EntryType.Domain);
                    continue;
                }

                var right = rights[random.nextInt(4, rights.length - 1)];
                _data[i][j] = new AccessObject(j - _objects, j, right, EntryType.Domain); //switch index
            }
        }
    }

    @Override
    public void PrintData()
    {
        final int rows = _domains;
        final int columns = _domains + _objects;

        System.out.print("\t");
        for (var i = 0; i < _objects; i++) {
            System.out.printf("\tF%d\t", i);
        }
        for (var i = 0; i < _domains; i++) {
            System.out.printf("\t D%d\t\t", i);
        }
        System.out.println();

        for (var i = 0; i < rows; i++) {
            System.out.printf("D%d\t", i);
            for (var j = 0; j < columns; j++) {
                System.out.printf("|\t%s\t", _data[i][j].AccessRight);
            }
            System.out.println();
        }

        System.out.println();
    }
}
