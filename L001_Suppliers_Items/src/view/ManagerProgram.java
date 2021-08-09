/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controll.ItemDTO;
import controll.SupplierDTO;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ItemsDAO;
import model.SuppliersDAO;

/**
 *
 * @author DELL
 */
public class ManagerProgram extends javax.swing.JFrame {
    Vector dataSuppliers = new Vector();
    Vector dataItems = new Vector();
    Vector<String> columnSuppliers = new Vector<>();
    Vector<String> columnItems = new Vector<>();
    DefaultTableModel defaultTableModel;
    int success = 1;
    boolean addNewSuppliers = true;
    boolean addNewItems = true;
    
    public ManagerProgram() {
        initComponents();
        this.setLocationRelativeTo(null);
        setColumnNameSuppliers();
        setColumnNameItems();
        newSuppliers();
        newItems();
        loadDataSupplier();
        loadDataItems();
    }
    
    private void newSuppliers(){
        addNewSuppliers = true;
        txtCodeSup.setText("");
        txtCodeSup.setEditable(true);
        txtNameSup.setText("");
        txtAddress.setText("");
        chbCollaborating.setSelected(false);
    }
    
    private void newItems(){
        addNewItems = true;
        txtCodeItem.setText("");
        txtCodeItem.setEditable(true);
        txtNameItem.setText("");
        cmbSupplier.setSelectedIndex(0);
        cmbSupplier.setEnabled(true);
        txtUnit.setText("");
        txtPrice.setText("");
        chbSupplying.isSelected();
    }
    
    private void setColumnNameSuppliers(){
        columnSuppliers.add("Code");
        columnSuppliers.add("Name");
        columnSuppliers.add("Address");
        defaultTableModel = (DefaultTableModel) tblSuppliers.getModel();
        defaultTableModel.setDataVector(dataSuppliers, columnSuppliers);
    }
    
    private void setColumnNameItems(){
        columnItems.add("Code");
        columnItems.add("Name");
        columnItems.add("SupCode");
        columnItems.add("Unit");
        columnItems.add("Price");
        defaultTableModel = (DefaultTableModel) tblItems.getModel();
        defaultTableModel.setDataVector(dataItems, columnItems);
    }
    
    private void loadDataSupplier(){    
        try {
            for(SupplierDTO s : SuppliersDAO.getAllSupplier()){
                Vector row = new Vector();
                row.add(s.getSupCode());
                row.add(s.getSupName());
                row.add(s.getAddress());
                cmbSupplier.addItem(s.getSupCode() + " " + s.getSupName());
                dataSuppliers.add(row);
            }
        } catch (Exception ex) {
            System.out.println("Lỗi loadData");
        }
        //tblSuppliers.updateUI();
    }
    
    private void loadDataItems(){
        try{
            for(ItemDTO i : ItemsDAO.getAllItems()){
                Vector row = new Vector();
                row.add(i.getItemCode());
                row.add(i.getItemName());
                row.add(i.getSupCode());
                row.add(i.getUnit());
                row.add(i.getPrice());
                row.add(1000*(i.getPrice()));
                dataItems.add(row);
            }
        }catch(Exception e){
            System.out.println("Loi loadDataItems");
        }
        tblItems.updateUI();
    }
    
    private void saveSupplier(){
        String supCode = txtCodeSup.getText().toUpperCase();
        String supName = txtNameSup.getText();
        String address = txtAddress.getText();
        boolean coll = chbCollaborating.isSelected();
        if(supCode.isEmpty() || supName.isEmpty() || address.isEmpty()){
            JOptionPane.showMessageDialog(this,"Code, Name or Address can not empty!");
        }
        else if(!supCode.matches("^[A-Z]{1,5}")){
            JOptionPane.showMessageDialog(this, "Code of supplier must be from 1 to 5 lecter");
        }
        else{
            SupplierDTO s = new SupplierDTO(supCode, supName, address, coll);
            try{
               if(SuppliersDAO.getSupplierByCode(supCode)!=null){
                    JOptionPane.showMessageDialog(this, "This Code has dupplicated");
                }
                else if(SuppliersDAO.insertSupplier(s)==success){
                    Vector row = new Vector();
                    row.add(supCode);
                    row.add(supName);
                    row.add(address);
                    dataSuppliers.add(row);
                    cmbSupplier.addItem(supCode+ " " + supName);
                    JOptionPane.showMessageDialog(this, "Save successfully");
                    newSuppliers();
                }
           }catch(Exception e){
                e.printStackTrace();
           }
        }
        tblSuppliers.updateUI();
    }
    
