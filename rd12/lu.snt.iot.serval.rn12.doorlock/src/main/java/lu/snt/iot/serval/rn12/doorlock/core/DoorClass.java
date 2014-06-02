package lu.snt.iot.serval.rn12.doorlock.core;

import java.lang.String;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Assaad
 */
public class DoorClass  {
private final static int DEFAULTLENGTH = 1; //VERY IMPORTANT TO CHANGE LATER

    private int passLength;
    private String pubKey="";
    private String currentHash="";


    private boolean doorState=false;
    private Random randomGenerator = new Random();



    //In Java, it is better to name and type the parameters
    public DoorClass(String[] args) // public DoorClass(String publicKey, String pass)
    {
        try
        {
        pubKey= args[0]; //pubKey = publicKey;
            int temp= Integer.valueOf(args[1]);
            if(temp>4&&temp<16)
                passLength= temp;
            else
                passLength=DEFAULTLENGTH;
        }
        catch(Exception ex)
        {
            //LOG EXCEPTION
        }
        changePassword() ;

    }



    public void closeDoor()
    {
        doorState=false;
        //LOG DOOR CLOSED HERE on "doorLog" Channel
    }
    
    public boolean getDoorState()
    {
        return doorState;
    }

    public void changePassword()
    {
        int min=1;
        int max=9;

        int i;

        for(i=0;i<passLength-1;i++)
        {
            min=min*10;
            max=max*10;
        }

        int x = randomGenerator.nextInt(max)+min;


        currentHash = Hash(Integer.toString(x));

        if(pubKey=="")
        {
            //Send x not encrypted on "passwordOutput" Channel
        }
        else
        {
            //Send x encrypted on "passwordOutput" Channel
        }
        //LOG PASSWORD CHANGE


    }


    
    public boolean openDoor(String password)
    {
        if(password.length()==passLength&&passContainJustNumbers(password)==true && Hash(password).equals(currentHash)==true) {
            //OPEN THE DOOR EVENT
            //LOG DOOR OPENED HERE on "doorLog" Channel
            doorState=true;
            return true;
        }
        else
        {
            //LOG FAILED ATTEMPT HERE on "doorLog" Channel
            return false;
        }
    }
    
    private boolean passContainJustNumbers(String password)
    {
         for (int i = 0; i < password.length();i++){
            if (!Character.isDigit((password.charAt(i)))){
                return false;
                }
            }
        return true;
    
    }
    
    private String Hash(String x)
    {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(x.getBytes()); 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DoorClass.class.getName()).log(Level.SEVERE, null, ex);
        }
      return "";        
    }



}
