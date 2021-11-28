package homework;


import java.util.Stack;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private Stack<Customer> listCustomer = new Stack<>();

    public void add(Customer customer) {
        listCustomer.push(customer);
    }

    public Customer take() {
        return listCustomer.pop();
        // return null; // это "заглушка, чтобы скомилировать"
    }
}
