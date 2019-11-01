package code.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long star;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "foodId")
    private Food food;

    private Timestamp reviewDate;

    public Review() {
    }

    public Review(Long star, String comment, Account account, Food food, Timestamp reviewDate) {
        this.star = star;
        this.comment = comment;
        this.account = account;
        this.food = food;
        this.reviewDate = reviewDate;
    }

    public String getFormatDate() {
        String s = "";
        if ("ROLE_USER".equals(account.getRole())) {
            s += "User - ";
        }
        if ("ROLE_ADMIN".equals(account.getRole())) {
            s += "Admin - ";
        }
        DateTime time = new DateTime(reviewDate.getTime());
        switch (time.getMonthOfYear()) {
            case 1:
                s += "January ";
                break;
            case 2:
                s += "February ";
                break;
            case 3:
                s += "March ";
                break;
            case 4:
                s += "April ";
                break;
            case 5:
                s += "May ";
                break;
            case 6:
                s += "June ";
                break;
            case 7:
                s += "July ";
                break;
            case 8:
                s += "August ";
                break;
            case 9:
                s += "September ";
                break;
            case 10:
                s += "October ";
                break;
            case 11:
                s += "November ";
                break;
            case 12:
                s += "December ";
                break;
        }
        s += time.getDayOfMonth() + ", " + time.getYear();
        return s;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStar() {
        return star;
    }

    public void setStar(Long star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }
}
