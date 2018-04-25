package app.witkey.live.activities.expansion;

/**
 * created by developer2 on 07-07-2017.
 */


/**
 * This file contains basic configuration change that need to do
 * to make this apk expansion working
 */

public class Config {

    /**
     * Main expansion file name under zip obb
     */
    public static final String EX_MAIN_FOLDER_NAME = "main.10.app.witkey.live";//"<enter name>";

    public static final String EX_MAIN_FILE_NAME = "6.gif";//"<enter name>";

    /**
     * Main expansion file extension under zip obb
     */
    public static final String EX_MAIN_FILE_EXTN = "gif";//"<enter extension>";

    /**
     * Main expansion file version
     */
    public static final int EXPANSION_MAIN_VERSION = 10;// <enter your expansion file version>

    /**
     * Main expansion file size
     */
    //6234610
    public static final int EXPANSION_MAIN_FILE_SIZE = 62523000;//142000;// <enter your expansion file file size in bytes>

    /**
     * Modify this as your app from GooglePlay
     * get it from Google play developer console under
     * App > Development tools > Services and APIs > Licensing & in-app billing
     */
//    static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAplb8IUMdlROIqWEGhKgggknhRvdiICD34n3POFP/0TUQE5rHbM/JZWJiuIHdCSXf7jTE/cjmq8mAINsG4S7OmBlTxSnHvSUgZ13iDB5x35PAvHGeLYF9qqFZmEOkzQORfDOJm427EgUgyHhqdBKch3nB7mhAOUBQKt+TvCt2w4hAkSQY2uYI4H0uDHWUQX/nvlvS7MF5PDh5kETEg2KyAtdUu89yZx+juCkVNmPWhfpwXyN8tN4E/kAaYzscDb4TCQmTNT6Nk/JBjg8vAuxwI2w8cOa0b9k3jxKlCVCr4sJIEEqIknvmyfX4vOi+YUe0GPexUbzfIkrwfGK2TroLpwIDAQAB";//"<enter base64 public key>";
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkssI3kKS91xuR+Xh98q6K26wuLV8FdYSVm3jSxiW+ecRPBOkTKGki5JfuujZ2PrSyPhD47PaMfJlxMNPZbmpGUugUNVQlYAbN9Ch9MnX8R+DldVJP+m7ZMNTfDOn1xYeMW2Xv/W63KTKr95Ggccmqjp0NTFuIa34FRyd9KOIZVrKupBWWL7+8XG+M4s0FZFSbqPjTDMTrptK4zWZ+zglCnRi6TqUXCsVY2nSk/BqDXOY1tm5WOvZYAAWFpgbApxbLasslX0CZuCssZNUUJjuOpvG+lQUSqqBFZifVnuGzPDNr27sjXqqEDNxj7QeyV1ROnwuhXLcARQxWG4xxRDwtwIDAQAB";//"<enter base64 public key>";
//    static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlFe9TS5V3F1L+utLfrvZbMUBtjAwwoyJAbefdpJ2qLAMLJENM3khnTyU728PTkCnWamnI7l3zPLml7TeHXB2EnaA5d8/TRXdn6pzqV7F+lflq3vlSOWClM6mzeLmrM7aOiGtl4M6JnxxQpCjarQkZyhHC7+mND+2yWGfBUuSQOoVaOqzPuqJxzioYMXUW1nLKtRyx/3Nya6FIhnVaofgzuvRYgRT81oIq4tTQfAloedXEsBGmluAGNNtG++Qyh/wXacsFdVxTJ1SBwPuUlvwfajATBsfhX7xbeqaX4qADPEKH2BZqyc30LM//KylGW0VYDvs0Z3vHv6pdudKGALEuwIDAQAB";//"<enter base64 public key>";

    /**
     * Here is where you place the data that the validator will use to determine
     * if the file was delivered correctly. This is encoded in the source code
     * so the application can easily determine whether the file has been
     * properly delivered without having to talk to the server.
     */
    public static final XAPKFile[] xAPKS = {
            new XAPKFile(
                    // true signifies a main file
                    true,
                    // the version of the APK that the file was uploaded against
                    EXPANSION_MAIN_VERSION,
                    // the length of the file in bytes
                    EXPANSION_MAIN_FILE_SIZE
            ),
    };
}
