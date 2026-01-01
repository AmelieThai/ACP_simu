public class Types {
    public static void main(String[] args) {
        byte b = 127;
        short s = 32767;
        int i = 2147483647;
        long l = 9223372036854775807L;
        float f = 1E-45f;
        double d = 1E-323;
        boolean bl = true;
        char c = 'A';
        String str = "Hello";

        System.out.println("--- Valeurs initiales ---");
        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);
        System.out.println("boolean: " + bl);
        System.out.println("char: " + c);
        System.out.println("String: " + str);


        b = (byte) (b + 1);
        s = (short) (s + 1);
        i = i + 1;
        l = l + 1;
        c = (char) (c + 1);

        f = f / 10;
        d = d / 10;

        bl = !bl;

        str = str + " World!";

        System.out.println("\n--- Valeurs modifiées ---");
        System.out.println("byte (+1): " + b + " (Overflow!)");
        System.out.println("short (+1): " + s + " (Overflow!)");
        System.out.println("int (+1): " + i + " (Overflow!)");
        System.out.println("long (+1): " + l + " (Overflow!)");
        System.out.println("float (/10): " + f);
        System.out.println("double (/10): " + d);
        System.out.println("boolean (!): " + bl);
        System.out.println("char (+1): " + c);
        System.out.println("String (+): " + str);
    }
}
