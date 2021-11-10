package core.writer;

import core.element.picture.PictureElement;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Усольцев Иван
 */
public interface DocumentConstructor {

    int createNewPageAndSetCurrent(String pageName);
    int getCurrentPageIndex();

    <T extends TableRowPresentation> void createTable(Table<T> table);
    void createTextBlock(TextBlock text);
    void createImage(PictureElement pe);

    void setPrintSetting(PrintSetting ps);
    void setPrintSetting(int pageIndex, PrintSetting ps);

    Path writeToFile() throws IOException;
}