    private void saveItem(){
        String itemCode = txtCodeItem.getText().toUpperCase();
        String itemName = txtNameItem.getText();
        String[] storeCmbSupplier = cmbSupplier.getItemAt(cmbSupplier.getSelectedIndex()).split(" ");
        String supCode = storeCmbSupplier[0];

        String unit = txtUnit.getText();
        String price = txtPrice.getText();
        boolean supplying = chbSupplying.isSelected();
        if(itemCode.isEmpty()||itemName.isEmpty()||unit.isEmpty()||price.isEmpty()){
            JOptionPane.showMessageDialog(this, "Code, Name, Unit, Price cann not empty!");
        }
        else if(checkPrice(price)==false){
            JOptionPane.showMessageDialog(this, "Price must be a number!");
        }
        else if(!itemCode.matches("E\\d{4}")){
            JOptionPane.showMessageDialog(this, "Code must be E and have 4 number!");
        }
        else{
            ItemDTO i = new ItemDTO(itemCode, itemName,supCode,unit,Float.parseFloat(price), supplying);
            try{
                if(ItemsDAO.getItemByCode(itemCode)!=null){
                    JOptionPane.showMessageDialog(this, "This code has dupplicated");
                }
                else if(ItemsDAO.insertItem(i)==success){
                    Vector row = new Vector();
                    row.add(itemCode);
                    row.add(itemName);
                    row.add(supCode);
                    row.add(unit);
                    row.add(price);
                    dataItems.add(row);
                    JOptionPane.showMessageDialog(this, "Save successfully!");
                    newItems();
                }
           }catch(Exception e){
                e.printStackTrace();
            }
        }
        tblItems.updateUI();
    }
    
