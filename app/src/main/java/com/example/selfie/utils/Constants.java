package com.example.selfie.utils;

/**
 * Class that contains all the Constants required in our Video Upload
 * client App.
 */
public class Constants {
    /**
     * URL of the SelfieWebService.  Please Read the Instructions in
     * README.md to set up the SERVER_URL.
     */
    //for AVD
    //public static final String SERVER_URL = "https://10.0.2.2:8443";

    //for Genymotion and USB device
    public static final String SERVER_URL = "https://192.168.56.1:8443";
    
    /**
     * Define a constant for 1 MB.
     */
    public static final long MEGA_BYTE = 1024 * 1024;

    /**
     * Maximum size of Selfie to be uploaded in MB.
     */
    public static final long MAX_SIZE_MEGA_BYTE = 50 * MEGA_BYTE;
}
