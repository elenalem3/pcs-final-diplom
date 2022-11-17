import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> wordsMap = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] allOfPdfs = pdfsDir.listFiles();
        if (allOfPdfs != null) {
            for (File file : allOfPdfs) {
                var doc = new PdfDocument(new PdfReader(file)); //создать объект пдф-документа
                for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(i)); //получить текст со страницы
                    var words = text.split("\\P{IsAlphabetic}+"); //разбить текст на слова
                    Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                    for (var word : words) { // перебираем слова
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1); //добавляем в мапу слова в нижнем регистре
                    }
                    for (var word : freqs.keySet()) { //перебираем мапу по сету ключ-слово-количество
                        int page = i;
                        List<PageEntry> pageList = new ArrayList<>();
                        if (!wordsMap.containsKey(word)) {
                            pageList.add(new PageEntry(file.getName(), page, freqs.get(word)));
                            pageList.sort(PageEntry::compareTo);
                            wordsMap.put(word, pageList);
                        } else {
                            List<PageEntry> entryList = wordsMap.get(word);
                            entryList.add(new PageEntry(file.getName(), page, freqs.get(word)));
                            entryList.sort(PageEntry::compareTo);
                            wordsMap.put(word, entryList);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) { //поиск по слову
        String wordReg = word.toLowerCase(); //преобразуем все символы слова в нижний регистр
        List<PageEntry> pageEntryList = wordsMap.getOrDefault(wordReg, Collections.emptyList());

        return pageEntryList;
    }
}
