package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Stephen on 1/26/2018.
 */

public class EvilHangmanGame implements IEvilHangmanGame
{
    private int asciiConvert = 97;
    private int cur;
    private Set<String> ans = new HashSet<String>();
    private char[] guesses = new char[26];
    private StringBuilder readOut = new StringBuilder();
    private StringBuilder newReadOut = new StringBuilder();
    private int FinalLength;
    private int turns;

    @Override
    public void startGame(File dictionary, int wordLength)
    {
        readOut = new StringBuilder();
        newReadOut = new StringBuilder();
        for(int i = 0; i < 26; i++) {guesses[i] = (char) 1000;}
        FinalLength = wordLength;
        for(int i = 0; i < wordLength; i++)
        {
            readOut.append("-");
            newReadOut.append("-");
        }
        makePotentialWords(dictionary, wordLength);
    }
    public void playTheGame()
    {
        int ascii;
        char user;
        int revealed;
        boolean correct = false;
        int num;
        while(turns != 0)
        {
            num = 0;
            correct = false;
//            System.out.println("Your best guess is: " + bestGuess());
            printAns();
            System.out.println(ans.size());
            revealed = 0;
            if(turns != 1) {System.out.println("You have " + turns + " guesses left.");}
            else{System.out.println("You have " + turns + " guess left.");}

            System.out.print("Used Letters: ");
            for(int i = 0; i < 26; i++)
            {
                ascii = (int) guesses[i] - asciiConvert;
                if(i == ascii){System.out.print(guesses[i] + " ");}
            }
            System.out.println("");

            System.out.println("Word: " + newReadOut.toString());

            user = userInterface();
            try {
                makeGuess(user);
            } catch (GuessAlreadyMadeException e) {
                e.printStackTrace();
            }
            ans = changeAnswers(user, 0, ans);
            setRevealedWord(user);
            for(int i = 0; i < newReadOut.toString().length(); i++)
            {
                if(((int)'-') != ((int) readOut.charAt(i)))
                {
                    newReadOut.setCharAt(i, readOut.charAt(i));
                    correct = true;
                    num++;
                }
                if(((int) newReadOut.charAt(i)) != ((int)'-'))
                {
                    revealed++;
                }
            }
            if(num!=0)
            {
                System.out.println("Yes there is " + num + " " + user);
            }
            else
            {
                System.out.println("Sorry, there are no " + user + "'s.");
            }
            System.out.println("");
            if(revealed == FinalLength)
            {
                System.out.println("You Win!");
                printAns();
                break;
            }
            if(!correct){turns--;}
        }
        if(!correct)
        {
            System.out.println("You lose!");
            System.out.println("The word was: " + ans.toArray()[0]);
        }
    }
    public char bestGuess()
    {
        Set<String> A;
        char test;
        char next = '\t';
        int size;
        int small = ans.size();
        boolean contain = false;
        for(int i = 0; i < guesses.length; i++)
        {
            test = (char)(i+asciiConvert);
            if(((int) guesses[i] != (int) '-' && (int) guesses[i] != (int) test))
            {
                A = changeAnswers(test, 0, ans);
                size = A.size();
                if(!contain)
                {
                    if(contains(A, test))
                    {
                        small = size;
                        next = test;
                        contain = true;
                    }
                }
                if(contain)
                {
                    if(contains(A, test))
                    {
                        if (small >= size)
                        {
                            small = size;
                            next = test;
                        }
                    }
                }
                else if(small >= size)
                {
                    small = size;
                    next = test;
                }
            }
        }
        return next;
    }
    public boolean contains(Set<String> mySet, char myChar)
    {
        boolean contains = false;
        String str = (String) mySet.toArray()[0];
        for(int i = 0; i < str.length(); i++)
        {
            if((int) str.charAt(i) == (int) myChar){contains = true;}
        }
        return contains;
    }

