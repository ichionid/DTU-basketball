package com.example.giannis.dtu_basketball;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by giannis on 12/30/15.
 */
public final class Cloner {
    /**
      * This method makes a "deep clone" of any Java object it is given.
      */
    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException e) {
            Log.i("exc", "1");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.i("exc", "2");
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.i("exc", "3");
            e.printStackTrace();
        }
        return null;
    }
}
