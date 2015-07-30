package name.alexkosarev.sandbox.springboot.rest.wrappers;

public class MessageWrapper {

    private String message;

    public MessageWrapper() {
    }

    public MessageWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