   private void updateSupplier(){
        String supCode = txtCodeSup.getText();
        String supName = txtNameSup.getText();
        String address = txtAddress.getText();
        boolean coll = chbCollaborating.isSelected();
        try{
            SupplierDTO s = new SupplierDTO(supCode, supName, address, coll);
            if(SuppliersDAO.updateSupplier(s)==success){
                int selectRow = tblSuppliers.getSelectedRow();
                Vector row = (Vector) dataSuppliers.get(selectRow);
                row.set(1, supName);
                row.set(2,address);
                updateComboBox(supCode, supName);
                JOptionPane.showMessageDialog(this, "Update successfully");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        tblSuppliers.updateUI();
    }
    
    private int getValueComboBox(String value){
        for(int i=1;i<cmbSupplier.getItemCount();i++){
            if(cmbSupplier.getItemAt(i).contains(value)){
                return i;
            }
        }
        return 0;
    }
    
    private void updateComboBox(String supCode, String supName){
        for(int i=1;i<cmbSupplier.getItemCount();i++){
            if(cmbSupplier.getItemAt(i).contains(supCode)){
                cmbSupplier.removeItemAt(i);
                cmbSupplier.addItem(supCode+supName);
                break;
            }
        }
    }
    
    private void updateItem(){
        String itemCode = txtCodeItem.getText();
        String itemName = txtNameItem.getText();
        String[] storeCmbSupplier = cmbSupplier.getItemAt(cmbSupplier.getSelectedIndex()).split(" ");
        String supCode = storeCmbSupplier[0];
        
        String unit = txtUnit.getText();
        String price = txtPrice.getText();
        boolean supplying = chbSupplying.isSelected();
        try{
            ItemDTO i = new ItemDTO(itemCode, itemName , supCode , unit , Float.parseFloat(price) , supplying);
            if(ItemsDAO.updateIteṃ(i)==success){
                int selectRow = tblItems.getSelectedRow();
                Vector row = (Vector) dataItems.get(selectRow);
                row.set(1, itemName);
                row.set(3, unit);
                row.set(4, price);
                JOptionPane.showMessageDialog(this, "Update succesfully!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        tblItems.updateUI();
    }
    
    private boolean checkPrice(String price){
        while(true){
            try{
                Float.parseFloat(price);
                return true;
            }catch(NumberFormatException e){
                return false;
            }
        }
    }
    
    private void deleteAllDataItem(){
        dataItems.removeAll(dataItems);
        tblItems.updateUI();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSuppliers = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCodeSup = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtNameSup = new javax.swing.JTextField();
        btnAddNewSup = new javax.swing.JButton();
        btnSaveSup = new javax.swing.JButton();
        btnDeleteSup = new javax.swing.JButton();
        chbCollaborating = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCodeItem = new javax.swing.JTextField();
        txtNameItem = new javax.swing.JTextField();
        txtUnit = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        cmbSupplier = new javax.swing.JComboBox<>();
        btnAddNewItem = new javax.swing.JButton();
        btnSaveItem = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        chbSupplying = new javax.swing.JCheckBox();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblSuppliers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSuppliersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSuppliers);

        jLabel1.setText("Code:");

        jLabel2.setText("Name:");

        jLabel3.setText("Address:");

        jLabel4.setText("Collaborating:");

        btnAddNewSup.setText("Add New");
        btnAddNewSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewSupActionPerformed(evt);
            }
        });

        btnSaveSup.setText("Save");
        btnSaveSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSupActionPerformed(evt);
            }
        });

        btnDeleteSup.setText("Delete");
        btnDeleteSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnAddNewSup)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodeSup)
                    .addComponent(txtNameSup)
                    .addComponent(txtAddress)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnSaveSup, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(btnDeleteSup, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(chbCollaborating)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodeSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNameSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chbCollaborating))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddNewSup)
                    .addComponent(btnSaveSup)
                    .addComponent(btnDeleteSup))
                .addGap(87, 87, 87))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Suppliers", jPanel3);

        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItems);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        jLabel5.setText("Item Code:");

        jLabel6.setText("Item Name:");

        jLabel7.setText("Supplier:");

        jLabel8.setText("Unit");

        jLabel9.setText("Price:");

        jLabel10.setText("Supplying:");

        cmbSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));

        btnAddNewItem.setText("Add New");
        btnAddNewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewItemActionPerformed(evt);
            }
        });

        btnSaveItem.setText("Save");
        btnSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveItemActionPerformed(evt);
            }
        });

        btnDeleteItem.setText("Delete");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnAddNewItem)
                        .addGap(31, 31, 31)
                        .addComponent(btnSaveItem, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(2, 2, 2))
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodeItem)
                            .addComponent(txtNameItem)
                            .addComponent(txtUnit)
                            .addComponent(txtPrice)
                            .addComponent(cmbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(chbSupplying)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtCodeItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNameItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(chbSupplying))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddNewItem)
                    .addComponent(btnSaveItem)
                    .addComponent(btnDeleteItem))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Items", jPanel2);

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnAddNewSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewSupActionPerformed
        // TODO add your handling code here:
        newSuppliers();
    }//GEN-LAST:event_btnAddNewSupActionPerformed

    private void btnAddNewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewItemActionPerformed
        // TODO add your handling code here:
        newItems();
    }//GEN-LAST:event_btnAddNewItemActionPerformed

    private void tblSuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSuppliersMouseClicked
        // TODO add your handling code here:
        addNewSuppliers = false;
        txtCodeSup.setEditable(false);
        int selectedRow = tblSuppliers.getSelectedRow();
        String supCode = tblSuppliers.getValueAt(selectedRow, 0).toString();
        String supName = tblSuppliers.getValueAt(selectedRow, 1).toString();
        String address = tblSuppliers.getValueAt(selectedRow, 2).toString();
        txtCodeSup.setText(supCode);
        txtNameSup.setText(supName);
        txtAddress.setText(address);
        try {
            if(SuppliersDAO.getCollaborating(supCode)==true){
                chbCollaborating.setSelected(true);
            }else{
                chbCollaborating.setSelected(false);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagerProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblSuppliersMouseClicked

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked
        // TODO add your handling code here:
        addNewItems = false;
        txtCodeItem.setEditable(false);
        cmbSupplier.setEditable(false);
        int selectedRow = tblItems.getSelectedRow();
        String itemCode = tblItems.getValueAt(selectedRow, 0).toString();
        String itemName = tblItems.getValueAt(selectedRow, 1).toString();
        String supCode = tblItems.getValueAt(selectedRow, 2).toString();
        String unit = tblItems.getValueAt(selectedRow, 3).toString();
        String price = tblItems.getValueAt(selectedRow, 4).toString();
        txtCodeItem.setText(itemCode);
        txtNameItem.setText(itemName);
        cmbSupplier.setSelectedIndex(getValueComboBox(supCode));
        txtUnit.setText(unit);
        txtPrice.setText(price);
        try{
            if(ItemsDAO.getSupplying(itemCode)==true){
                chbSupplying.setSelected(true);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_tblItemsMouseClicked

    private void btnSaveSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSupActionPerformed
        // TODO add your handling code here:
        if(addNewSuppliers){
            saveSupplier();
        }
        else{
            updateSupplier();
        }
        
    }//GEN-LAST:event_btnSaveSupActionPerformed

    private void btnSaveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveItemActionPerformed
        // TODO add your handling code here:
        if(addNewItems){
            saveItem();
        }
        else{
            updateItem();
        }
    }//GEN-LAST:event_btnSaveItemActionPerformed

    private void btnDeleteSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSupActionPerformed
        // TODO add your handling code here:
        int row = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION);
        if(row==JOptionPane.YES_OPTION){
            try{
                int selectRow = tblSuppliers.getSelectedRow();
                String supCode = tblSuppliers.getValueAt(selectRow, 0).toString();
                if(SuppliersDAO.deleteSupplier(supCode)==success){
                    dataSuppliers.remove(selectRow);                   
                    cmbSupplier.removeItemAt(getValueComboBox(supCode));
                    JOptionPane.showMessageDialog(this, "Delete successfully!");
                    newSuppliers();
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "Delete failed");
            }
        }
        tblSuppliers.updateUI();
        deleteAllDataItem();
        loadDataItems();
    }//GEN-LAST:event_btnDeleteSupActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        // TODO add your handling code here:
        int row = JOptionPane.showConfirmDialog(this,"Are you sure?", "Delete", JOptionPane.YES_NO_OPTION);
        if(row==JOptionPane.YES_OPTION){
            try{
                int selectRow = tblItems.getSelectedRow();
                String itemCode = tblItems.getValueAt(selectRow, 0).toString();
            
                if(ItemsDAO.deleteItem(itemCode)==success){
                    dataItems.remove(selectRow);
                    JOptionPane.showMessageDialog(this, "Delete succesfully!");
                    newItems();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Delete Failed. Please try again!");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        tblItems.updateUI();
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManagerProgram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerProgram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerProgram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerProgram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerProgram().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNewItem;
    private javax.swing.JButton btnAddNewSup;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnDeleteSup;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSaveItem;
    private javax.swing.JButton btnSaveSup;
    private javax.swing.JCheckBox chbCollaborating;
    private javax.swing.JCheckBox chbSupplying;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblSuppliers;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCodeItem;
    private javax.swing.JTextField txtCodeSup;
    private javax.swing.JTextField txtNameItem;
    private javax.swing.JTextField txtNameSup;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtUnit;
    // End of variables declaration//GEN-END:variables
}
