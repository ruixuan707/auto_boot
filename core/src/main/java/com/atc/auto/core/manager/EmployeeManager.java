package com.atc.auto.core.manager;


import com.atc.auto.core.entity.authority.Employee;

/**
 * EmployeeManager
 *
 * @author Monco
 * @version 1.0.0
 */
public class EmployeeManager {

    private static final ThreadLocal<Employee> LOCAL = new ThreadLocal<Employee>();

    /**
     * 放到当前线程中
     *
     * @param employee 用户
     */
    public static void set(Employee employee) {
        LOCAL.set(employee);
    }

    /**
     * 从当前线程中获取
     *
     * @return Employee 用户
     */
    public static Employee get() {
        return LOCAL.get();
    }


    /**
     * 从当前线程删除
     */
    public static void remove() {
        LOCAL.remove();
    }

}