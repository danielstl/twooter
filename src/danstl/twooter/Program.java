package danstl.twooter;

import danstl.twooter.gui.HomePage;
import twooter.TwooterClient;

import java.io.IOException;

public class Program {

    public static void main(String[] args) throws IOException {
        TwooterClient c = new TwooterClient();

        new HomePage(c);
    }
}
