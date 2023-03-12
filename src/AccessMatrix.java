import java.util.Random;

public class AccessMatrix {
    public static void main(String[] args) {
        String[] Rights = {"R","W","RW"};



        String[] Switch = {"allow","do not allow"};



        // generate random values for N and M in the range [3,7]
        Random rand = new Random();
        int N = rand.nextInt(5) + 3;
        int M = rand.nextInt(5) + 3;

        // create access matrix of size N x (M + N)
        String[][] accessMatrix = new String[N][M + N];

        // populate access matrix randomly
        for (int i = 0; i < N; i++) {
            Random random = new Random();
            int Rindex = random.nextInt(Rights.length);
            
            Random srand = new Random();
            int Sindex = srand.nextInt(Switch.length);

            for (int j = 0; j < M + N; j++) {
                // allow switching into other domains (excluding own domain)
                if (j >= M && j < M + N && i != j - M) {
                    accessMatrix[i][j] = Switch[Sindex];

                }
                // set access rights for objects
                else if (j < M) {
                    accessMatrix[i][j] = Rights[Rindex];
                }
            }
        }

        // print access matrix
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M + N; j++) {
                System.out.print(accessMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
