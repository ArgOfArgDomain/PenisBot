package penisbot;

public class Utilities {

    /**
     * Sleep without caring about interruptions.
     * @param millis
     */
    public static void trySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Dun care
        }
    }

}
