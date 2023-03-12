import Tasks.Base.Task;
import Tasks.AccessMatrix.AccessMatrixTask;
import Tasks.AccessListForObjects.AccessListTask;
import Tasks.CapabilitiesListForDomains.CapabilityListTask;

public final class Main {
    public static void main(String[] args) {

        Task currentTask;

        try {
            // Tasks
            Task accessMatrixTask = new AccessMatrixTask();
            Task accessListTask = new AccessListTask();
            Task capabilityListTask = new CapabilityListTask();

            // Get corresponding task from arguments
            var fullArgs = String.format("%s %s", args[0], args[1]);
            switch (fullArgs) {
                case "-S 1" -> currentTask = accessMatrixTask;
                case "-S 2" -> currentTask = accessListTask;
                case "-S 3" -> currentTask = capabilityListTask;
                default -> throw new IllegalStateException("Unexpected value: " + fullArgs);
            }

        } catch (Exception e) {

            // Incorrect command line arguments
            System.out.println("Invalid command line arguments! Unable to run task.");
            System.out.println("AccessMatrixTask:           -S 1");
            System.out.println("AccessListTask:             -S 2");
            System.out.println("CapabilityListTask:         -S 3");
            return;

        }

        // Run task
        currentTask.Run();

    }
}