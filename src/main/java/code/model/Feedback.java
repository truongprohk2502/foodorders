package code.model;

public class Feedback {

    public static final String FROM_EMAIL = "truongproly2502@gmail.com";

    public static final String TARGET_EMAIL = "nguyendinhtruong98@gmail.com";

    public static final String SUBJECT_EMAIL = "Feedback from customer";

    private String name;

    private String email;

    private String phone;

    private String content;

    public Feedback() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
