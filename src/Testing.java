import java.util.Random;

public class Testing {
    private static final String[] DOMAINS = {"domain1", "domain2", "domain3", "domain4", "domain5"};
    private static final int NUM_OBJECTS = 250;
    private static final int NUM_DOMAINS = 26;//must be less than objects
    private static final Random RAND = new Random();
    public static void main(String[] args) {
        CapabilityList capabilityList = new CapabilityList(NUM_DOMAINS);

        // Generate random objects
        String[] objects = new String[NUM_OBJECTS];
        for (int i = 0; i < NUM_OBJECTS; i++) {
            objects[i] = "object" + i;
        }

        // Add random capabilities for objects
        for (String object : objects) {
            boolean hasAccess = RAND.nextBoolean();
            capabilityList.addCapability(object, hasAccess);
        }

        // Test random domain access to objects
        for (int i = 0; i < NUM_DOMAINS; i++) {
            String object = objects[RAND.nextInt(NUM_OBJECTS)];
            boolean hasAccess = capabilityList.hasAccess(object, i);
            System.out.println("Domain " + i + " has access to " + object + ": " + hasAccess);
        }
    }
}
