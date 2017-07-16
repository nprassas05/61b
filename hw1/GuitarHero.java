/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHero {
    static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        /* create an array of 37 guitar strings for each character that could be typed from keyboard string */
        synthesizer.GuitarString[] guitarStrings = new synthesizer.GuitarString[37];
        for (int i = 0; i < 37; ++i) {
            double freq = 440.0 * Math.pow(2, (i - 24) / 12.0);
            guitarStrings[i] = new synthesizer.GuitarString(freq);
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                
                if (index != -1) {
                    guitarStrings[index].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (synthesizer.GuitarString g: guitarStrings) {
                sample += g.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            for (synthesizer.GuitarString g: guitarStrings) {
                g.tic();
            }
        }
    }
}

