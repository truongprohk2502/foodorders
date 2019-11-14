package code.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordered")
public class Ordered {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "orderedId")
    private List<SingleOrder> singleOrders;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    private Timestamp orderDate;

    private String status;

    public Ordered() {
        singleOrders = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<SingleOrder> getSingleOrders() {
        return singleOrders;
    }

    public void setSingleOrders(List<SingleOrder> singleOrders) {
        this.singleOrders = singleOrders;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
