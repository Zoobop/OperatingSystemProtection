import java.util.Random;

public class AccessMatrix {
    public static void main(String[] args) {
        String[] Rights = {"R","W","RW","-"};



        String[] Switch = {"allow","null"};



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
            Random srand = new Random();

            for (int j = 0; j < M + N; j++) {
                // allow switching into other domains (excluding own domain)
                if (j >= M && j < M + N && i != j - M) {
                    accessMatrix[i][j] = Switch[srand.nextInt(Switch.length)]; //switch index

                }
                // set access rights for objects
                else if (j < M) {
                    accessMatrix[i][j] = Rights[random.nextInt(Rights.length)]; //rights index
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
