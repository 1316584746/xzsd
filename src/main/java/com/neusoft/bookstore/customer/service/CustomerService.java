package com.neusoft.bookstore.customer.service;

import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.util.ResponseVo;

/**
 * @Author 20141
 * @Date 2020/4/23 10:59
 * @Version 1.0
 */
public interface CustomerService {


    ResponseVo addCustomer(Customer customer);

    ResponseVo login(Customer customer);

    ResponseVo loginOut(String userAccount);

    ResponseVo listCustomers(Customer customer);

    ResponseVo findCustomerById(Integer id);

    ResponseVo updateCustomerById(Customer customer);

    ResponseVo deleteCustomerById(Integer id);

    ResponseVo updatePwd(String originPwd, String newPwd, Integer userId, String userAccount);

    ResponseVo updateScore(String frontScore, Integer userId, String userAccount);
}

