package Tasks.CapabilitiesListForDomains;

import Tasks.Base.IAccessible;
import Tasks.Shared.Utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class CapabilityList implements IAccessible {
    private final int _domains;
    private final int _objects;
    private final List<AccessPair> _data; // Contains a node chain

    public CapabilityList(int domains, int objects) {
        _domains = domains;
        _objects = objects;
        _data = new ArrayList<>(_domains);
    }

    @Override
    public AccessObject GetObject(int domainId, int objectIndex) {

        final var accessPair = _data.get(domainId);
        final var object = accessPair.AccessObject;
        final var node = accessPair.Node;

        assert node != null;

        // Search for access object within domain
        var iter = node;
        while (iter != null) {
            // Found access object
            final var currentObject = iter.AccessObject;
            if (currentObject.Index == objectIndex) {
                return currentObject;
            }

            iter = iter.Next;
        }

        // Create invalid access object
        final var accessRight = object.Type == EntryType.File ?
                AccessRight.NoObjectAccess : AccessRight.NoDomainAccess;

        return new AccessObject(domainId, object.ObjectId, objectIndex, accessRight, object.Type);
    }

    @Override
    public AccessObject GetRandomObject(int domainId) {
        final var length = _domains + _objects;
        var randomIndex = ThreadLocalRandom.current().nextInt(length);
        var object = GetObject(domainId, randomIndex);

        // Ensure not accessing same domain (if applicable)
        while (object.ObjectId == domainId)
        {
            randomIndex = ThreadLocalRandom.current().nextInt(length);
            object = GetObject(domainId, randomIndex);
        }

        return object;
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
        final var length = _domains + _objects;
        for (var domainId = 0; domainId < _domains; domainId++) {

            final var domain = new AccessObject(domainId, domainId, domainId, AccessRight.NoDomainAccess, EntryType.Domain);
            final var accessPair = new AccessPair(domain);
            _data.add(accessPair);

            // Create objects
            for (var index = 0; index < length; index++) {

                if (index < _objects) {
                    // Get random object right and check if valid
                    final var objectRight = rights[random.nextInt(0, 4)];
                    if (objectRight == AccessRight.NoObjectAccess)
                        continue;

                    final var objectId = index;
                    AddEntry(accessPair, new AccessObject(domainId, objectId, index, objectRight, EntryType.File));
                }
                else {
                    // Check for switching to same domain (and continue)
                    final var objectId = index - _objects;
                    if (objectId == domainId)
                        continue;

                    // Get random domain right and check if valid
                    final var domainRight = random.nextBoolean() ? AccessRight.AllowSwitch : AccessRight.NoDomainAccess;
                    if (domainRight != AccessRight.AllowSwitch)
                        continue;

                    AddEntry(accessPair, new AccessObject(domainId, objectId, index, domainRight, EntryType.Domain));
                }
            }
        }
    }

    @Override
    public void PrintData() {
        // Show nodes
        for (var i = 0; i < _domains; i++) {

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
            final var representation = accessNode.AccessObject.Type == EntryType.File ? 'F' : 'D';
            if (accessNode.Next != null)
                System.out.printf("\t%c%d[%s]\t-->", representation, accessNode.AccessObject.ObjectId, accessNode.AccessObject.AccessRight);
            else
                System.out.printf("\t%c%d[%s]", representation, accessNode.AccessObject.ObjectId, accessNode.AccessObject.AccessRight);

            accessNode = accessNode.Next;
        }
        System.out.println();
    }
}
