import java.util.Date;
import java.util.List;
import java.util.Map;

public class FlareDatabaseClass {

    public List<String> ActivityList;
    public List<String> DietList;
    public List<String> MiscList;

    public Map<Integer, FlareClassAbstract> flares;
}

class FlareClassAbstract {
    private Date start;
    private Date end;
    private int avg_pain;
    private int flare_length;
    private Map<Integer, FlareClass> flareList;
}

class FlareClass {
    private List<Integer> pain_nums;
    private List<Integer> times;
}
