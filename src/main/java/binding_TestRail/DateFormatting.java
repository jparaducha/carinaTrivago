package binding_TestRail;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatting {
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"+" "+"'T'HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }
}
