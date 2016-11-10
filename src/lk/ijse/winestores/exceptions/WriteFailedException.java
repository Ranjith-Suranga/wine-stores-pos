/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.exceptions;

/**
 *
 * @author Ranjith Suranga
 */
public class WriteFailedException extends Exception{
    
    private String message;

    public WriteFailedException() {
    }

    public WriteFailedException(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        if (message != null){
            return message;
        }
        return super.getMessage();
    }
    
}
