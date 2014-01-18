package com.orders.controllers;

import com.orders.facade.OrdersFacade;
import org.datamodel.OrderDataModel;
import org.orders.entity.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="ordersController")
@SessionScoped
public class OrdersController {
    private static Logger _log = Logger.getLogger(OrdersController.class.getName());

    private List<Orders> orderList;
    //Добавление множественного выбора строк заказов
    private List<Orders> selectedOrderList, activeCustOrders, histCustOrders;
    private Orders[] selectedOrders;
    private OrderDataModel mediumOrdersModel;
    private SelectItem[] statusOptions;
    private  String[] statusValues;

    private Order selectedOrder;
    @ManagedProperty("#{proposalController}")
    ProposalController proposalController;
    @ManagedProperty("#{userController}")
    UserController userController;
    @ManagedProperty("#{loginController}")
    LoginController loginController;

    @EJB
    private OrdersFacade ordersFacade;

    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        orderList = new ArrayList<Orders>();
        orderList = ordersFacade.findAll();


        mediumOrdersModel = new OrderDataModel(orderList);

        statusValues = new String[4];
        statusValues[0] = "Обработка";
        statusValues[1] = "Оплачен";
        statusValues[2] = "В пути";
        statusValues[3] = "Доставлен";
        statusOptions = createFilterOptions(statusValues);

    }


    public void refreshOrders(){
        orderList.clear();
        orderList = ordersFacade.findAll();
    }

    public void setBonusTrue(Orders order){
        Orders order_u = ordersFacade.find(order.getRecid());
        order_u.setBonus(true);
        ordersFacade.edit(order_u);

        order.setBonus(true);
    }
    private SelectItem[] createFilterOptions(String[] data)  {
        SelectItem[] options = new SelectItem[data.length + 1];

        options[0] = new SelectItem("", "Select");
        for(int i = 0; i < data.length; i++) {
            options[i + 1] = new SelectItem(data[i], data[i]);
        }

        return options;
    }

    public SelectItem[] getStatusOptions() {
        return statusOptions;
    }

    //Создание строк заказов из корзины покупателя на форме shop.xhtml
    public void addOrders(ShopingCart shopingCart){
        if(loginController.getCustomer().getUser() != null){
            _log.info("Началось создание строк заказов.....");

           for(ShopingCartItem item: shopingCart.getShopingCartItemList()){

               Orders order = new Orders();
               order.setProduct(item.getProposal().getProduct());
               order.setProposal(item.getProposal().getRecid());

                       //Заглушка до создания аутентификации
                       order.setCreatedBy(loginController.getCustomer().getUser());
                       order.setCustomer(loginController.getCustomer().getUser());
                       //---------------

               order.setQty(item.getQty());
               order.setPrice(item.getProposal().getPrice());
               order.setAmount(item.getAmount());
               order.setStatus("Оформлен");
               order.setBonus(false);
               order.setBlocked(item.getProposal().getBlocked());
               order.setFullFilled(item.getProposal().getFullFilled());
               order.setPromo(item.getProposal().getPromo());
               ordersFacade.create(order);

           }
        addMessage("Заказ отправлен на обработку!");
        proposalController.clearShoppingCart();

        }else{
            addMessage("Для создания заказа необходма авторизация!");
        }
    }
    public void viewSelected(){
        for(Orders order : selectedOrders){
            //addMessage(order.getOrderId().toString());
        }
    }

    public void setStatusSelectedOrders(String status){
        for(Orders order : selectedOrders){
            order.setStatus(status);
            ordersFacade.edit(order);
        }
        refreshOrders();
        addMessage("Статус установлен");
    }

    public Double getFillPercent(Proposal proposal){
        Double percent = 0.0;
        percent = getOrderedQtyOnProposal(proposal) / proposal.getStartQty();
        return percent;
    }

    public Double getOrderedQtyOnProposal(Proposal proposal){
            Double qty = Double.valueOf(0);
            for(Orders order: orderList){
               if(order.getProposal().equals(proposal.getRecid()) &&
                       (order.getStatus().equals("Оплачен") || order.getStatus().equals("Оплачен Бонусами"))
                       ){
                   qty+=order.getQty();
               }
           }
        return qty;
    }

    public  Double getRemainQty(Proposal proposal){
        Double qty = Double.valueOf(0);
        qty = proposal.getStartQty() - getOrderedQtyOnProposal(proposal);
        return qty;
    }
    public  Long getMinQty(Proposal proposal){
        return proposal.getMinQty();
    }
    public void setStatusOrder(String status){
        selectedOrder.setStatus(status);
    }

    public List<Orders> getOrderList() {
        return orderList;
    }
    public void filterOrderList(String value){
        for(Orders order : getOrderList()){

        }
    }
    public void setOrderList(List<Orders> orderList) {
        this.orderList = orderList;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public ProposalController getProposalController() {
        return proposalController;
    }

    public void setProposalController(ProposalController proposalController) {
        this.proposalController = proposalController;
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public List<Orders> getSelectedOrderList() {
        return selectedOrderList;
    }

    public void setSelectedOrderList(List<Orders> selectedOrderList) {
        this.selectedOrderList = selectedOrderList;
    }

    public OrderDataModel getMediumOrdersModel() {
        return mediumOrdersModel;
    }

    public void setMediumOrdersModel(OrderDataModel mediumOrdersModel) {
        this.mediumOrdersModel = mediumOrdersModel;
    }

    public Orders[] getSelectedOrders() {
        return selectedOrders;
    }

    public void setSelectedOrders(Orders[] selectedOrders) {
        this.selectedOrders = selectedOrders;
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

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


}
