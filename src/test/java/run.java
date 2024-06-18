import com.kenter7317.Registry;

public class run {
    @Registry(value = "abc")
    private String test;

    public static void main(String[] args) {
        run r = new run();
        System.out.println(r.test);

    }
}
