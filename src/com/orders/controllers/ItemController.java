package com.orders.controllers;

import com.orders.facade.ProductFacade;
import com.orders.facade.RelevantprodFacade;
import org.datamodel.ProductDataModel;
import org.orders.entity.Items;
import org.orders.entity.Product;
import org.orders.entity.Relevantproducts;
import org.orders.entity.SelectedProduct;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="itemController")
@SessionScoped
public class ItemController {
    private static Logger log = Logger.getLogger(ItemController.class.getName());
    private List<Items> itemList;
    private Product selected;
    private List<Product> products, filteredProducts;
    private List<Relevantproducts> relevantproductes;
    private Relevantproducts selectedRelevant;
    private List<SelectedProduct> selectedProducts;

    private ProductDataModel selectedProductsModel;
    private Product[] selectedProductsA;

    @EJB
    private ProductFacade productFacade;
    @EJB
    private RelevantprodFacade relevantprodFacade;

    public void refresh(){
        products = productFacade.findAll();
        selected = productFacade.find(selected.getRecid());
    }
    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        products = productFacade.findAll();
        if(!productFacade.findAll().isEmpty()){
            products = productFacade.findAll();
            selected = products.get(0);
        }else{selected = new Product();}

        relevantproductes = relevantprodFacade.findAll();
        selectedProducts = new ArrayList<SelectedProduct>();

        selectedProductsModel = new ProductDataModel(products);
    }
    public void addRelevantProducts(){
        relevantprodFacade.attachReleventProducts(selectedProductsA, selected);
        productSelection();
       addMessage("Релевантные продукты связаны!");
    }
    public void deleteRelevantProduct(){
        relevantprodFacade.remove(selectedRelevant);
        relevantproductes =  relevantprodFacade.findRelevantProducts(selected.getRecid());
        selectedRelevant = relevantproductes.get(0);
        addMessage("Релевантные продукты сброшен!");
    }
    public void productSelection(){
        selectedProductsA = null;
        relevantproductes =  relevantprodFacade.findRelevantProducts(selected.getRecid());
    }
   /* public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = servletContext.getRealPath("") + File.separator + "resources\\content" + File.separator+ file.getFileName();
        String path = servletContext.getRealPath("") + File.separator + "resources\\content\\";

        String name = *//*selected.getItemId() +*//*
                 event.getFile().getFileName().substring(
                event.getFile().getFileName().lastIndexOf('.'));
        InputStream is = file.getInputstream();
        File outfile = new File(path + name);
        OutputStream out = new FileOutputStream(outfile);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            //fos.write(buf, 0, len);
            out.write(buf, 0, len);
        is.close();
        out.close();
        //fos.close();
        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }*/

    public void handleFileUploadDB(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        log.info("Item file uploaded" + file.getSize());
        selected.setPhoto(file.getContents());
        productFacade.edit(selected);
        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }

    public StreamedContent getImage() throws IOException {
        if(selected.getPhoto() != null){
            return new DefaultStreamedContent(new ByteArrayInputStream(selected.getPhoto()));
        }
        return null;
    }
    public void create(){

        Product product = new Product();
        product.setName("Good");
        product.setProduct("dfsgdfg");
        product.setCreatedBy("Admin");
        product.setCreatedAt(new Date());
        product.setUpdatedBy("Admin");
        product.setUpdatedAt(new Date());
        product.setVersion(Long.valueOf(1));

        selected = product;
        productFacade.create(product);
        products = productFacade.findAll();
        addMessage("Товар создан!");
    }
    public void edit(){
        productFacade.edit(selected);
        selected = productFacade.find(selected.getRecid());
        products.clear();
        products = productFacade.findAll();
        addMessage("Товар обновлен!");
    }
    public void delete(){
        try{
            productFacade.remove(selected);
            log.info("Удален объект");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
        products = productFacade.findAll();
        selected = products.get(0);
        addMessage("Товар удален!");
    }
    public void search(){

        addMessage("Товар найден!");
    }

    public List<Items> getItemList() {
        return itemList;
    }

    public void setItemList(List<Items> itemList) {
        this.itemList = itemList;
    }

    public Product getSelected() {
        return selected;
    }

    public void setSelected(Product selected) {
        this.selected = selected;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Relevantproducts> getRelevantproductes() {
        return relevantproductes;
    }

    public void setRelevantproductes(List<Relevantproducts> relevantproductes) {
        this.relevantproductes = relevantproductes;
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public List<SelectedProduct> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<SelectedProduct> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public ProductDataModel getSelectedProductsModel() {
        return selectedProductsModel;
    }

    public void setSelectedProductsModel(ProductDataModel selectedProductsModel) {
        this.selectedProductsModel = selectedProductsModel;
    }

    public Product[] getSelectedProductsA() {
        return selectedProductsA;
    }

    public void setSelectedProductsA(Product[] selectedProductsA) {
        this.selectedProductsA = selectedProductsA;
    }

    public Relevantproducts getSelectedRelevant() {
        return selectedRelevant;
    }

    public void setSelectedRelevant(Relevantproducts selectedRelevant) {
        this.selectedRelevant = selectedRelevant;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
