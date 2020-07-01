package com.neusoft.bookstore.customer.mapper;

import com.neusoft.bookstore.customer.model.Customer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author 20141
 * @Date 2020/4/23 11:01
 * @Version 1.0
 */

@Mapper
public interface CustomerMapper {

    List<Customer> findCustomerByPhoneAndAccount(Customer customer);

    int addCustomer(Customer customer);


    Customer selectLoginCustomer(Customer customer);

    List<Customer> listCutomers(Customer customer);

    Customer findCustomerById(Integer id);

    List<Customer> findCustomerByPhoneAndAccountExOwn(Customer customer);

    int updateCustomerById(Customer customer);

    int deleteCustomerById(Integer id);

    int updatePwdById(Map<Object, Object> map);

    int updateScoreById(Map<Object, Object> map);
}

