package by.it.kruglenja.jd01_15;

import java.io.File;

/**
 * Created by user on 13.12.2018.
 */
public class Util {
    private Util(){

    }
    static String getPath(Class<?> clazz) {
        String simpleName  = clazz.getSimpleName();
        String path = clazz.getName().replace(simpleName, "");
        path = path.replace(".", File.separator);
        String root = System.getProperty("user.dir");
        String res = root + File.separator + "src" + File.separator + path;
        return res;
    }

    static String getPath() {
        return getPath(Util.class);
    }
    static String getPath(String filename) {
        return getPath() + filename;
    }
}
