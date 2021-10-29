class Book {
    private final Page[] pages;
    int pageNum;

    Book() {
        this(1);
        //This is when the user create a book,
        //default constructor with no arguments
        //this.pageNum = 1;
    }

    

    private Book(int pageNum) {
        this.pageNum = pageNum;
    }

    Book(Page[] pages) {
        this.pages = pages;
    }

    Book nextPage() {
        return this.goToPage(this.pageNum + 1);
    }

    Book prevPage() {
        return this.goToPage(this.pageNum - 1);
    }

    Book goToPage(int pageNum) {
        //Constraints
        if(pageNum < 1 || pageNum > this.numPages()) {
            pageNum = this.pageNum;
        }
        return new Book(this.pages, pageNum);
    }

    private Page getPage(int pageNum) {
        return this.pages[pageNum - 1];
    }   

    private int numPages() {
        return this.pages.length;
    }

    public String toString() {
        return this.getPage(this.pageNum) + "\n page " + this.pageNum;
    }
}
