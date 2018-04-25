import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Solution {

    public static void main(String[] args) {

        //Prepare collection words
        Reader reader = new Reader();
        reader.setBook("words.txt");
        List<String> Book = reader.getBook();

        //Sorting collection by asc
        Sorter s = new Sorter();
        s.setBookForSorted(Book);
        Book = s.getSortedBookByAsc();

        //Prepare collection by composition words
        Compositor c = new Compositor();
        c.setBookForComposite(Book);
        Book = c.getCompositeBook();

        //Prepare resulting array
        Resulter r = new Resulter();
        r.setBookAndSorter(Book, s);
        String[] rm = r.getResult();

        //Return result for console
        for (int i = 0; i != rm.length;i++) {
            System.out.println(rm[i]);
        }
        System.out.println("Concatenated words count - " + Book.size());
    }

}

class Reader {
    private String fileName;
    private List<String> buffer = new ArrayList<>();

    public void setBook(String name) {
        this.fileName = name;
    }

    public List<String> getBook() {
        ReaderEngine();
        return buffer;
    }

    private final void ReaderEngine() {
        try {
            buffer = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

final class Sorter {
    private List<String> sortedBook;

    public void setBookForSorted(List<String> book) {
        this.sortedBook = book;
    }

    public List<String> getSortedBookByAsc() {
        SortingEngine("Asc");
        return sortedBook;
    }

    public List<String> getSortedBookByDesc() {
        SortingEngine("Desc");
        return sortedBook;
    }

    private final void SortingEngine(String sequence) {
        int result;
        Collections.sort(sortedBook, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                switch (sequence) {
                    case "Asc": {
                        return o1.length() - o2.length();
                    }
                    case "Desc": {
                        return o2.length() - o1.length();
                    }
                }
                return 0;
            }
        });
    }
}

class Compositor {
    private List<String> Book;
    private Set<String> draftBook = new HashSet<>();
    private List<String> compositeBook = new ArrayList<>();

    public void setBookForComposite(List<String> Book) {
        this.Book = Book;
    }

    public List<String> getCompositeBook() {
        getComposition();
        return this.compositeBook;

    }

    private final void getComposition() {

        for(String paje:this.Book) {
            if (matcherPajes(draftBook, paje)) {
                compositeBook.add(paje);
            }
            draftBook.add(paje);
        }
    }

    private final boolean matcherPajes(Set<String> draftBook, String paje){

        if (draftBook.isEmpty()) return false;
        boolean[] chars = new boolean[paje.length() + 1];
        chars[0] = true;
        for (int i = 1; i <= paje.length(); i++) {
            for (int j = 0; j < i ; j++) {
                if (chars[j]==false) {
                    continue;
                }
                if (draftBook.contains(paje.substring(j, i))) {
                    chars[i] = true;
                    break;
                }
            }
        }
        return chars[paje.length()];
    }


}

class Resulter {
    private List<String> Book;
    private Sorter s;
    private String[] Result = new String[3];


    public void setBookAndSorter(List<String> Book, Sorter s) {
        this.Book = Book;
        this.s = s;
    }

    public String[] getResult() {
        resulting();
        return Result;
    }

    private final void resulting() {
        int i = 0;
        int j = 0;

        //Reuse sorting class by desc
        s.setBookForSorted(this.Book);
        this.Book = s.getSortedBookByDesc();

        try {
            Result[0] = Book.get(0).toString();
            do {
                if (Book.get(i++).length() < Result[0].length()) {
                    Result[++j] = Book.get(i - 1).toString();
                }
            } while (Result[2] == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
