package code.service;

import code.model.SingleOrder;
import code.repository.SingleOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SingleOrderServiceImpl implements SingleOrderService {

    @Autowired
    private SingleOrderRepository singleOrderRepository;

    @Override
    public void save(SingleOrder order) {
        singleOrderRepository.save(order);
    }
}
