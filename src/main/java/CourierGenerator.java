import models.Courier;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public static Courier getRandom() {
        final String login = RandomStringUtils.randomAlphabetic(7);
        final String password = RandomStringUtils.randomAlphabetic(7);
        final String firstName = RandomStringUtils.randomAlphabetic(7);
        return new Courier(login, password, firstName);
    }

    public static Courier getRandomWithoutLogin(){
        final String password = RandomStringUtils.randomAlphabetic(7);
        final String firstName = RandomStringUtils.randomAlphabetic(7);
        return new Courier(null, password, firstName);
    }

    public static Courier getRandomWithoutPassword(){
        final String login = RandomStringUtils.randomAlphabetic(7);
        final String firstName = RandomStringUtils.randomAlphabetic(7);
        return new Courier(login, null, firstName);
    }
}