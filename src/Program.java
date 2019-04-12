import gui.HomePage;
import twooter.TwooterClient;

import java.io.IOException;

public class Program {

    public static void main(String[] args) throws IOException {
        TwooterClient c = new TwooterClient();

        c.registerName("test");

        new HomePage(c);
    }
}
