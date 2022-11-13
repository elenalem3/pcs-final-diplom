public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;  //имя пдф-файла
    private final int page; //номер страницы
    private final int count; //количество раз, которое встретилось слово на странице

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        int result = o.count - this.count;
        if (result == 0) {
            result = this.pdfName.compareTo(o.pdfName);
            if (result == 0) {
                result = this.page - o.page;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "PageEntry{" + "pdf=" + pdfName + ", page=" + page + ", count=" + count + "}";
    }
}