    public char userInterface()
    {
        String user = "fail";
        char use;

        while(user == "fail")
        {
            System.out.print("Enter guess: ");
            Scanner scan = new Scanner(System.in);
            user = scan.next();
            user = user.toLowerCase();
            use = user.charAt(0);
            if (user.length() == 1)
            {
                if(!((97 <= (int) use)&&((int) use <= 122)))
                {
                    if(turns != 1) {System.out.println("You have " + turns + " guesses left.");}
                    else {System.out.println("You have " + turns + " guess left.");}
                    user = "fail";
                }
                else
                {
                    if(((int) guesses[(use)-asciiConvert]) == (int)use)
                    {
                        GuessAlreadyMadeException();
                        user = "fail";
                    }
                    else{}
                }
            }
            else
            {
                System.out.println("Invalid Input");
                if(turns != 1) {System.out.println("You have " + turns + " guesses left.");}
                else {System.out.println("You have " + turns + " guess left.");}
                user = "fail";
            }
        }
        return user.charAt(0);
    }
    public void GuessAlreadyMadeException()
    {
        System.out.println("You already used that letter");
        if(turns != 1) {System.out.println("You have " + turns + " guesses left.");}
        else {System.out.println("You have " + turns + " guess left.");}
    }
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException
    {
        ///*
        if(!((97 <= (int) guess)&&((int) guess <= 122)))
        {
            throw new GuessAlreadyMadeException();
        }
        else
        {
            if(((int) guesses[(guess)-asciiConvert]) == (int)guess)
            {
                throw new GuessAlreadyMadeException();
            }
            else{guesses[(int) (guess) -asciiConvert] = guess;}
        }
        ans = changeAnswers(guess, 0, ans);
        setRevealedWord(guess);
        //*/
        return ans;
    }
    public void makePotentialWords(File dictionary, int wordLength)
    {
        int size;
        String word;
        try
        {
            Scanner scan = new Scanner(dictionary);
            while(scan.hasNext())
            {
                word = scan.next();
                if(word.length() == wordLength)
                {
                    ans.add(word);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    public int getSetSize(Set<Object> S) {return S.size();}

    public Set<String> getAnswers(){return ans;}
    public Set<String> changeAnswers(char guess, int at, Set<String> input)
    {
        //Somehow need to change 'String in' to the private value outPut
        Set<String> A = new HashSet<String>();
        Set<String> B = new HashSet<String>();
        Set<String> C;
        cur = at;
        if((at == FinalLength) || (input.size() == 0))
        {
            return input;
        }
        else
        {
            for(String pAns: input)
            {
                if((int) pAns.charAt(at) == (int) guess)
                {
                    A.add(pAns);
                }
                else
                {
                    B.add(pAns);
                }
            }
            A = changeAnswers(guess, at+1, A);
            B = changeAnswers(guess, at+1, B);
            C = setPreference(A,B,guess);
            /*
            if(C.equals(A)){readOut.setCharAt(at, guess);}
            else{readOut.setCharAt(at, '-');}
            */
            return C;
        }
    }
    public Set<String> setPreference(Set<String> A, Set<String> B, char guess)
    {
        if(A.size() > B.size())
        {
            return A;
        }
        else if(A.size() < B.size())
        {
            return B;
        }
        else
        {
//            if(cur != FinalLength)
//           {
//                A = changeAnswers(guess, cur, A);
//                B = changeAnswers(guess, cur, B);
//                return setPreference(A, B, guess);
//            }
            return B;
        }
    }
    public void setTurns(int t){turns = t;}
    public void printAns()
    {
        System.out.println(ans);
    }
    public void setRevealedWord(char user)
    {
        String[] myArray = ans.toArray((new String[0]));
        for(int i = 0; i < myArray[0].length(); i++)
         {
             if((int) myArray[0].charAt(i) == (int) user)
             {
                 newReadOut.setCharAt(i, user);
                 readOut.setCharAt(i, user);
             }
             else
             {
                 readOut.setCharAt(i, '-');
             }
         }
    }

}
