public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to NeuroYou Neurofeedback therapy!");
        try {
            Runnable r1 = ShapedWindowDemo.dimming();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
