/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Compu Fire
 */
public class PasswordUtils {
 
    private static PasswordUtils instance;
    
    private PasswordUtils(){
        
    }
    
    public static PasswordUtils getInstance(){
        if(instance == null){
            instance = new PasswordUtils();
        }
        return instance;
    }
    
    public String encryptedPassword(String pass){
        return BCrypt.hashpw(pass, BCrypt.gensalt());
    }
    
    public Boolean checkPassword(String pass, String encryptedPass){
        return BCrypt.checkpw(pass, encryptedPass);
    }
}

