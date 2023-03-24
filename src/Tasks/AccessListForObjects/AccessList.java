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
    public AccessObject GetObject(int domainId, int objectIndex) {

        final var accessPair = _data.get(objectIndex);
        final var object = accessPair.AccessObject;
        final var node = accessPair.Node;

        assert node != null;

        // Search for access object within domain
        var iter = node;
        while (iter != null) {
            // Found access object
            final var currentObject = iter.AccessObject;
            if (currentObject.DomainId == domainId) {
                return new AccessObject(domainId, object.ObjectId, object.Index, currentObject.AccessRight, object.Type);
            }

            iter = iter.Next;
        }

        // Create invalid access object
        final var accessRight = object.Type == EntryType.File ?
                AccessRight.NoObjectAccess : AccessRight.NoDomainAccess;

        return new AccessObject(domainId, object.ObjectId, object.Index, accessRight, object.Type);
    }

    @Override
    public AccessObject GetRandomObject(int domainId) {
        final var length = _domains + _objects;
        var randomIndex = ThreadLocalRandom.current().nextInt(length);
        // Ensure not accessing same domain (if applicable)
        var object = _data.get(randomIndex);
        while (object.AccessObject.Type == EntryType.Domain && object.AccessObject.ObjectId == domainId)
        {
            randomIndex = ThreadLocalRandom.current().nextInt(length);
            object = _data.get(randomIndex);
        }

        return GetObject(domainId, randomIndex);
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
        for (var index = 0; index < length; index++) {
            // Create objects
            if (index < _objects) {

                final var object = new AccessObject(-1, index, index, AccessRight.NoObjectAccess, EntryType.File);
                final var accessPair = new AccessPair(object);
                _data.add(accessPair);

                for (var domainId = 0; domainId < _domains; domainId++) {

                    // Get random object right and check if valid
                    final var objectRight = rights[random.nextInt(0, 4)];
                    if (objectRight == AccessRight.NoObjectAccess)
                        continue;

                    AddEntry(accessPair, new AccessObject(domainId, domainId, index, objectRight, EntryType.Domain));
                }
            }
            else {

                final var headObjectId = index - _objects;
                final var object = new AccessObject(-1, headObjectId, index, AccessRight.NoDomainAccess, EntryType.Domain);
                final var accessPair = new AccessPair(object);
                _data.add(accessPair);

                for (var domainId = 0; domainId < _domains; domainId++) {
                    // Check for switching to same domain (and continue)
                    if (domainId == headObjectId)
                        continue;

                    // Get random domain right and check if valid
                    final var domainRight = random.nextBoolean() ? AccessRight.AllowSwitch : AccessRight.NoDomainAccess;
                    if (domainRight != AccessRight.AllowSwitch)
                        continue;

                    AddEntry(accessPair, new AccessObject(domainId, domainId, index, domainRight, EntryType.Domain));
                }
            }
        }
    }

    @Override
    public void PrintData() {
        // Show objects
        for (var i = 0; i < _objects; i++) {

            var pair = _data.get(i);
            System.out.printf("\t F%d -->", pair.AccessObject.ObjectId);
            final var node = pair.Node;
            PrintLink(node);
        }

        // Show domains
        for (var i = _objects; i < _domains + _objects; i++) {

            var pair = _data.get(i);
            System.out.printf("\t D%d -->", pair.AccessObject.ObjectId);
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
                System.out.printf("\tD%d[%s]\t-->", accessNode.AccessObject.ObjectId, accessNode.AccessObject.AccessRight);
            else
                System.out.printf("\tD%d[%s]", accessNode.AccessObject.ObjectId, accessNode.AccessObject.AccessRight);

            accessNode = accessNode.Next;
        }
        System.out.println();
    }
}
