package danstl.twooter;

import com.google.gson.Gson;

/**
 * Class to represent twoots formatted in the json format
 */
public class JsonTwoot {

    private static final String PAYLOAD_NAME = "data:twooter/formatted;json,";

    private String agent;
    private String[] images;
    private String text;

    /**
     * Attempts to resolve the specified twoot text into a JsonTwoot. Will return null if the text is not in the correct format
     *
     * @param text the twoot to resolve
     * @return a JsonTwoot if the twoot is valid twooter json
     */
    public static JsonTwoot resolve(String text) {

        if (text.startsWith(PAYLOAD_NAME)) { //if it starts with the json header
            try {
                return new Gson().fromJson(text, JsonTwoot.class); //attempt to resolve into json
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public String getAgent() {
        return agent;
    }

    public String[] getImages() {
        return images;
    }

    public String getText() {
        return text;
    }
}
