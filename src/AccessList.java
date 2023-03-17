public class AccessList {
    private int size;//size of the list
    private Node[] table;//access list of nodes


    private static class Node {
        String domain;//domains
        boolean canSwitch;//whether domains can be switched
        boolean canRead;//read permission
        boolean canWrite;//write permission
        boolean canReadWrite;//read & write permission
        Node next;//next node in the list


        Node(String domain, boolean canSwitch, boolean canRead, boolean canWrite, boolean canReadWrite) {
            this.domain = domain;
            this.canSwitch = canSwitch;
            this.canRead = canRead;
            this.canWrite = canWrite;
            this.canReadWrite = canReadWrite;
        }
    }

    //The access list definition
    public AccessList(int M, int N) {
        size = M + N;
        table = new Node[size];
    }

    //add entries to the access list
    public void addEntry(String domain, boolean canSwitch, boolean canRead, boolean canWrite, boolean canReadWrite) {
        Node node = new Node(domain, canSwitch, canRead, canWrite, canReadWrite);
        int index = getIndex(domain);
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node curr = table[index];
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = node;
        }
    }

    //get the index of the domain
    private int getIndex(String domain) {
        return Math.abs(domain.hashCode()) % size;
    }

    //whether domains can be switched based on domain
    public boolean canSwitch(String domain) {
        Node curr = table[getIndex(domain)];
        while (curr != null) {
            if (curr.domain.equals(domain)) {
                return curr.canSwitch;
            }
            curr = curr.next;
        }
        return false;
    }

    //read permission based on domain
    public boolean canRead(String domain) {
        Node curr = table[getIndex(domain)];
        while (curr != null) {
            if (curr.domain.equals(domain)) {
                return curr.canRead;
            }
            curr = curr.next;
        }
        return false;
    }

    //write permission based on domain
    public boolean canWrite(String domain) {
        Node curr = table[getIndex(domain)];
        while (curr != null) {
            if (curr.domain.equals(domain)) {
                return curr.canWrite;
            }
            curr = curr.next;
        }
        return false;
    }

    //read & write permission based on domain
    public boolean canReadWrite(String domain) {
        Node curr = table[getIndex(domain)];
        while (curr != null) {
            if (curr.domain.equals(domain)) {
                return curr.canReadWrite;
            }
            curr = curr.next;
        }
        return false;
    }
}
