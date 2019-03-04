import java.util.ArrayList;

public class Items {

    private  String title;
    private  String number;
    private  String id;
    public ArrayList<String> saleid;


    public Items(String title, String number,String id) {
        this.title=title;
        this.number=number;
        this.id=id;
        saleid = new ArrayList<String>();

    }

    public String getTitle() {
        return title;
    }

    public String getNumber() {
        return number;
    }

    public String getId(){ return id;}

    public String getSaleid(int i)
    { return saleid.get(i);
    }

    public void assignnumber(String str){
        this.number=str;


    }
    public void assignsaleid(String str, int i){

        this.saleid.add(i,str);
    }
}
