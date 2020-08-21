package pkgboolean.model_inverted.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad-Ahsan
 */
public class BooleanModel_InvertedIndex {
    public static void main(String[] args) {
        Map<String,LinkedList<Integer>> II = new TreeMap<String,LinkedList<Integer>>();
        
        //Map<String,Map<Integer,LinkedList<Integer>>> PI = new TreeMap<String,Map<Integer,LinkedList<Integer>>>();
        
        ArrayList stopWords = new ArrayList();
        
        ArrayList<String> allTOKENS = new ArrayList<String>(); 
        ArrayList<String> uniqueTOKENS = new ArrayList<String>(); 
        
        String[] finalText = new String[50];

        System.out.println("Preprocessing...");
        ExtractingStopWords(stopWords);
        WritingAllTokens(stopWords,finalText,allTOKENS);
        WritingUnique_SortedTokens(uniqueTOKENS,allTOKENS);
        CreatingInvertedIndex(uniqueTOKENS,finalText,II);
        System.out.println("Preprocessing Done.");
//        II.forEach((key,value) -> System.out.println(key + " = " + value));
//        System.out.println("\nInverted Index Size = "+II.size()+"\n");
        
//          try{
//            CreatingPositionalIndex(uniqueTOKENS,II);
//          }catch (FileNotFoundException ex) {
//            Logger.getLogger(Inverted_Positional_Index_Assignment1K163742.class.getName()).log(Level.SEVERE, null, ex);
//          }
  
        QuerySearching(II);
    }
    public static void ExtractingStopWords(ArrayList stopWords){
        File swFile = new File("Stopword-List.txt");        
        Scanner sc1 = null;
        try {
            sc1 = new Scanner(swFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BooleanModel_InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(sc1.hasNext()){
            stopWords.add(sc1.next());
        }
    }
    
    public static void WritingAllTokens(ArrayList stopWords,String[] finalText,ArrayList<String> allTOKENS){
        String fileName = "";
        String[] words = null;
        
//        File newFile = null;
//        BufferedWriter newFilebr = null;
//        String newFileName = "FinalFileSizes.txt";
//            newFile = new File(newFileName);
//        try {
//            newFilebr = new BufferedWriter(new FileWriter(newFile,true));
//        } catch (IOException ex) {
//            Logger.getLogger(Inverted_Positional_Index_Assignment1K163742.class.getName()).log(Level.SEVERE, null, ex);
//        }
                
        for(int i=1; i<51; i++){
            try {
                fileName = i+".txt";
                
                finalText[i-1] = "";
                
                File file = new File(fileName);
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                
                String str = new String(data, "UTF-8");
                str = str.replaceAll("[^\na-zA-Z0-9 -]","");
                str = str.replaceAll("[\\-]"," ");
                str = str.replaceAll("[\\\n]"," ");
                str = str.toLowerCase();
                
                words = str.split(" ");
        
                for(int w=0; w<words.length; w++){
                    if( !stopWords.contains(words[w]) ){
                        finalText[i-1] += words[w] + " ";
                    }
                }
                
//                System.out.println("file "+i+" "+finalText[i-1]);
//                newFilebr.write(i+" "+finalText[i-1]+"\n"+"end"+i+"\n");

//                String[] fileWords = finalText[i-1].split(" ");
//                System.out.println(i+" "+fileWords.length);
//                newFilebr.append(i+" "+fileWords.length+"\n");
                
                StringTokenizer st1 = new StringTokenizer(finalText[i-1]," ",true);
                
                while (st1.hasMoreTokens()){
                      
                    while(st1.hasMoreTokens()){
                        String t = st1.nextToken();
                        allTOKENS.add(t);
                    }
                }
                
                }   
                catch (FileNotFoundException ex) {
                Logger.getLogger(BooleanModel_InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                Logger.getLogger(BooleanModel_InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    
    
    public static void WritingUnique_SortedTokens(ArrayList<String> uniqueTOKENS,ArrayList<String> allTOKENS){
        
        Set<String> TokenAll = new HashSet<String>();
        
        for(int t=0; t<allTOKENS.size(); t++){
            String word = allTOKENS.get(t);
            TokenAll.add(word);
        }
        
        Iterator<String> TokenAllIterator = TokenAll.iterator();
        while (TokenAllIterator.hasNext()) {
            String word = TokenAllIterator.next();
            uniqueTOKENS.add(word);
        }
        Collections.sort(uniqueTOKENS);
    }
    
    
    public static void CreatingInvertedIndex(ArrayList<String> uniqueTOKENS,String[] finalText, Map<String,LinkedList<Integer>> II){
        
        Iterator<String> uniqueTOKENSIterator = uniqueTOKENS.iterator();
        while(uniqueTOKENSIterator.hasNext()){
            String word = uniqueTOKENSIterator.next();
            LinkedList<Integer> Postings = new LinkedList<Integer>();
            
            for (int f = 1; f < 51; f++)
            {
                if (finalText[f-1].contains(" "+word+" ") == true)
                {
                        Postings.addLast(f);
                }
            }
            
            II.put(word, Postings);
        }
        
    }
    
//    public static void CreatingPositionalIndex(ArrayList<String> uniqueTOKENS, Map<String,LinkedList<Integer>> II) throws FileNotFoundException{
//        
//        Map<String,Map<Integer,LinkedList<Integer>>> PI = new TreeMap<String,Map<Integer,LinkedList<Integer>>>();
//        String fileName = "Final.txt";
//        String line = "";
//
//        File rFile = new File(fileName);    
//        FileReader fr = new FileReader(rFile);
//        BufferedReader br = new BufferedReader(fr);
//        
//        LinkedList<Integer> P = new LinkedList<Integer>();
//        LinkedList<Integer> wordsP = new LinkedList<Integer>();
//        
//        int i =0;
//        
//        Iterator<String> uniqueTOKENSIterator = uniqueTOKENS.iterator();
//        while(uniqueTOKENSIterator.hasNext()){
//            String word = uniqueTOKENSIterator.next();
//            P = II.get(word);
//            
//            String[] files = new String[P.size()];
//            i=0;
//            for(int pos : P){
//
//                try {
//                    line = br.readLine();
//                } catch (IOException ex) {
//                    Logger.getLogger(Inverted_Positional_Index_Assignment1K163742.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                //System.out.println("p = "+pos);
//                while(line != ("end"+pos)){
//                    files[i] += line;
//                }
//
//                i++;
//            }
//            i=0;
//            int count = 0;
//            for(int pos=0 ;pos<files.length; pos++){
//                String[] words = files[pos].split(" ");
//                count = 0;
//                for(int w=0; w<words.length; w++){
//                    if(!(words[w] == word)){
//                        count++;
//                    }
//                }
//                wordsP.add(count);
//            }
//        }
//        
//    }
    
    public static void QuerySearching(Map<String,LinkedList<Integer>> II){
        
        while(true)
        {
            String text = Menu();
            text = text.toLowerCase();
            
            if (text.equals("0")){
                return;
            }
            else if( !(text.isEmpty()) )
            {
                try {
                    String[] words = text.split(" ");
                    LinkedList<Integer> term1Posting = new LinkedList<Integer>();

                    for(int w=0; w<words.length; w++){
                        if(words[w].equals("and")){
                            LinkedList<Integer> term2Posting = II.get(words[++w]);
                            term1Posting = Query2Terms(term1Posting,term2Posting,"and",II);

                        }else if(words[w].equals("or")){
                            LinkedList<Integer> term2Posting = II.get(words[++w]);
                            term1Posting = Query2Terms(term1Posting,term2Posting,"or",II);

                        }else if(words[w].equals("not")){
                            //LinkedList<Integer> term2Posting = II.get(words[++w]);
                            term1Posting = Query1TermNOT(words[++w],II);

                        }else{
                            term1Posting = II.get(words[w]);
                        }
                    }

                    System.out.println(term1Posting.size()+" documents retieved.");
                    for(int a=0; a<term1Posting.size(); a++){
                        System.out.print(term1Posting.get(a)+".txt"+" ");
                    }
                    System.out.println("");
                }catch(Exception e){
                    System.out.println("Term not in Inverted Index!");
                }
            }
        }    
    }
    
    public static LinkedList<Integer> Query1TermNOT(String word, Map<String,LinkedList<Integer>> II){
       
        LinkedList<Integer> test = new LinkedList<Integer>();
        LinkedList<Integer> finalAnswer = new LinkedList<Integer>();
        LinkedList<Integer> answer = new LinkedList<Integer>();

        for(int i=0; i<50; i++){
            test.addLast(i+1);
        }

        answer = II.get(word);

        for(int j=0; j<test.size(); j++){
            if( !(answer.contains(test.get(j))) ){
                finalAnswer.addLast( test.get(j) );
            } 
        }

        return finalAnswer;
    }
    
    public static LinkedList<Integer> Query2Terms(LinkedList<Integer> term1P, LinkedList<Integer> term2P, String operator, Map<String,LinkedList<Integer>> II){

        LinkedList<Integer> answer = new LinkedList<Integer>();
    
        if( operator.equals("and") ){

            int ll2 = 0,ll1 = 0; boolean flag2 = true, flag1 = true;
            
            while( !(ll1>term1P.size()-1) && !(ll2>term2P.size()-1) ){

                if( term1P.get(ll1).equals(term2P.get(ll2)) ){
                    answer.addLast(term1P.get(ll1));
                    flag2 = true;
                    flag1 = true;
                }else if( term1P.get(ll1).compareTo(term2P.get(ll2))<0 ){
                    flag2 = false;
                    flag1 = true;
                }else{
                    flag2 = true;
                    flag1 = false;
                }

                if(flag1 == true){
                    ll1++;
                }
                if(flag2 == true){
                    ll2++;
                }
            }
        }else if( operator.equals("or") ){

            for(int i=0; i<term1P.size(); i++){
                answer.addLast(term1P.get(i));
            }

            for(int i=0; i<term2P.size(); i++){
                if( !(answer.contains(term2P.get(i))) ){
                    answer.addLast(term2P.get(i));
                }
            }
        }

        return answer;
    }
    
    public static String Menu()
    {
        System.out.println("\nEnter Boolean Query:  \t OR \tEnter 0 to Exit");
        System.out.println("e.g. Query-> cat and dog\n");
        Scanner input = new Scanner(System.in);
        String text = input.nextLine();
        return text;
    }
}
