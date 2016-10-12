/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.dto.SupplierDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface SupplierController extends SuperController {

    public SupplierModel getSupplierById(String supplierId) throws ClassNotFoundException, SQLException;
    public ArrayList<SupplierModel> getAllSuppliers() throws ClassNotFoundException, SQLException;
    
    public int saveSupplier(SupplierDTO supplier)throws ClassNotFoundException, SQLException;
    public int changeSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException;
    public int removeSupplier(String supplierId) throws ClassNotFoundException, SQLException;
    public ArrayList<SupplierDTO> getSuppliers(String supplierName) throws ClassNotFoundException,SQLException;    

    public class SupplierModel {

        private String supplierId;
        private String name;
        private String address;
        private String email;
        private String contact;
        private String fax;
        private String cordintatorName;
        private String cordinatorContact;
        private String agentName;
        private String agenNo;
        private String supplierAdded;

        public SupplierModel(String supplierId, String name, String address, String email, String contact, String fax, String cordintatorName, String cordinatorContact, String agentName, String agenNo, String supplierAdded) {
            this.supplierId = supplierId;
            this.name = name;
            this.address = address;
            this.email = email;
            this.contact = contact;
            this.fax = fax;
            this.cordintatorName = cordintatorName;
            this.cordinatorContact = cordinatorContact;
            this.agentName = agentName;
            this.agenNo = agenNo;
            this.supplierAdded = supplierAdded;
        }

        /**
         * @return the supplierId
         */
        public String getSupplierId() {
            return supplierId;
        }

        /**
         * @param supplierId the supplierId to set
         */
        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @param address the address to set
         */
        public void setAddress(String address) {
            this.address = address;
        }

        /**
         * @return the email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email the email to set
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * @return the contact
         */
        public String getContact() {
            return contact;
        }

        /**
         * @param contact the contact to set
         */
        public void setContact(String contact) {
            this.contact = contact;
        }

        /**
         * @return the fax
         */
        public String getFax() {
            return fax;
        }

        /**
         * @param fax the fax to set
         */
        public void setFax(String fax) {
            this.fax = fax;
        }

        /**
         * @return the cordintatorName
         */
        public String getCordintatorName() {
            return cordintatorName;
        }

        /**
         * @param cordintatorName the cordintatorName to set
         */
        public void setCordintatorName(String cordintatorName) {
            this.cordintatorName = cordintatorName;
        }

        /**
         * @return the cordinatorContact
         */
        public String getCordinatorContact() {
            return cordinatorContact;
        }

        /**
         * @param cordinatorContact the cordinatorContact to set
         */
        public void setCordinatorContact(String cordinatorContact) {
            this.cordinatorContact = cordinatorContact;
        }

        /**
         * @return the agentName
         */
        public String getAgentName() {
            return agentName;
        }

        /**
         * @param agentName the agentName to set
         */
        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        /**
         * @return the agenNo
         */
        public String getAgenNo() {
            return agenNo;
        }

        /**
         * @param agenNo the agenNo to set
         */
        public void setAgenNo(String agenNo) {
            this.agenNo = agenNo;
        }

        /**
         * @return the supplierAdded
         */
        public String getSupplierAdded() {
            return supplierAdded;
        }

        /**
         * @param supplierAdded the supplierAdded to set
         */
        public void setSupplierAdded(String supplierAdded) {
            this.supplierAdded = supplierAdded;
        }

    }

}
