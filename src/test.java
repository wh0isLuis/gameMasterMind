public class test {
    public static void main(String[] args) {
        generate("BBA",3);

    }
    private static void generate(String input,int n) {
        if (n<=0) {
            System.out.println(input);
        } else {
            generate(input + "A", n-1);
            generate(input + "B", n-1);
        }
    }
}
