package hangman;

import java.awt.SystemTray;
import java.io.File;
import java.util.Scanner;

/**
 * Created by Stephen on 1/26/2018.
 */

public class Main {
    public static void main(String[] args) {
        int wordSize;
        int guess;
        char mychar= 'y';
        String myString;
        String str;
        File myFile;
        EvilHangmanGame theGames = new EvilHangmanGame();
        int arg = args.length;
        while((int) mychar == (int) 'y') {
            if (arg > 0) {
                myFile = new File(args[0]);
            } else {
                System.out.println("1st Arguement not found!");
                return;
            }
            if (arg > 1) {
                str = args[1];
                wordSize = Integer.parseInt(str);
            } else {
                System.out.println("2nd Arguement not found!");
                return;
            }
            if (arg > 2) {
                str = args[2];
                guess = Integer.parseInt(str);
            } else {
                System.out.println("3rd Arguement not found!");
                return;
            }
            if ((wordSize >= 2) && (guess >= 1)) {
//            System.out.println("Let the games begin");
                theGames.setTurns(guess);
                theGames.startGame(myFile, wordSize);
                if (theGames.getAnswers().size() == 0) {
                    System.out.println("No words of size" + wordSize + " were found...please try again.");
                    return;
                } else {
                    theGames.playTheGame();

                }
            /*
            System.out.println("Here's the answers: ");
            getAnswersNow(theGames);
            //*/
            } else {
                System.out.println("Invalid Word Size or Guess Size");
            }
            System.out.println("Would you like to continue? (input y to continue, any other will stop)");
            Scanner scan = new Scanner(System.in);
            myString = scan.next();
            if (myString.length() != 1) {

            } else {
                mychar = myString.charAt(0);
            }
        }
    }
    public static void getAnswersNow(EvilHangmanGame theGames)
    {
        for(String aStr: theGames.getAnswers())
        {
            //System.out.println(aStr);
        }
    }

}
