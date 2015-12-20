package in.rakhtsavelives.app;

public class ListViewItem {
    private String name,bg,city;
    public ListViewItem(String name,String bg,String city){
        this.name=name;
        this.bg=bg;
        this.city=city;
    }

    public String getName() {
        return name;
    }

    public String getBg() {
        return bg;
    }

    public String getCity() {
        return city;
    }
}