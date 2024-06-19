import com.kenter7317.Registry;

public class run {
    @Registry(value = "abc")
    private static String test = null;

    public static void main(String[] args) {
        System.out.println(test);
    }
}
