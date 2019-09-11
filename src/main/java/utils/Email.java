package utils;

import javax.mail.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Email {

    private Folder folder;

    public enum EmailFolder {
        INBOX("INBOX");

        private String text;

        EmailFolder(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public Email(String username, String password, String server, EmailFolder emailFolder) throws MessagingException {
        /* Connects to email server with credentials provided to read from a given folder of the email application
         * @param username    Email username (e.g. test@gmail.com)
         * @param password    Email password
         * @param server      Email server   (e.g. smtp.email.com)
         * @param emailFolder Folder in email application to interact with
         */
        Properties props = System.getProperties();
        try {
            props.load(new FileInputStream(new File(System.getProperty("user.dir") + "/src/main/java/utils/email.properties")));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect(server, username, password);
        folder = store.getFolder(emailFolder.getText());
        folder.open(Folder.READ_WRITE);
    }

    public int getNumberOfMessages() throws MessagingException {
        return folder.getMessageCount();
    }

    public Message getMessageByIndex(int index) throws MessagingException {
        // Gets a message by its position in the folder. The earliest message is indexed at 1.
        return folder.getMessage(index);
    }

    public Message getLatestMessage() throws MessagingException {
        return getMessageByIndex(getNumberOfMessages());
    }

    public String getMessageContent(Message message) throws Exception {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    public String getURl(String message) {
        String url = "";
        Pattern trimmer = Pattern.compile("\\b(?:http|https):\\/{2}\\S+\\b");
        Matcher m = trimmer.matcher(message);
        //gets first link found in the email.. assuming it is the only url for 3botlogin app
        if (m.find()) {
            url = m.group();
        }
        return url;
    }

    private Map<String, Integer> getStartAndEndIndices (int max) throws MessagingException {
        int endIndex = getNumberOfMessages();
        int startIndex = endIndex - max;

        if (startIndex < 1) {
            startIndex = 1;
        }

        Map<String, Integer> indices = new HashMap<String, Integer>();
        indices.put("startIndex", startIndex);
        indices.put("endIndex", endIndex);

        return indices;
    }

    public Boolean waitForNewMessage(int currentMessageNumber) throws MessagingException {
        // will wait for a new email to come giving 30 seconds as timeout
        int i = 0;
        while (getNumberOfMessages() <= currentMessageNumber){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.format("IOException: %s%n", e);
            }
            if (30 < i) {
                return Boolean.FALSE;
            }
            i++;
        }
        return Boolean.TRUE;
    }

}

