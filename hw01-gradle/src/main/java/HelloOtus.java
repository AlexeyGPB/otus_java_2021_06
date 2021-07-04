import com.google.common.primitives.Chars;
import java.util.Arrays;

public class HelloOtus {
    public static void main(String[] args) {
        char[] arr1 = {'O', 't', 'u', 's'};
        char[] arr2 = {'j', 'a', 'v', 'a'};
        char[] arr = Chars.concat(arr1, arr2);
        System.out.println(Arrays.toString(arr));
    }

}
