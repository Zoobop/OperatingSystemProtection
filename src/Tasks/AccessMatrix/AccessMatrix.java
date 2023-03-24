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
    public AccessObject GetObject(int domainId, int index) {
        return _data[domainId][index];
    }

    @Override
    public AccessObject GetRandomObject(int domainId) {
        var randomIndex = ThreadLocalRandom.current().nextInt(_data.length);
        // Ensure not accessing same domain (if applicable)
        var object = _data[domainId][randomIndex];
        while (object.Type == EntryType.Domain && object.ObjectId == domainId)
        {
            randomIndex = ThreadLocalRandom.current().nextInt(_data.length);
            object = _data[domainId][randomIndex];
        }
        return object;
    }

    @Override
    public int GetDomainCount() { return _domains; }

    @Override
    public int GetObjectCount() { return _objects; }

    @Override
    public void Randomize() {
        final AccessRight[] rights = AccessRight.values();

        var random = ThreadLocalRandom.current();
        for (var domaindId = 0; domaindId < _domains; domaindId++) {
            // Create objects
            for (var index = 0; index < _objects; index++) {
                final var objectId = index;
                final var objectRight = rights[random.nextInt(4)];
                _data[domaindId][index] = new AccessObject(domaindId, objectId, index, objectRight, EntryType.File); //rights index
            }

            // Create domains
            for (var index = _objects; index < _domains + _objects; index++) {
                final var objectId = index - _objects;

                // Check for switching to same domain
                if (domaindId == objectId) {
                    _data[domaindId][index] = new AccessObject(domaindId, objectId, index, AccessRight.SameDomain, EntryType.Domain);
                    continue;
                }

                final var domainRight = rights[random.nextInt(4, rights.length - 1)];
                _data[domaindId][index] = new AccessObject(domaindId, objectId, index, domainRight, EntryType.Domain); //switch index
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
