package Tasks.AccessListForObjects;

import Tasks.Base.IAccessible;
import Tasks.Shared.Utility.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class AccessList implements IAccessible {
    private final int _domains;
    private final int _objects;
    private final List<AccessPair> _data;  // Contains a node chain

    public AccessList(int domains, int objects) {
        _domains = domains;
        _objects = objects;
        _data = new ArrayList<>(_domains + _objects);
    }

    @Override
    public AccessObject GetEntry(int domainId, int objectId) {

        final var accessPair = _data.get(objectId);
        final var object = accessPair.AccessObject;
        final var node = accessPair.Node;

        assert node != null;

        // Search for access object within domain
        final var id = object.Type == EntryType.Object ? objectId : objectId - _objects;
        var iter = node;
        while (iter != null) {
            // Found access object
            if (iter.AccessObject.Id == domainId) {
                if (id == domainId)
                    break;
                return new AccessObject(id, domainId, iter.AccessObject.AccessRight, object.Type);
            }

            iter = iter.Next;
        }

        // Create invalid access object
        final var accessRight = object.Type == EntryType.Object ?
                AccessRight.NoObjectAccess : AccessRight.NoDomainAccess;

        return new AccessObject(id, domainId, accessRight, object.Type);
    }

    @Override
    public AccessObject GetRandomEntry(int domainId) {
        final var objectId = ThreadLocalRandom.current().nextInt(_domains + _objects);
        return GetEntry(domainId, objectId);
    }

    @Override
    public int GetDomainCount() {
        return _domains;
    }

    @Override
    public int GetObjectCount() {
        return _objects;
    }

    @Override
    public void Randomize() {
        final var rights = AccessRight.values();

        var random = ThreadLocalRandom.current();
        final int length = _domains + _objects;
        for (var i = 0; i < length; i++) {
            // Create objects
            if (i < _objects) {

                final var object = new AccessObject(i, i, AccessRight.NoObjectAccess, EntryType.Object);
                final var accessPair = new AccessPair(object, null);
                _data.add(accessPair);

                for (int j = 0, n = 0; j < _domains; j++) {

                    // Get random object right and check if valid
                    final var objectRight = rights[random.nextInt(0, 4)];
                    if (objectRight == AccessRight.NoObjectAccess)
                        continue;

                    AddEntry(accessPair, new AccessObject(n++, j, objectRight, EntryType.Domain));
                }
            }
            else {

                final var object = new AccessObject(i - _objects, i, AccessRight.NoDomainAccess, EntryType.Domain);
                final var accessPair = new AccessPair(object, null);
                _data.add(accessPair);

                for (int j = 0, n = 0; j < _domains; j++) {
                    // Check for switching to same domain (and continue)
                    if (j == i - _objects)
                        continue;

                    // Get random domain right and check if valid
                    final var domainRight = random.nextBoolean() ? AccessRight.AllowSwitch : AccessRight.NoDomainAccess;
                    if (domainRight != AccessRight.AllowSwitch)
                        continue;

                    AddEntry(accessPair, new AccessObject(n++, j, domainRight, EntryType.Domain));
                }
            }
        }
    }

    @Override
    public void PrintData() {
        // Show objects
        for (var i = 0; i < _objects; i++) {

            var pair = _data.get(i);
            System.out.printf("\t F%d -->", pair.AccessObject.Id);
            final var node = pair.Node;
            PrintLink(node);
        }

        // Show domains
        for (var i = _objects; i < _domains + _objects; i++) {

            var pair = _data.get(i);
            System.out.printf("\t D%d -->", pair.AccessObject.Id);
            final var node = pair.Node;
            PrintLink(node);
        }

        System.out.println();
    }

    /*
        Helper Functions
     */

    // Adds a node to the end of the link
    private void AddEntry(AccessPair pair, AccessObject accessObject) {

        // Create node
        final var newNode = new AccessNode(accessObject);

        // First insert (head node)
        if (pair.Node == null) {
            pair.Node = newNode;
        }
        // Preceding inserts
        else {
            var iter = pair.Node;
            while (iter.Next != null) {
                iter = iter.Next;
            }
            iter.Next = newNode;
        }
    }

    private void PrintLink(AccessNode accessNode) {

        while (accessNode != null) {
            if (accessNode.Next != null)
                System.out.printf("\tD%d[%s]\t-->", accessNode.AccessObject.Index, accessNode.AccessObject.AccessRight);
            else
                System.out.printf("\tD%d[%s]", accessNode.AccessObject.Index, accessNode.AccessObject.AccessRight);

            accessNode = accessNode.Next;
        }
        System.out.println();
    }
}
