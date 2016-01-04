package in.rakhtsavelives.app;

public class ListViewItem {
    private String name, bg, city, age;

    public ListViewItem(String name, String bg, String city, String age) {
        this.name = name;
        this.bg = bg;
        this.city = city;
        this.age = age;
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

    public String getAge() {
        return age;
    }
}