public class CapabilityList {
    private static class Node {
        String object;//objects in the list
        boolean hasAccess;//whether access is granted
        Node next;//next node in the list

        //nodes in the capability list
        Node(String object, boolean hasAccess) {
            this.object = object;
            this.hasAccess = hasAccess;
        }
    }

    private Node head;
    private int size;//size of capability list


    public CapabilityList(int size) {
        this.size = size;
    }

    //capabilities by object and whether it has access
    public void addCapability(String object, boolean hasAccess) {
        if (head == null) {
            head = new Node(object, hasAccess);
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node(object, hasAccess);
        }
    }

    //if object has access by domain
    public boolean hasAccess(String object, int domainIndex) {
        if (domainIndex < 0 || domainIndex >= size) {
            throw new IndexOutOfBoundsException("Domain index out of bounds");
        }
        Node current = head;
        for (int i = 0; i < domainIndex; i++) {
            if (current == null) {
                throw new IllegalStateException("Capability list is not complete");
            }
            current = current.next;
        }
        if (current == null) {
            throw new IllegalStateException("Capability list is not complete");
        }
        return current.hasAccess;
    }
}
