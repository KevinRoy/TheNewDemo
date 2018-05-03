package com.mattbaby.vega.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式utils
 * Created by kevin on 17/5/18.
 */

public class FormatUtils {

    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
