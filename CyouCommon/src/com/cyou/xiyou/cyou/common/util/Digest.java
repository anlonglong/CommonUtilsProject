package com.cyou.xiyou.cyou.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;

public class Digest
{
    public static String compute(File file, String type)
    {
        FileInputStream fis = null;

        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 1024];

            for(int bytesRead; (bytesRead = fis.read(buffer)) != -1;)
            {
                messageDigest.update(buffer, 0, bytesRead);
            }

            return HexUtil.bytesToHex(messageDigest.digest());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(fis != null)
                {
                    fis.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String compute(String str, String type)
    {
        return compute(str.getBytes(), type);
    }

    public static String compute(String str, String type, String charsetName)
    {
        try
        {
            return compute(str.getBytes(charsetName), type);
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String compute(byte[] bytes, String type)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(bytes);
            return HexUtil.bytesToHex(messageDigest.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String computeMD5(File file)
    {
        return compute(file, "MD5");
    }

    public static String computeMD5(String str)
    {
        return compute(str, "MD5");
    }

    public static String computeMD5(String str, String charsetName)
    {
        return compute(str, "MD5", charsetName);
    }

    public static String computeMD5(byte[] bytes)
    {
        return compute(bytes, "MD5");
    }

    public static String[] getSupportedTypes()
    {
        Set<String> result = new HashSet<>();
        final String type = "MessageDigest.";
        final String alg = "Alg.Alias." + type;
        String str;

        for(Provider provider: Security.getProviders())
        {
            for(Object key: provider.keySet())
            {
                str = String.valueOf(key).split(" ")[0];

                if(str.startsWith(type) || str.startsWith(alg))
                {
                    result.add(str.substring(str.lastIndexOf('.') + 1));
                }
            }
        }

        return result.toArray(new String[result.size()]);
    }
}