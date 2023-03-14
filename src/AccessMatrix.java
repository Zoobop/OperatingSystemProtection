import java.util.Random;

public class AccessMatrix {
    public static void main(String[] args) {
        String[] Rights = {"R","W","RW"};



        String[] Switch = {"allow","do not allow"};



        // generate random values for N and M in the range [3,7]
        Random rand = new Random();
        int N = rand.nextInt(3,7);
        System.out.println(N);
        int M = rand.nextInt(3,7);
        System.out.println(M);

        // create access matrix of size N x (M + N)
        String[][] accessMatrix = new String[N][M + N];

        // populate access matrix randomly
        for (int i = 0; i < N; i++) {
            Random random = new Random();
            int Rindex = random.nextInt(Rights.length); //rindex is rights index
            
            Random srand = new Random();
            int Sindex = srand.nextInt(Switch.length); //sindex is switch index

            for (int j = 0; j < M + N; j++) {
                // allow switching into other domains (excluding own domain)
                if (j >= M && j < M + N && i != j - M) {
                    accessMatrix[i][j] = Switch[srand.nextInt(Switch.length)];

                }
                // set access rights for objects
                else if (j < M) {
                    accessMatrix[i][j] = Rights[random.nextInt(Rights.length)];
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
