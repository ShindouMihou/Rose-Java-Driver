package pw.mihou.rosedb.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoseUtility {

    public static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping().serializeNulls().create();

}
