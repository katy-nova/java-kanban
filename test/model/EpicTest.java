import model.Epic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void cannotAddEpicInsideItself() {
        Epic epic = new Epic("name", "description");
        //нужно ли добавлять эпик в таск менеджер если мы тестируем внутренний метод класса эпик?
        //или мы должны тестировать метод addSubtask в классе Таск Мененеджере?
        // если да, то как его тестировать, ведь он принимает только объект типа подзадача и передать эпик не выйдет
        epic.addSubtask(epic.getID());
        assertTrue(epic.getSubtasks().isEmpty());
    }

}