package com.orders.controllers;

import com.orders.facade.OrdersFacade;
import org.orders.entity.Customer;
import org.orders.entity.Orders;
import org.orders.entity.UsersE;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="loginController")
@SessionScoped
public class LoginController {
    private static Logger _log = Logger.getLogger(LoginController.class.getName());
    private String username;
    private String password;
    private UsersE curUser;
    private Boolean isAuth = false;
    private String msg;
    private String menuTxt = "Вход";
    private Boolean isAdmin = false;
    private Boolean isUser = false;
    private Customer customer;
    private List<Orders> activeCustOrders, histCustOrders;

    @EJB
    private com.orders.facade.UserFacade userFacade;

    @EJB
    private com.orders.facade.CustomerFacade customerFacade;

    @EJB
    private OrdersFacade ordersFacade;

    public void auth(String login){
        for(UsersE usr : userFacade.findAll()){
            _log.info("Сравниваем: " + usr.getLogin().trim() + "  ___c__   " + login.trim());

            if (usr.getLogin().trim().equals(login.trim())) {
                _log.info("Пользователь найден: " + login);

                if(usr.getPassword().trim().equals(password.trim())){
                    _log.info("Пароль верен: " + password);
                   setAuth(true);
                   setMenuTxt(login);
                   setMsg("Авторизация прошла успешно");
                   //Заглушка для ролей пользователя
                   setRole(usr.getRole());
                   curUser = usr;
                   setCustomer(customerFacade.findCustomer(login));
                   findCustOrders();
                   //-----------
                   addMessage(msg + ": " + isAuth);
                   return;

                   }else{
                       _log.info("Пароль ошибка: " + password);
                       setAuth(false);
                       setMsg("Пароль введен неверно");
                       addMessage(msg + ": " + isAuth);
                       return;
                   }

            } else {
                   setAuth(false);
                   setMsg("Пользователя не существует");
            }
        }
        addMessage(msg + ": " + isAuth);
    }

    public void logout(){
        username = "";
        password = "";
        setMenuTxt("Вход");
        setMsg("Выход выполнен");
        setAdmin(false);
        setAuth(false);
        curUser = new UsersE();
        customer = new Customer();
        addMessage(msg + ": " + isAuth);
    }
    public void setRole(String role){
       if(role.equals("Admin")){
           setAdmin(true);
       }
        if(role.equals("User")){
            setUser(true);
        }
    }

    public void findCustOrders(){
        _log.info("Идет поиск заказов клиента:" + customer.getName() + " " +customer.getSurname() + "--Юзер: " + customer.getUser());

        activeCustOrders = ordersFacade.findCustOrders(customer.getUser());
        for(Orders order: activeCustOrders) {
            _log.info(order.getRecid() + " : " + order.getCreatedBy());
        }
        _log.info("Найден строк:" + activeCustOrders.size());

        histCustOrders = ordersFacade.findCustOrders(customer.getName());
    }

    public void saveCustomer(){
        customerFacade.edit(customer);
        userFacade.edit(curUser);

        addMessage("Данные сохранены");
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public String getMenuTxt() {
        return menuTxt;
    }

    public void setMenuTxt(String menuTxt) {
        this.menuTxt = menuTxt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UsersE getCurUser() {
        return curUser;
    }

    public void setCurUser(UsersE curUser) {
        this.curUser = curUser;
    }

    public List<Orders> getActiveCustOrders() {
        return activeCustOrders;
    }

    public void setActiveCustOrders(List<Orders> activeCustOrders) {
        this.activeCustOrders = activeCustOrders;
    }

    public List<Orders> getHistCustOrders() {
        return histCustOrders;
    }

    public void setHistCustOrders(List<Orders> histCustOrders) {
        this.histCustOrders = histCustOrders;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
